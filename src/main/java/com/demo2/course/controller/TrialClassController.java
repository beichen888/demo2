package com.demo2.course.controller;

import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.course.model.TrialClass;
import com.demo2.course.model.TrialClassStudent;
import com.demo2.course.service.ITrialClassService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@Auth
public class TrialClassController {

    private ITrialClassService trialClassService;

    /**
     * 后台试听课管理列表
     */
    @GetMapping("/trialClasses")
    public Result list(Boolean enabled, @PageableDefault(sort = "date", direction = Sort.Direction.DESC) Pageable pageable) {
        return new Result<>(true, trialClassService.list(enabled, pageable));
    }

    /**
     * 当月试听课列表
     */
    @GetMapping("trialClasses/month")
    @Auth("anon")
    public Result month() {
        return new Result<>(true, trialClassService.listByMonth());
    }

    /**
     * 创建试听课
     */
    @PostMapping("/trialClass")
    public Result create(@RequestBody TrialClass trialClass) {
        trialClassService.create(trialClass);
        return new Result();
    }

    /**
     * 修改试听课
     */
    @PutMapping("/trialClass/{id}")
    public Result update(@PathVariable Integer id, @RequestBody TrialClass trialClass) {
        trialClass.setId(id);
        trialClassService.update(trialClass);
        return new Result();
    }

    /**
     * 启用试听课
     */
    @PutMapping("/trialClass/enable/{id}")
    public Result enable(@PathVariable Integer id) {
        trialClassService.enable(id);
        return new Result();
    }

    /**
     * 禁用试听课
     */
    @PutMapping("/trialClass/disable/{id}")
    public Result disable(@PathVariable Integer id) {
        trialClassService.disable(id);
        return new Result();
    }

    /**
     * 试听课学生列表
     */
    @GetMapping("/trialClass/students")
    public Result students(Integer classId, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        return new Result<>(true, trialClassService.students(classId, pageable));
    }

    /**
     * 试听课报名
     */
    @PostMapping("/trialClass/student")
    @Auth("anon")
    public Result student(@RequestBody TrialClassStudent student){
        trialClassService.student(student);
        return new Result();
    }

    @Resource
    public void setTrialClassService(ITrialClassService trialClassService) {
        this.trialClassService = trialClassService;
    }
}
