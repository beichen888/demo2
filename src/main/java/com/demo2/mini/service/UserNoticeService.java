package com.demo2.mini.service;

import com.demo2.common.*;
import com.demo2.common.exception.AppException;
import com.demo2.mini.dao.StudyPlanDao;
import com.demo2.mini.dao.UserNoticeDao;
import com.demo2.mini.model.StudyPlan;
import com.demo2.mini.model.TemplateData;
import com.demo2.mini.model.UserNotice;
import com.demo2.mini.model.WxTemplate;
import com.demo2.user.dao.UserDao;
import com.demo2.user.model.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 提醒
 */
@Service
public class UserNoticeService implements IUserNoticeService {
    @Resource
    private UserNoticeDao userNoticeDao;
    @Resource
    private Config config;
    @Resource
    private IMyAccessTokenService myAccessTokenService;
    @Resource
    private UserDao userDao;
    @Resource
    private StudyPlanDao studyPlanDao;
    @Resource
    private IUserFormService userFormService;

    private static Logger logger = LoggerFactory.getLogger(UserNoticeService.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(UserNotice userNotice) {
        UserNotice db = userNoticeDao.findByUserIdEquals(userNotice.getUserId());
        if (db != null) {
            userNotice.setId(db.getId());
        }
        userNoticeDao.save(userNotice);
    }

    @Override
    public UserNotice findByUserId(Integer userId) {
        return userNoticeDao.findByUserIdEquals(userId);
    }

    /**
     * 定时发送模板通知，每隔5分钟发起一次
     */
    @Override
    @Scheduled(cron = "0 0/5 * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void sendNotice() throws AppException {
        // 现在时间
        Date now = new Date();
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MINUTE, 5);
        //五分钟以后时间
        Date fiveMinuteAfter = nowTime.getTime();
        List<UserNotice> noticeList = userNoticeDao.findAll();
        for (UserNotice userNotice : noticeList) {
            String noticeTime = userNotice.getNoticeTime();
            if (StringUtils.isBlank(noticeTime)) {
                continue;
            }
            //提醒时间
            Integer hour = Integer.valueOf(noticeTime.substring(0, 2));
            Integer minute = Integer.valueOf(noticeTime.substring(3, 5));
            Calendar noticeCalendar = Calendar.getInstance();
            noticeCalendar.set(Calendar.HOUR_OF_DAY, hour);
            noticeCalendar.set(Calendar.MINUTE, minute);
            noticeCalendar.set(Calendar.SECOND, 0);
            Date noticeDate = noticeCalendar.getTime();
            // 发起提醒
            if (noticeDate.after(now) && noticeDate.before(fiveMinuteAfter)) {
                Integer userId = userNotice.getUserId();
                sendTemplateMessage(userId, noticeDate);
            }
        }
    }

    private String sendTemplateMessage(Integer userId, Date noticeDate) throws AppException {
        User user = userDao.findOne(userId);
        if (user == null || StringUtils.isBlank(user.getOpenid())) {
            return null;
        }
        String openId = user.getOpenid();
        String formId = userFormService.getFirstFormByUserId(userId);
        String accessToken = myAccessTokenService.getAccessToken();
        logger.info("sendNotice to user:" + user.getId() + " with token:" + JacksonUtil.toJson(accessToken) + ",formId:" + formId);
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=" + accessToken;
        String templateId = config.getWxTemplateId();
        WxTemplate wxTemplate = new WxTemplate();
        wxTemplate.setTouser(openId);
        wxTemplate.setTemplate_id(templateId);
        wxTemplate.setPage("pages/studyPlan/index");
        wxTemplate.setForm_id(formId);
        Map<String, TemplateData> templateDataMap = new HashMap<>();
        StudyPlan studyPlan = studyPlanDao.findStudyPlanByUserIdEquals(userId);
        Boolean learnChinese = studyPlan.getIsLearnChinese();
        if (learnChinese == null) {
            learnChinese = false;
        }
        // 日程主题
        TemplateData title = new TemplateData();
        title.setColor("#000000");
        title.setValue(getWxNoticeTitle(learnChinese));
        templateDataMap.put("keyword1", title);
        // 日程时间
        TemplateData date = new TemplateData();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = dateFormat.format(noticeDate);
        date.setValue(dateStr);
        templateDataMap.put("keyword2", date);

        // 备注
        TemplateData remark = new TemplateData();
        remark.setValue(getWxNoticeRemark(learnChinese));
        templateDataMap.put("keyword3", remark);

        wxTemplate.setData(templateDataMap);

        String content = JacksonUtil.toJson(wxTemplate);
        try {
            String wxResponse = HttpsUtils.post(url, content, "utf-8");
            AccessToken token = JacksonUtil.fromJson(wxResponse, AccessToken.class);
            logger.info("sendCompleteCourseMsg response:" + wxResponse);
            if (token != null && !token.getErrcode().equals(0)) {
                throw new AppException(MessageCode.MINI_GET_ACCESS_TOKEN_ERROR, wxResponse);
            }
        } catch (IOException ex) {
            throw new AppException();
        }
        return null;
    }

    private String getWxNoticeTitle(Boolean isLearnChinese) {
        if (isLearnChinese) {
            return "快来AceChinese开始今天的学习:)";
        } else {
            return "Ace Chinese vocab! It's time to start today's learning:)";
        }
    }

    private String getWxNoticeRemark(Boolean isLearnChinese) {
        if (isLearnChinese) {
            return "连续签到有惊喜哦！";
        } else {
            return "Keep learning for a secret gift!";
        }
    }
}
