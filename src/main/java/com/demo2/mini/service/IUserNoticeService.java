package com.demo2.mini.service;

import com.demo2.common.exception.AppException;
import com.demo2.mini.model.UserNotice;

/**
 * Created by miguo on 2018/6/25.
 */
public interface IUserNoticeService {

    void save(UserNotice userNotice);

    UserNotice findByUserId(Integer userId);

    void sendNotice() throws AppException;
}
