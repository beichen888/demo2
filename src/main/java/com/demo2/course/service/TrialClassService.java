package com.demo2.course.service;

import com.demo2.course.dao.TrialClassDao;
import com.demo2.course.model.TrialClass;
import com.demo2.course.model.TrialClassStudent;
import com.demo2.course.dao.TrialClassStudentDao;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrialClassService implements ITrialClassService {

    private TrialClassDao trialClassDao;

    private TrialClassStudentDao trialClassStudentDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void student(TrialClassStudent student) {
        student.setCreatedDate(new Date());
        trialClassStudentDao.save(student);
    }

    @Override
    public Page<TrialClassStudent> students(Integer classId, Pageable pageable) {
        TrialClassStudent student = new TrialClassStudent();
        if (classId != null) {
            student.setTrialClassId(classId);
        }
        return trialClassStudentDao.findAll(Example.of(student), pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(TrialClass trialClass) {
        trialClass.setEnabled(true);
        trialClassDao.save(trialClass);
    }

    @Override
    public Page<TrialClass> list(Boolean enabled, Pageable pageable) {
        TrialClass trialClass = new TrialClass();
        if (enabled != null) {
            trialClass.setEnabled(enabled);
        }
        return trialClassDao.findAll(Example.of(trialClass), pageable);
    }

    @Override
    public List<TrialClass> listByMonth() {
        LocalDate day = LocalDate.now();
        LocalDate start = day.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate end = day.with(TemporalAdjusters.lastDayOfMonth());
        return trialClassDao.findByDateBetweenAndEnabled(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant()), true);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(TrialClass trialClass) {
        TrialClass db = trialClassDao.findOne(trialClass.getId());
        db.setDate(trialClass.getDate());
        trialClassDao.save(db);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Integer id) {
        TrialClass db = trialClassDao.findOne(id);
        db.setEnabled(true);
        trialClassDao.save(db);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Integer id) {
        TrialClass db = trialClassDao.findOne(id);
        db.setEnabled(false);
        trialClassDao.save(db);
    }

    @Resource
    public void setTrialClassStudentDao(TrialClassStudentDao trialClassStudentDao) {
        this.trialClassStudentDao = trialClassStudentDao;
    }

    @Resource
    public void setTrialClassDao(TrialClassDao trialClassDao) {
        this.trialClassDao = trialClassDao;
    }
}
