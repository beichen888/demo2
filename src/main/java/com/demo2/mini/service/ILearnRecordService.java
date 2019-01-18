package com.demo2.mini.service;

import com.demo2.common.exception.AppException;
import com.demo2.mini.model.LearnRecord;

import java.util.List;
import java.util.Map;

public interface ILearnRecordService {

    LearnRecord create(Integer userId, boolean isPractice);

    /**
     * 用前一轮错误的题目生成练习题目
     * @param userId
     * @param preRecordId
     * @return
     */
    Map<String,Object> createPracticeFromPreRecord(Integer userId, Integer preRecordId) throws AppException;

    Map<String,Object> practiceFromKeep(Integer userId,Integer[] wordIds) throws AppException;

    LearnRecord findById(Integer id);

    LearnRecord findUnFinishByUserId(Integer userId);

    LearnRecord update(LearnRecord record);

    List<LearnRecord> findAllFinishByUserId(Integer userId);

    Map startTest(Integer userId) throws AppException;
}
