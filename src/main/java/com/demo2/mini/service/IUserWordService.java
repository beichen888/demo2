package com.demo2.mini.service;

import com.demo2.mini.model.UserWord;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;

import java.util.List;

/**
 * Created by miguo on 2018/6/29.
 */
public interface IUserWordService {
    List<UserWord> findLearnedLst(Integer userId, ChineseLevel level);

    List<UserWord> findTodayLearned(Integer userId);

    UserWord create(Integer userId,Word word);

    UserWord setLearned(Integer userId, Integer wordId);

    long countLearnedByLevel(Integer userId, ChineseLevel level,Boolean learned);
}
