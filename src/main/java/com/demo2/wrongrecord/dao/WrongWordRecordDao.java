package com.demo2.wrongrecord.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.wrongrecord.model.WrongWordRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WrongWordRecordDao extends BaseJpaRepository<WrongWordRecord> {

    WrongWordRecord findById(Integer id);

    /**
     * 根据用户和单词取得数据
     * @param userId
     * @param wordId
     * @return
     */
    WrongWordRecord findByUserIdAndWordId(Integer userId,Integer wordId);
    /**
     * 根据用户列出错误列表
     * @param userId
     * @param pageable
     * @return
     */
    Page<WrongWordRecord> findWrongWordRecordsByUserId(Integer userId, Pageable pageable);

    /**
     * 根据单词分组列出错误列表
     * @return
     */
    @Query(value = "SELECT word.vocabulary, record.word_level, SUM(record.wrong_count) AS wrong_count,SUM(record.correct_count) AS correct_count FROM wrong_word_record record LEFT JOIN word ON record.word_id = word.id GROUP BY word_id ORDER BY wrong_count DESC",
        nativeQuery = true)
    List<Object[]> findListGroupByWord();

    /**
     * 根据级别分组列出错误列表
     * @return
     */
    @Query(value = "SELECT record.word_level, SUM(record.wrong_count) AS wrong_count,SUM(record.correct_count) AS correct_count FROM wrong_word_record record GROUP BY word_level ORDER BY wrong_count DESC ",
         nativeQuery = true)
    List<Object[]> findPageListGroupByWordLevel();

}
