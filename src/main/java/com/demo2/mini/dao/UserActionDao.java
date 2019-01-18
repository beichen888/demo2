package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.UserAction;

import java.util.List;

/**
 * Created by miguo on 2018/6/28.
 */
public interface UserActionDao extends BaseJpaRepository<UserAction> {
    List<UserAction> findUserActionByUserIdEqualsAndActionEquals(Integer userId, String action);
}
