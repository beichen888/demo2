package com.demo2.mini.service;

import com.demo2.mini.dao.UserActionDao;
import com.demo2.mini.model.UserAction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/28.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserActionService implements IUserActionService {

    @Resource
    private UserActionDao userActionDao;

    @Override
    public UserAction save(UserAction userAction) {
        userAction.setCreatedDate(new Date());
        return userActionDao.save(userAction);
    }

    @Override
    public List<UserAction> find(Integer userId, String action) {
        return userActionDao.findUserActionByUserIdEqualsAndActionEquals(userId, action);
    }
}
