package com.demo2.wrongrecord.service;

import com.demo2.wrongrecord.model.WrongWordRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * Created by miguo on 2018/6/9.
 */
public interface IWrongWordRecordService {

    Page<WrongWordRecord> findPageListByUserId(Integer userId,int page, int pageSize,Sort sort);

    WrongWordRecord findById(Integer id);

    Page<Map<String,Object>> findPageListGroupByWord(int page, int pageSize, Sort sort);

    Page<Map<String, Object>> findPageListGroupByLevel(int page, int pageSize, Sort sort);

    void addWrongCnt(Integer userId, Integer wordId);

    void addCorrectCnt(Integer userId, Integer wordId);
}
