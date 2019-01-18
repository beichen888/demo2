package com.demo2.mini.service;

import com.demo2.mini.model.WordKeep;
import com.demo2.word.model.ChineseLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by miguo on 2018/6/13.
 */
public interface IWordKeepService {

    WordKeep findByUserIdAndWordId(Integer userId, Integer wordId);

    WordKeep save(WordKeep wordKeep);

    void deleteByUserIdAndWordId(Integer userId, Integer wordId);

    void readWord(Integer userId, Integer wordId);

    Boolean isKept(Integer userId, Integer wordId);

    Page<WordKeep> listByUserId(Pageable pageable, Integer userId,ChineseLevel level);
}
