package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.WordKeep;
import com.demo2.word.model.ChineseLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by miguo on 2018/6/13.
 */
public interface WordKeepDao extends BaseJpaRepository<WordKeep> {

    WordKeep findWordKeepByUserIdAndWordId(Integer userId, Integer wordId);

    void deleteByUserIdAndWordId(Integer userId, Integer wordId);

    Page<WordKeep> findWordKeepsByUserIdEquals(Integer userId, Pageable pageable);

    Page<WordKeep> findWordKeepsByUserIdEqualsAndLevelEquals(Integer userId, ChineseLevel level, Pageable pageable);

    List<WordKeep> findWordKeepsByUserIdEquals(Integer userId);
}
