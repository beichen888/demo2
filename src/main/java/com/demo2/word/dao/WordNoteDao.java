package com.demo2.word.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.word.model.WordNote;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by miguo on 2018/6/24.
 */
public interface WordNoteDao extends BaseJpaRepository<WordNote> {
    @Query(value = "SELECT * FROM word_note  WHERE user_id = ?1 and word_id=?2 ",nativeQuery = true)
    WordNote findByUserEqualsAndWordEquals(Integer userId,Integer wordId);
}
