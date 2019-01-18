package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.UserScore;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/25.
 */
public interface UserScoreDao extends BaseJpaRepository<UserScore> {

    UserScore findUserPointByUserIdEqualsAndCreateDateBetween(Integer userId, Date from, Date end);

    List<UserScore> findUserScoresByUserIdEqualsAndScoreGreaterThan(Integer userId, int score);

    List<UserScore> findUserScoresByUserIdEquals(Integer userId);

    @Query(value = "SELECT DISTINCT user_id, SUM(score)  FROM user_score GROUP BY user_id ORDER BY user_id", nativeQuery = true)
    List<Object[]> findUserScoreLst();

}
