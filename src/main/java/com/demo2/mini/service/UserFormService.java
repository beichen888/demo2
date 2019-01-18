package com.demo2.mini.service;

import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import com.demo2.mini.dao.UserFormDao;
import com.demo2.mini.model.UserForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/27.
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class UserFormService implements IUserFormService {

    @Resource
    private UserFormDao userFormDao;

    @Override
    public UserForm save(Integer userId, String formId) {
        //删除过期form
        Date date = get6DaysAgoTime();
        userFormDao.deleteAllByCreatedDateBefore(date);
        UserForm form = new UserForm();
        form.setUserId(userId);
        form.setFormId(formId);
        form.setCreatedDate(new Date());
        return userFormDao.save(form);
    }

    @Override
    public List<UserForm> findByUserId(Integer userId) {
        return userFormDao.findUserFormsByUserIdEquals(userId);
    }

    @Override
    public String getFirstFormByUserId(Integer userId) throws AppException {
        UserForm userForm = userFormDao.findFirstByUserIdEqualsOrderById(userId);
        if (userForm == null) {
            throw new AppException(MessageCode.FORM_ID_NOT_EXIST_ERROR, userId);
        }
        userFormDao.delete(userForm);
        return userForm.getFormId();
    }

    private Date get6DaysAgoTime() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - 6);
        return calendar.getTime();
    }
}
