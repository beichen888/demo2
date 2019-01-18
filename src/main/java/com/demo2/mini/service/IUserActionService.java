package com.demo2.mini.service;

import com.demo2.mini.model.UserAction;

import java.util.List;

/**
 * Created by miguo on 2018/6/28.
 */
public interface IUserActionService {
    UserAction save(UserAction userAction);

    List<UserAction> find(Integer userId, String action);
}
