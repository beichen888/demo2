package com.demo2.user.service;

import com.demo2.common.exception.AppException;
import com.demo2.user.model.User;
import com.demo2.user.model.UserDto;
import com.demo2.user.model.UserQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    User create(User user) throws AppException;

    Page<User> list(Pageable pageable, UserQuery userQuery);

    void update(User user);

    User find(Integer id);

    UserDto findUserDto(Integer userId) throws AppException;

    User findByOpenid(String openid);

    User findByPhone(String emailOrPhone);

    void updatePassword(Integer id, String oldPassword, String password) throws AppException;

    void resetPassword(String phone, String password) throws AppException;

    void disable(Integer id);

    void enable(Integer id);
}
