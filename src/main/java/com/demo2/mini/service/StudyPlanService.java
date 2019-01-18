package com.demo2.mini.service;

import com.demo2.mini.dao.StudyPlanDao;
import com.demo2.mini.model.StudyPlan;
import com.demo2.user.dao.UserDao;
import com.demo2.user.model.User;
import com.demo2.word.dao.WordDao;
import com.demo2.word.model.Word;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/7.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StudyPlanService implements IStudyPlanService {
    @Resource
    private StudyPlanDao studyPlanDao;

    @Resource
    private WordDao wordDao;
    @Resource
    private UserDao userDao;

    @Resource
    private IUserWordService userWordService;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public StudyPlan findByUserId(Integer userId) {
        return studyPlanDao.findStudyPlanByUserIdEquals(userId);
    }

    @Override
    public StudyPlan save(StudyPlan studyPlan) {
        StudyPlan db = studyPlanDao.findStudyPlanByUserIdEquals(studyPlan.getUserId());
        if (db == null) {
            studyPlan.setCreatedDate(new Date());
            studyPlan.setUpdateDate(new Date());
        } else {
            //更改计划
            studyPlan.setId(db.getId());
            studyPlan.setCreatedDate(db.getCreatedDate());
            studyPlan.setUpdateDate(new Date());
        }
        //将UserWord表数据填充起来
        List<Word> wordList = wordDao.findByLevelOrderByPriority(studyPlan.getChineseLevel().name());
        for (Word word : wordList) {
            userWordService.create(studyPlan.getUserId(), word);
        }

        //更新用户当前学习等级
        User user = userDao.findOne(studyPlan.getUserId());
        user.setChineseLevel(studyPlan.getChineseLevel());
        userDao.save(user);
        return studyPlanDao.save(studyPlan);
    }

    @Override
    public void delete(Integer id) {

    }
}
