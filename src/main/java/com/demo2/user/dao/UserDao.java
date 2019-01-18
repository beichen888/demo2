package com.demo2.user.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.user.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserDao extends BaseJpaRepository<User> {

    User findByPhone(String phone);

    User findByOpenid(String openid);

    /**
     * 取得今日新增人数
     *
     * @return 今日新增人数
     */
    @Query(value = "SELECT COUNT(0) FROM USERS WHERE DATE_FORMAT(created_date,'%Y-%m-%d') = curdate()", nativeQuery = true)
    Integer getTodayAddCnt();

    /**
     * 取得某天以内的每天用户量
     *
     * @param limitCnt
     * @return
     */
    @Query(value = "SELECT DATE_FORMAT(created_date,'%Y-%m-%d') AS createDate, ( SELECT SUM(1) FROM users AS user1 WHERE user1.created_date <= user2.created_date ) AS total FROM users AS user2 GROUP BY createDate ORDER BY createDate DESC limit ?1", nativeQuery = true)
    List<Object[]> getUserCntByDay(@Param("limitCnt") Integer limitCnt);

}
