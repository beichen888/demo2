package com.demo2.mini.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.StudyPlan;
import com.demo2.mini.service.IStudyPlanService;
import com.demo2.user.model.UserToken;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 学习计划api
 */
@RestController
@RequestMapping("/api/studyPlan")
@Auth
public class StudyPlanController extends BaseController {

    @Resource
    private IStudyPlanService studyPlanService;

    @GetMapping
    public Result getByUserId(@RequestAttribute("userToken") UserToken userToken) {
        StudyPlan studyPlan = studyPlanService.findByUserId(userToken.getId());
        return renderSuccess(studyPlan);
    }

    @PostMapping
    public Result save(@RequestBody StudyPlan studyPlan) {
        studyPlan = studyPlanService.save(studyPlan);
        return renderSuccess(studyPlan);
    }
}
