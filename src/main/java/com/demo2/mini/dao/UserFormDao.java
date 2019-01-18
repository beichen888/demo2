package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.UserForm;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/27.
 */
public interface UserFormDao extends BaseJpaRepository<UserForm> {

    List<UserForm> findUserFormsByUserIdEquals(Integer userId);

    UserForm findFirstByUserIdEqualsOrderById(Integer userId);

    void deleteAllByCreatedDateBefore(Date date);

}
