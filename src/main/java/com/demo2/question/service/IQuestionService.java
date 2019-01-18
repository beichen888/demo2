package com.demo2.question.service;

import com.demo2.question.model.Question;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/16.
 */
public interface IQuestionService {

    Page<Question> listByPage(Pageable pageable, ChineseLevel level, String keyword, Date start, Date end);

    boolean create(Word word);

    Question findById(Integer id);

    boolean update(Question question);

    List<Question> findByWordId(Integer userId);
}
