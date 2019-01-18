package com.demo2.user.service;

import com.demo2.common.Config;
import com.demo2.common.Digests;
import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import com.demo2.label.dao.LabelDao;
import com.demo2.label.model.Label;
import com.demo2.mini.dao.UserWordDao;
import com.demo2.mini.service.IUserWordService;
import com.demo2.user.dao.UserDao;
import com.demo2.user.model.Role;
import com.demo2.user.model.User;
import com.demo2.user.model.UserDto;
import com.demo2.user.model.UserQuery;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.service.IWordService;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UserService implements IUserService {
    @Resource
    private UserDao userDao;
    @Resource
    private Config config;
    @Resource
    private LabelDao labelDao;
    @Resource
    private UserWordDao userWordDao;
    @Resource
    private IWordService wordService;
    @Resource
    private IUserWordService userWordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User create(User user) throws AppException {
        if (user.getOpenid() != null) {
            if (userDao.findByOpenid(user.getOpenid()) != null) {
                return null;
            }
        }
        if (user.getPhone() != null) {
            if (userDao.findByPhone(user.getPhone()) != null) {
                throw new AppException(MessageCode.USER_PHONE_EXIST_ERROR, user.getPhone());
            }
        }
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        encryptPassword(user);
        user.setActive(true);
        user.setCreatedDate(new Date());
        user.setRole(Role.USER);
        return userDao.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(User user) {
        User toSave =find(user.getId());
        toSave.setEmail(user.getEmail());
        if (StringUtils.isNotBlank(user.getPhone())) {
            toSave.setPhone(user.getPhone());
        }
        String nickName = null;
        try {
            nickName = URLEncoder.encode(user.getNickname(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        toSave.setNickname(nickName);
        toSave.setAvatar(user.getAvatar());
        toSave.setCountry(user.getCountry());
        toSave.setProvince(user.getProvince());
        toSave.setCity(user.getCity());
        toSave.setLanguage(user.getLanguage());
        toSave.setBirthday(user.getBirthday());
        toSave.setLabelIds(user.getLabelIds());
        toSave.setChineseLevel(user.getChineseLevel());
        toSave.setGender(user.getGender());
        userDao.save(toSave);
    }

    @Override
    public Page<User> list(Pageable pageable, UserQuery userQuery) {
        Page<User> dbUser;
        if (userQuery != null) {
            String nickNameOrPhone = userQuery.getNikeOrPhone();
            Date start = userQuery.getStart();
            Date end = userQuery.getEnd();
            Specification specification = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotBlank(nickNameOrPhone)) {
                    predicates.add(cb.like(root.get("nickName"), "%" + nickNameOrPhone + "%"));
                }
                if (start != null && end != null) {
                    predicates.add(cb.between(root.get("updateDate"), start, end));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            };
            dbUser = userDao.findAll(specification, pageable);
        } else {
            dbUser = userDao.findAll(pageable);
        }

        List<User> userList = dbUser.getContent();
        for (User user : userList) {
            String nickName = user.getNickname();
            if (StringUtils.isNotBlank(nickName)) {
                try {
                    nickName = URLDecoder.decode(nickName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            user.setNickname(nickName);
        }
        Page<User> userPage = new PageImpl<>(userList, pageable, dbUser.getTotalElements());
        return userPage;
    }

    @Override
    public User findByOpenid(String openid) {
        return userDao.findByOpenid(openid);
    }

    @Override
    public User findByPhone(String phone) {
        return userDao.findByPhone(phone);
    }

    @Override
    public void updatePassword(Integer id, String oldPassword, String password) throws AppException {
        User user = find(id);

        String hashPassword = hashString(oldPassword, user.getSalt());
        if (!user.getPassword().equals(hashPassword)) {
            throw new AppException(MessageCode.USER_OLD_PASSWORD_ERROR);
        }

        user.setPassword(hashString(password, user.getSalt()));
        userDao.save(user);
    }


    @Override
    public User find(Integer id) {
        User user = userDao.findOne(id);
        String nickName = user.getNickname();
        if (StringUtils.isNotBlank(nickName)) {
            try {
                nickName = URLDecoder.decode(nickName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        user.setNickname(nickName);
        return user;
    }

    @Override
    public UserDto findUserDto(Integer userId) throws AppException {
        User user = find(userId);
        if (user != null) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            String nickName = userDto.getNickname();
            if (StringUtils.isNotBlank(nickName)) {
                try {
                    nickName = URLDecoder.decode(nickName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            userDto.setNickname(nickName);
            // 标签
            if (StringUtils.isNoneBlank(user.getLabelIds())) {
                String[] ids = user.getLabelIds().split(",");
                List<String> labelNames = new ArrayList<>(ids.length);
                for (String labelId : ids) {
                    Label label = labelDao.findOne(Integer.valueOf(labelId));
                    labelNames.add(label.getName());
                }
                userDto.setLabelNames(labelNames);
            }
            // 学习概况
            //总计学习天数
            List<Object[]> objects = userWordDao.listStudyDays(userId);
            int learnTotalDays = objects.size();
            userDto.setLearnTotalDays(learnTotalDays);
            // 连续学习天数
            userDto.setContinueLearnDays(5); //TODO
            //各个等级完成度
            List<Double> levelPercent = new ArrayList<>();
            DecimalFormat df = new DecimalFormat("0.00");
            long wordCnt = wordService.countByLevel(ChineseLevel.HSK1);
            long learnedWordCnt = userWordService.countLearnedByLevel(userId, ChineseLevel.HSK1, true);
            Double level1Percent = 0d;
            if (wordCnt != 0) {
                level1Percent = (double) learnedWordCnt / wordCnt * 100;
                level1Percent = Double.valueOf(df.format(level1Percent));
            }
            levelPercent.add(level1Percent);
            wordCnt = wordService.countByLevel(ChineseLevel.HSK2);
            learnedWordCnt = userWordService.countLearnedByLevel(userId, ChineseLevel.HSK2, true);
            Double level2Percent = 0d;
            if (wordCnt != 0) {
                level2Percent = (double) learnedWordCnt / wordCnt * 100;
                level2Percent = Double.valueOf(df.format(level2Percent));
            }
            levelPercent.add(level2Percent);
            wordCnt = wordService.countByLevel(ChineseLevel.HSK3);
            learnedWordCnt = userWordService.countLearnedByLevel(userId, ChineseLevel.HSK3, true);
            Double level3Percent = 0d;
            if (wordCnt != 0) {
                level3Percent = (double) learnedWordCnt / wordCnt * 100;
                level3Percent = Double.valueOf(df.format(level3Percent));
            }
            levelPercent.add(level3Percent);
            wordCnt = wordService.countByLevel(ChineseLevel.HSK4);
            learnedWordCnt = userWordService.countLearnedByLevel(userId, ChineseLevel.HSK4, true);
            Double level4Percent = 0d;
            if (wordCnt != 0) {
                level4Percent = (double) learnedWordCnt / wordCnt * 100;
                level4Percent = Double.valueOf(df.format(level4Percent));
            }
            levelPercent.add(level4Percent);
            wordCnt = wordService.countByLevel(ChineseLevel.HSK5);
            learnedWordCnt = userWordService.countLearnedByLevel(userId, ChineseLevel.HSK5, true);
            Double level5Percent = 0d;
            if (wordCnt != 0) {
                level5Percent = (double) learnedWordCnt / wordCnt * 100;
                level5Percent = Double.valueOf(df.format(level5Percent));
            }
            levelPercent.add(level5Percent);
            wordCnt = wordService.countByLevel(ChineseLevel.HSK6);
            learnedWordCnt = userWordService.countLearnedByLevel(userId, ChineseLevel.HSK6, true);
            Double level6Percent = 0d;
            if (wordCnt != 0) {
                level6Percent = (double) learnedWordCnt / wordCnt * 100;
                level6Percent = Double.valueOf(df.format(level6Percent));
            }
            levelPercent.add(level6Percent);
            userDto.setLevelPercent(levelPercent);

            return userDto;
        } else {
            throw new AppException(MessageCode.USER_NOT_EXIST_ERROR);
        }
    }

    private void encryptPassword(User user) {
        byte[] salt = Digests.generateSalt(config.getSaltSize());
        user.setSalt(Hex.encodeHexString(salt));

        byte[] hashPassword = Digests.sha1(user.getPassword().getBytes(), salt, config.getHashIterations());
        user.setPassword(Hex.encodeHexString(hashPassword));
    }

    private void checkPhone(Integer userId, String phone) throws AppException {
        User userByPhone = userDao.findByPhone(phone);
        if (userByPhone != null && !userByPhone.getId().equals(userId)) {
            throw new AppException(MessageCode.USER_PHONE_EXIST_ERROR, phone);
        }
    }

    /**
     * 重置密码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String phone, String newPassword) throws AppException {
        if (newPassword.isEmpty()) {
            throw new AppException(MessageCode.USER_PASSWORD_BLANK_ERROR);
        }
        User toSave = userDao.findByPhone(phone);
        toSave.setPassword(hashString(newPassword, toSave.getSalt()));
        userDao.save(toSave);
    }

    private String hashString(String password, String salt) throws AppException {
        try {
            return Hex.encodeHexString(Digests.sha1(password.getBytes(), Hex.decodeHex(salt.toCharArray()), config.getHashIterations()));
        } catch (DecoderException e) {
            throw new AppException(MessageCode.HEX_DECODE_EXCEPTION_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disable(Integer userId) {
        User user = find(userId);
        user.setActive(false);
        userDao.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Integer userId) {
        User user = find(userId);
        user.setActive(true);
        userDao.save(user);
    }
}
