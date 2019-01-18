package com.demo2.word.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.word.model.Word;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WordDao extends BaseJpaRepository<Word> {

    Word findWordByVocabularyEquals(String vocabulary);

    /**
     * 根据分组随机取得图片code
     *
     * @param imageGroup 分组
     * @param limit      条数
     * @return
     */
    @Query(value = "SELECT word.image_path FROM word where word.image_group= ?1 AND word.id <> ?2 AND  level = ?3 ORDER BY rand() limit ?4", nativeQuery = true)
    List<String> findImagesByImageGroup(String imageGroup, Integer wordId, String level, int limit);

    @Query(value = "SELECT * FROM word where word.level= ?1 AND word.id <> ?2 ORDER BY rand() limit ?3", nativeQuery = true)
    List<Word> findWordsByLevel(String level, Integer wordId, int limit);

    @Query(value = "SELECT * FROM word where level = ?1 order by priority ASC", nativeQuery = true)
    List<Word> findByLevelOrderByPriority(String level);


}
