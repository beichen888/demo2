package com.demo2.mini.service;

import com.demo2.common.DateUtil;
import com.demo2.mini.dao.UserScoreDao;
import com.demo2.mini.model.UserScore;
import com.demo2.mini.model.UserScoreConfig;
import com.demo2.user.dao.UserDao;
import com.demo2.user.model.User;
import com.demo2.user.model.UserLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/25.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserScoreService implements IUserScoreService {
    @Resource
    private UserScoreDao userScoreDao;
    @Resource
    private UserDao userDao;
    private static final Logger logger = LoggerFactory.getLogger(UserScoreService.class);

    @Override
    public UserScore save(Integer userId, String type) {
        int score = 0;
        if ("share".equals(type)) {
            score = UserScoreConfig.SHARE;
        }
        if ("finish".equals(type)) {
            score = UserScoreConfig.FINISH_LEARN;
        }
        if ("feedback".equals(type)) {
            score = UserScoreConfig.FEEDBACK;
        }
        UserScore db = findTodayUserPoint(userId);
        if (db == null) {
            UserScore userPoint = new UserScore();
            userPoint.setUserId(userId);
            userPoint.setScore(score);
            userPoint.setCreateDate(new Date());
            userScoreDao.save(userPoint);
        } else {
            if (db.getScore() + score > UserScoreConfig.PER_DAY_MAX) {
                db.setScore(UserScoreConfig.PER_DAY_MAX);
            } else {
                db.setScore(db.getScore() + score);
            }
            userScoreDao.save(db);
        }
        updateUserLevel(userId);
        return null;
    }

    @Override
    public int findUserTotalScore(Integer userId) {
        List<UserScore> points = userScoreDao.findUserScoresByUserIdEquals(userId);
        UserScore userPoint = new UserScore();
        userPoint.setUserId(userId);
        int pointTotal = 0;
        for (UserScore point : points) {
            pointTotal += point.getScore();
        }
        return pointTotal;
    }

    @Override
    public List<UserScore> findAddScoreDaysByUserId(Integer userId) {
        return userScoreDao.findUserScoresByUserIdEqualsAndScoreGreaterThan(userId, 0);
    }

    /**
     * 若打卡中断，则当天不减少经验，从第二天开始，每天减少2点经验，直到重新开始打卡或者经验值为0
     * 定时任务，夜里0点5份之后执行
     */
    @Override
    @Scheduled(cron = "0 5 0 * * ? ")
    public void computeScoreByDay() {
        logger.info("用户分数计算定时任务启动....");
        Date from = DateUtil.getPreDayFirstSecond();
        Date end = DateUtil.getPreDayLastSecond();
        List<Object[]> userLst = userScoreDao.findUserScoreLst();
        for (Object[] objects : userLst) {
            Integer userId = Integer.valueOf(objects[0].toString());
            // 查询前一天打卡记录
            UserScore userScore = userScoreDao.findUserPointByUserIdEqualsAndCreateDateBetween(userId, from, end);
            //前一天没有打款记录,减掉2分
            if (userScore == null) {
                Integer total = Integer.valueOf(objects[1].toString());
                int reduce = (total - 2 > 0) ? -2 : total * -1;
                logger.info("计算用户id：" + objects[0] + "的减少分数为:" + reduce);
                UserScore userPoint = new UserScore();
                userPoint.setUserId(userId);
                userPoint.setScore(reduce);
                userPoint.setCreateDate(new Date());
                userScoreDao.save(userPoint);
                updateUserLevel(userId);
            }
        }
        logger.info("用户分数计算定时任务结束....");
    }

    private UserScore findTodayUserPoint(Integer userId) {

        Date from = DateUtil.getTodayFirstSecond();

        Date end = DateUtil.getTodayLastSecond();

        return userScoreDao.findUserPointByUserIdEqualsAndCreateDateBetween(userId, from, end);
    }

    /**
     * 更新用户等级
     * @param userId 用户id
     */
    private void updateUserLevel(Integer userId) {
        User user = userDao.findOne(userId);
        if (user != null) {
            int score = findUserTotalScore(userId);
            if (score < 15) {
                user.setUserLevel(UserLevel.EMPTY);
            } else if (score < 90) {
                user.setUserLevel(UserLevel.ONE);
            } else if (score < 300) {
                user.setUserLevel(UserLevel.TWO);
            } else {
                user.setUserLevel(UserLevel.THREE);
            }
            userDao.save(user);
        }
    }
}
