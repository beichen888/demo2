package com.demo2.mini.service;

import com.demo2.common.exception.AppException;
import com.demo2.mini.model.UserForm;

import java.util.List;

/**
 * Created by miguo on 2018/6/27.
 */
public interface IUserFormService {

    UserForm save(Integer userId,String formId);

    List<UserForm> findByUserId(Integer userId);

    String getFirstFormByUserId(Integer userId) throws AppException;
}
