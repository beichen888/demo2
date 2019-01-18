package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.UserWord;
import com.demo2.word.model.ChineseLevel;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by miguo on 2018/6/27.
 */
public interface UserWordDao extends BaseJpaRepository<UserWord> {

    @Query(value = "SELECT * FROM user_word WHERE learned = 0 AND priority IS NOT NULL AND user_id = ?1 AND level=?2 order by priority limit ?3", nativeQuery = true)
    List<UserWord> findTestWordsByPriorityIsNotNull(Integer userId, String level, int limit);

    @Query(value = "SELECT * FROM user_word WHERE learned = 0 AND priority IS NULL AND user_id = ?1 AND level=?2 order by rand() limit ?3", nativeQuery = true)
    List<UserWord> findTestWordsByPriorityIsNull(Integer userId, String level, int limit);

    @Query(value = "SELECT * FROM user_word WHERE user_id=?1 AND word_id=?2",nativeQuery = true)
    UserWord findUserWordByUserIdAndWordId(Integer userId, Integer wordId);

    @Query(value = "replace into user_word(create_date,learned,learned_date,level,priority,selected,user_id,word_id) values(?1,?2,?3,?4,?5,?6,?7,?8)",nativeQuery = true)
    UserWord create(Date createDate,Boolean learned,Date learnedDate,String level,Integer priority,Boolean selected,Integer userId,Integer wordId);

    List<UserWord> findUserWordsByUserIdEqualsAndLevelEqualsAndLearnedIsTrue(Integer userId, ChineseLevel level);

    List<UserWord> findUserWordsByUserIdEqualsAndLearnedDateAfter(Integer userId, Date date);

    @Query(value = "SELECT DISTINCT(learn_day) from (SELECT user_id,DATE_FORMAT(learned_date,'%Y%m%d') as learn_day FROM user_word where learned_date is not null and user_id=?1) as user_learn",nativeQuery = true)
    List<Object[]> listStudyDays(Integer userId);
}
