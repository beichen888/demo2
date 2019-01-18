package com.demo2.mini.dao;

import com.demo2.common.BaseJpaRepository;
import com.demo2.mini.model.LearnRecord;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LearnRecordDao extends BaseJpaRepository<LearnRecord>{

    LearnRecord findLearnRecordByUserIdEqualsAndFinishIsFalseAndPracticeIsFalse(Integer userId);

    LearnRecord findLearnRecordByUserIdEqualsAndCreatedDateBetweenAndPracticeIsFalseAndFinishIsFalse(Integer userId, Date from, Date end);

    List<LearnRecord> findLearnRecordByUserIdEqualsAndFinishIsTrue(Integer userId);

    @Query(value = "SELECT * FROM learn_record WHERE practice = 0 AND user_id=?1 ORDER BY id desc limit 1",nativeQuery = true)
    LearnRecord findLastLearnRecordByUserId(Integer userId);

}
