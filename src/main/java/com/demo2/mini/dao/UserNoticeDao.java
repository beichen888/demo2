package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.UserNotice;

/**
 * Created by miguo on 2018/6/25.
 */
public interface UserNoticeDao extends BaseJpaRepository<UserNotice> {
    UserNotice findByUserIdEquals(Integer userId);

}
