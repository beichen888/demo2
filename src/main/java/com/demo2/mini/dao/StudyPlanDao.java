package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.StudyPlan;

public interface StudyPlanDao extends BaseJpaRepository<StudyPlan> {

    StudyPlan findStudyPlanByUserIdEquals(Integer userId);
}
