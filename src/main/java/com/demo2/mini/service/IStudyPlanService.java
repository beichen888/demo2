package com.demo2.mini.service;

import com.demo2.mini.model.StudyPlan;

/**
 * Created by miguo on 2018/6/7.
 */
public interface IStudyPlanService {

    StudyPlan findByUserId(Integer userId);

    StudyPlan save (StudyPlan studyPlan);

    void delete(Integer id);



}
