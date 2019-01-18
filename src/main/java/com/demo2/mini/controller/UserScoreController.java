package com.demo2.mini.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.UserScore;
import com.demo2.mini.service.IUserScoreService;
import com.demo2.user.model.UserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by miguo on 2018/6/25.
 */
@RestController
@RequestMapping("/api")
@Auth
@Api(value = "打卡积分设置", description = "打卡积分设置")
public class UserScoreController extends BaseController {
    @Resource
    private IUserScoreService userScoreService;

    @PostMapping("/score")
    @ApiOperation(value = "保存分数", notes = "保存分数", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result save(@RequestAttribute("userToken") UserToken userToken, String type) {
               userScoreService.save(userToken.getId(), type);
        return renderSuccess();
    }


    @GetMapping("/scores")
    @ApiOperation(value = "取得分数", notes = "取得分数", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result getScores(@RequestAttribute("userToken") UserToken userToken) {
        Map<String, Object> retMap = new HashMap<>();
        int total = userScoreService.findUserTotalScore(userToken.getId());
        retMap.put("total", total);
        List<UserScore> scoreByDay = userScoreService.findAddScoreDaysByUserId(userToken.getId());
        retMap.put("scoreByDay", scoreByDay);
        return renderSuccess(retMap);

    }


}
