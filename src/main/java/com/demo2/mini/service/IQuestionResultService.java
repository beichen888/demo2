package com.demo2.mini.service;

import com.demo2.common.exception.AppException;
import com.demo2.mini.model.QuestionResult;
import com.demo2.word.model.Word;

import java.util.List;

/**
 * Created by miguo on 2018/6/19.
 */

public interface IQuestionResultService {

    QuestionResult save(QuestionResult result,Integer userId);

    List<QuestionResult> createPracticeResultByWord(Integer userId, Word word, Integer recordId, int limit) throws AppException;

    List<QuestionResult> findDoneByLearnRecordId(Integer learnRecordId);

    List<QuestionResult> findListByLearnRecordId(Integer learnRecordId);

    List<QuestionResult> findUnDoneByLearnRecordId(Integer learnRecordId);

    List<QuestionResult> findWrongPracticeResults(Integer learnRecordId);

    QuestionResult findById(Integer id);


}
