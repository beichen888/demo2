package com.demo2.mini.service;

import com.demo2.common.DateUtil;
import com.demo2.mini.dao.UserWordDao;
import com.demo2.mini.model.UserWord;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/29.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserWordService implements IUserWordService {

    @Resource
    private UserWordDao userWordDao;

    @Override
    public List<UserWord> findLearnedLst(Integer userId, ChineseLevel level) {
        return userWordDao.findUserWordsByUserIdEqualsAndLevelEqualsAndLearnedIsTrue(userId, level);
    }

    @Override
    public List<UserWord> findTodayLearned(Integer userId) {
        Date todayFirstSecond = DateUtil.getTodayFirstSecond();
        return userWordDao.findUserWordsByUserIdEqualsAndLearnedDateAfter(userId, todayFirstSecond);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord create(Integer userId, Word word) {
        UserWord userWord = new UserWord();
        userWord.setUserId(userId);
        userWord.setWordId(word.getId());
        UserWord db = userWordDao.findUserWordByUserIdAndWordId(userId, word.getId());
        if (db != null) {
            userWord.setId(db.getId());
        }
        userWord.setPriority(word.getPriority());
        userWord.setLevel(word.getLevel());
        userWord.setLearned(false);
        userWord.setCreateDate(new Date());
        save(userWord);
        return userWord;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserWord setLearned(Integer userId, Integer wordId) {
        UserWord userWord = userWordDao.findUserWordByUserIdAndWordId(userId, wordId);
        if (userWord != null && userWord.getId() != null) {
            userWord.setLearnedDate(new Date());
            userWord.setLearned(true);
            return save(userWord);
        }
        return null;
    }

    @Override
    public long countLearnedByLevel(Integer userId, ChineseLevel level, Boolean learned) {
        UserWord userWord = new UserWord();
        userWord.setUserId(userId);
        if (level != null) {
            userWord.setLevel(level);
        }
        userWord.setLearned(learned);
        return userWordDao.count(Example.of(userWord));
    }

    private UserWord save(UserWord userWord) {
//        Date createDate = userWord.getCreateDate();
//        Boolean learned = userWord.getLearned();
//        Date learnedDate = userWord.getLearnedDate();
//        String level = userWord.getLevel().name();
//        Integer priority = userWord.getPriority();
//        Boolean selected = userWord.getSelected();
//        Integer userId = userWord.getUserId();
//        Integer wordId = userWord.getWordId();
//        return userWordDao.create(createDate,learned,learnedDate,level,priority,selected,userId,wordId);
        return userWordDao.save(userWord);
    }
}
