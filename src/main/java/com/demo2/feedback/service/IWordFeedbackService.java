package com.demo2.feedback.service;

import com.demo2.feedback.model.WordFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IWordFeedbackService {

    void save(WordFeedback wordFeedback);

    WordFeedback find(Integer id);

    Page<WordFeedback> list(Pageable pageable,String keyword);
}
