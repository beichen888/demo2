package com.demo2.question.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.question.model.Question;
import com.demo2.question.model.QuestionType;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/16.
 */
public interface QuestionDao extends BaseJpaRepository<Question> {


    Question findQuestionByWordIdEqualsAndTypeEquals(Integer wordId, QuestionType type);

    List<Question> findQuestionsByWordIdEquals(Integer wordId);
    @Query(value = "SELECT * FROM question WHERE word_id = ?1 ORDER BY rand() limit ?2",nativeQuery = true)
    List<Question> findRandomQuestionByWordId(Integer wordId,int limit);

    @Query(value = "SELECT * FROM question WHERE type<>'SIX' AND type<>'THREE' AND word_id = ?1 ORDER BY rand() limit ?2",nativeQuery = true)
    List<Question> findRandomQuestionByWordIdAndTypeNotThree(Integer wordId,int limit);

    @Query(value = "SELECT * FROM question WHERE type<>'SIX' AND type<>'FOUR' AND word_id = ?1 ORDER BY rand() limit ?2",nativeQuery = true)
    List<Question> findRandomQuestionByWordIdAndTypeNotFour(Integer wordId,int limit);

    List<Question> findQuestionsByCreatedDateBetween(Date from,Date end);
}
