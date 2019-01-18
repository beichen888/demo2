package com.demo2.course.service;

import com.demo2.course.model.TrialClass;
import com.demo2.course.model.TrialClassStudent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITrialClassService {
    void student(TrialClassStudent student);

    Page<TrialClassStudent> students(Integer classId, Pageable pageable);

    void create(TrialClass trialClass);

    Page<TrialClass> list(Boolean enabled, Pageable pageable);

    List<TrialClass> listByMonth();

    void update(TrialClass trialClass);

    void enable(Integer id);

    void disable(Integer id);
}
