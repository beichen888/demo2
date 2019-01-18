package com.demo2.mini.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.UserForm;
import com.demo2.mini.model.UserNotice;
import com.demo2.mini.service.IUserFormService;
import com.demo2.mini.service.IUserNoticeService;
import com.demo2.user.model.UserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by miguo on 2018/6/25.
 */
@RestController
@RequestMapping(value = "/api")
@Auth
@Api(value = "小程序通知相关接口", description = "小程序通知相关接口")
public class UserNoticeController extends BaseController {
    @Resource
    private IUserFormService userFormService;

    @Resource
    private IUserNoticeService userNoticeService;

    @PostMapping("/notice")
    @ApiOperation(value = "设置消息通知时间", notes = "设置消息通知时间", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result addNotice(@RequestAttribute("userToken") UserToken userToken, String noticeTime) {
        UserNotice notice = new UserNotice();
        notice.setUserId(userToken.getId());
        notice.setNoticeTime(noticeTime);
        userNoticeService.save(notice);
        return renderSuccess();
    }

    @GetMapping("/userForm")
    @ApiOperation(value = "取得formid", notes = "取得formid", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result getByUserId(@RequestAttribute("userToken") UserToken userToken) {
        List<UserForm> forms = userFormService.findByUserId(userToken.getId());
        return renderSuccess(forms);
    }

    @PostMapping("/userForm")
    @ApiOperation(value = "保存formid", notes = "保存formid", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result save(@RequestAttribute("userToken") UserToken userToken, String formId) {
        UserForm userForm = userFormService.save(userToken.getId(), formId);
        return renderSuccess(userForm);
    }

}
