package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.QuestionResult;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/19.
 */
public interface QuestionResultDao extends BaseJpaRepository<QuestionResult> {

    List<QuestionResult> findQuestionResultsByCorrectIsNotNullAndRecordIdEqualsOrderById(Integer recordId);

    List<QuestionResult> findQuestionResultsByCorrectIsNullAndRecordIdEqualsOrderById(Integer recordId);

    List<QuestionResult> findQuestionResultsByRecordIdEqualsOrderById(Integer recordId);

    @Query(value = "SELECT * FROM question_result where new_word=1 AND correct IS NOT NULL AND practice = 0 AND record_id = ?1 order by rand()", nativeQuery = true)
    List<QuestionResult> findNewWordQuestionResultsByRecordId(Integer recordId);

    @Query(value = "SELECT * FROM question_result where new_word=1 AND correct IS NOT NULL AND practice = 0 AND created_date <?1 order by rand() limit ?2", nativeQuery = true)
    List<QuestionResult> findLearnedNewWordQuestionResultsBefore(Date date, int limit);


    List<QuestionResult> findQuestionResultsByRecordIdEqualsAndCorrectIsFalseAndPracticeIsTrueAndNewWordIsTrue(Integer recordId);
}
