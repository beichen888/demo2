package com.demo2.word.service;

import com.demo2.word.model.WordNote;

/**
 * Created by miguo on 2018/6/24.
 */
public interface IWordNoteService {

    WordNote save(WordNote wordNote);

    WordNote findByUserAndWord(Integer userId, Integer wordId);
}
