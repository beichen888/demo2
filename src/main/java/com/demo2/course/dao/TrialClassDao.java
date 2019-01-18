package com.demo2.course.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.course.model.TrialClass;

import java.util.Date;
import java.util.List;

public interface TrialClassDao extends BaseJpaRepository<TrialClass> {
    List<TrialClass> findByDateBetweenAndEnabled(Date from, Date from1, boolean enabled);
}
