package com.demo2.user.controller;

import com.auth0.jwt.JWTSigner;
import com.demo2.common.*;
import com.demo2.common.exception.AppException;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.UserNotice;
import com.demo2.mini.service.IUserNoticeService;
import com.demo2.user.model.*;
import com.demo2.user.service.IUserService;
import com.demo2.word.model.ChineseLevel;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/api")
@Auth
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private IUserService userService;
    @Resource
    private Config config;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private IUserNoticeService userNoticeService;

    /**
     * 登录
     */
    @PostMapping("/user/login")
    @Auth("anon")
    public Result login(String phone, String password) throws Exception {
        User user = userService.findByPhone(phone);
        if (user == null) {
            throw new AppException(MessageCode.USER_NOT_EXIST_ERROR);
        }
        String hashPassword = hashString(password, user.getSalt());
        if (!user.getPassword().equals(hashPassword)) {
            throw new AppException(MessageCode.USER_GET_AUTHENTICATION_INFO_ERROR);
        }
        if (!user.getActive()) {
            throw new AppException(MessageCode.USER_DISABLED_ERROR);
        }
        return new Result<>(true, sign(new UserToken(user.getId(), user.getRole().getName(), user.getPhone(), new Date().getTime())));
    }

    /**
     * 微信登录
     *
     * @param code
     * @return
     * @throws AppException
     */
    @GetMapping("/user/wxLogin")
    @Auth("anon")
    public Result login(String code) throws AppException {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", config.getAppid());
        map.put("secret", config.getSecret());
        map.put("js_code", code);
        AccessToken token = restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={js_code}&grant_type=authorization_code", AccessToken.class, map);
        logger.info("token:" + token.getAccessToken());
        logger.info("openId:" + token.getOpenid());
        if (token.getOpenid() == null) {
            return new Result<>(false, "无法登录，请退出重试");
        }
        User user = userService.findByOpenid(token.getOpenid());
        if (user == null) {
            user = new User();
            user.setOpenid(token.getOpenid());
            user.setGender(Gender.PRIVATE);
            user.setUserLevel(UserLevel.EMPTY);
            user.setChineseLevel(ChineseLevel.EMPTY);
            user.setRole(Role.USER);
            user = userService.create(user);
        }
        //设置默认消息推送时间
        UserNotice userNotice = userNoticeService.findByUserId(user.getId());
        if (userNotice == null) {
            UserNotice notice = new UserNotice();
            notice.setUserId(user.getId());
            notice.setNoticeTime("20:00");
            userNoticeService.save(notice);
        }
        return new Result<>(true, sign(new UserToken(user.getId(), user.getRole().getName(), user.getPhone(), Calendar.getInstance().getTimeInMillis())));
    }

    private String sign(UserToken userToken) {
        JWTSigner signer = new JWTSigner("shannon1122");
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userToken.getId());
        claims.put("role", userToken.getRole());
        claims.put("phone", userToken.getPhone());
        claims.put("datetime", userToken.getDatetime());
        return signer.sign(claims);
    }

    private String hashString(String password, String salt) throws AppException {
        try {
            return Hex.encodeHexString(
                Digests.sha1(password.getBytes(), Hex.decodeHex(salt.toCharArray()), config.getHashIterations()));
        } catch (DecoderException e) {
            throw new AppException(MessageCode.HEX_DECODE_EXCEPTION_ERROR);
        }
    }

    /**
     * 获取个人信息
     */
    @GetMapping("/user/info")
    public Result showInfo(@RequestAttribute("userToken") UserToken userToken) {
        Integer userId = userToken.getId();
        // 基本概况
        User user = userService.find(userId);
        return new Result<>(true, user);
    }

    /**
     * 修改个人信息
     */
    @PutMapping("/user/info")
    public Result changeInfo(@RequestBody User user, @RequestAttribute("userToken") UserToken userToken) {
        user.setId(userToken.getId());
        userService.update(user);
        return new Result();
    }

    /**
     * 修改密码
     */
    @PutMapping("/user/password")
    public Result changePass(String oldPassword, String password, String rePassword, @RequestAttribute("userToken") UserToken userToken) throws AppException {
        if (StringUtils.isBlank(password)) {
            throw new AppException(MessageCode.USER_NEW_PASSWORD_BLANK_ERROR);
        }
        if (!rePassword.equals(password)) {
            throw new AppException(MessageCode.USER_TWICE_PASSWORD_NOT_EQUAL_ERROR);
        }
        userService.updatePassword(userToken.getId(), oldPassword, password);
        return new Result();
    }

    /**
     * 注册用户
     */
    @PostMapping("/user/register")
    @Auth("anon")
    public Result register(String phone, String password, String confirmPassword) throws AppException {
        if (!password.equals(confirmPassword)) {
            throw new AppException(MessageCode.USER_TWICE_PASSWORD_NOT_EQUAL_ERROR);
        }
        User user = new User(phone, password);
        userService.create(user);
        return new Result<>(true, sign(new UserToken(user.getId(), user.getRole().getName(), user.getPhone(), new Date().getTime())));
    }

    /**
     * 重置密码
     */
    @PostMapping("/user/reset")
    public Result reset(String phone, String password, String confirmPassword) throws AppException {
        if (password.isEmpty() && confirmPassword.isEmpty()) {
            throw new AppException(MessageCode.USER_PASSWORD_BLANK_ERROR);
        }
        if (!password.equals(confirmPassword)) {
            throw new AppException(MessageCode.USER_TWICE_PASSWORD_NOT_EQUAL_ERROR);
        }
        userService.resetPassword(phone, password);
        return new Result();
    }
}
