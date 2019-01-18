package com.demo2.mini.service;

import com.demo2.mini.model.UserScore;

import java.util.List;

/**
 * Created by miguo on 2018/6/25.
 */
public interface IUserScoreService {

    UserScore save(Integer userId, String type);

    int findUserTotalScore(Integer userId);

    List<UserScore> findAddScoreDaysByUserId(Integer userId);

    /**
     * 若打卡中断，则当天不减少经验，从第二天开始，每天减少2点经验，直到重新开始打卡或者经验值为0
     */
    void computeScoreByDay();

}
