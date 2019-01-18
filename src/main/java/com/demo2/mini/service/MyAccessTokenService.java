package com.demo2.mini.service;

import com.demo2.common.AccessToken;
import com.demo2.common.Config;
import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import com.demo2.mini.dao.MyAccessTokenDao;
import com.demo2.mini.model.MyAccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class MyAccessTokenService implements IMyAccessTokenService {

    private static Logger logger = LoggerFactory.getLogger(MyAccessTokenService.class);
    @Resource
    private Config config;
    @Resource
    private MyAccessTokenDao myAccessTokenDao;
    @Resource
    private RestTemplate restTemplate;

    @Override
    public String getAccessToken() throws AppException {
        List<MyAccessToken> dbAccessToken = myAccessTokenDao.findAll();
        if (dbAccessToken.size() > 0) {
            MyAccessToken token = dbAccessToken.get(0);
            if (token.isValid()) {
                return token.getAccessToken();
            }
        }
        return getTokenFromWeixin();
    }

    private String getTokenFromWeixin() throws AppException {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", config.getAppid());
        map.put("secret", config.getSecret());
        map.put("grant_type", "client_credential");
        AccessToken token = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type={grant_type}&appid={appid}&secret={secret}", AccessToken.class, map);
        String accessToken = getToken(token);
        if (accessToken != null) {
            // update db
            myAccessTokenDao.deleteAll();
            MyAccessToken myAccessToken = new MyAccessToken();
            myAccessToken.setAccessToken(token.getAccessToken());
            myAccessToken.setExpire(token.getExpires_in());
            myAccessToken.setCreatedDate(new Date());
            myAccessTokenDao.save(myAccessToken);
            return accessToken;
        } else {
            return null;
        }
    }

    private String getToken(AccessToken token) throws AppException {
        if (token.getErrcode() == null) {
            logger.info("access_token:" + token.getAccessToken());
            logger.info("expires_in:" + token.getExpires_in());
            return token.getAccessToken();
        } else {
            throw new AppException(MessageCode.MINI_GET_ACCESS_TOKEN_ERROR, token.getErrcode());
        }
    }
}
