package com.demo2.wrongrecord.service;

import com.demo2.common.ChineseCharacterTool;
import com.demo2.user.model.User;
import com.demo2.word.dao.WordDao;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import com.demo2.wrongrecord.dao.WrongWordRecordDao;
import com.demo2.wrongrecord.model.WrongWordRecord;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by miguo on 2018/6/9.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WrongWordRecordService implements IWrongWordRecordService {

    @Resource
    private WrongWordRecordDao wrongWordRecordDao;
    @Resource
    private WordDao wordDao;

    @Override
    public Page<WrongWordRecord> findPageListByUserId(Integer userId, int page, int pageSize, Sort sort) {
        Pageable pageable = new PageRequest(page, pageSize, sort);
        Page<WrongWordRecord> recordPage = wrongWordRecordDao.findWrongWordRecordsByUserId(userId, pageable);
        return recordPage;
    }

    @Override
    public WrongWordRecord findById(Integer id) {
        return wrongWordRecordDao.findById(id);
    }

    @Override
    public Page<Map<String, Object>> findPageListGroupByWord(int page, int pageSize, Sort sort) {
        Pageable pageable = new PageRequest(page, pageSize, sort);

        List<Object[]> objectList = wrongWordRecordDao.findListGroupByWord();

        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] objects : objectList) {
            Map<String, Object> map = new HashMap<>();
            String vocabulary = String.valueOf(objects[0]);
            vocabulary = ChineseCharacterTool.getChineseCharacter(vocabulary);
            map.put("vocabulary", vocabulary);
            map.put("wordLevel", objects[1]);
            Integer wrongCnt = Integer.valueOf(String.valueOf(objects[2]));
            Integer correctCnt = Integer.valueOf(String.valueOf(objects[3]));
            map.put("wrongWordCount", objects[2]);
            double percent = new BigDecimal((float) wrongCnt / (wrongCnt + correctCnt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("wrongPercent", percent);
            list.add(map);
        }
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public Page<Map<String, Object>> findPageListGroupByLevel(int page, int pageSize, Sort sort) {
        Pageable pageable = new PageRequest(page, pageSize, sort);
        List<Object[]> objectList = wrongWordRecordDao.findPageListGroupByWordLevel();
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object[] objects : objectList) {
            Map<String, Object> map = new HashMap<>();
            map.put("wordLevel", objects[0]);
            Integer wrongCnt = Integer.valueOf(String.valueOf(objects[1]));
            Integer correctCnt = Integer.valueOf(String.valueOf(objects[2]));
            map.put("wrongWordCount", objects[1]);
            double percent = new BigDecimal((float) wrongCnt / (wrongCnt + correctCnt)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            map.put("wrongPercent", percent);
            list.add(map);
        }
        return new PageImpl<>(list, pageable, list.size());
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWrongCnt(Integer userId, Integer wordId) {
        WrongWordRecord wrongRecord = wrongWordRecordDao.findByUserIdAndWordId(userId, wordId);
        if (wrongRecord != null) {
            wrongRecord.setWrongCount(wrongRecord.getWrongCount() + 1);
        } else {
            wrongRecord = new WrongWordRecord();
            User user = new User();
            user.setId(userId);
            wrongRecord.setUser(user);
            Word word = wordDao.findOne(wordId);
            wrongRecord.setWord(word);
            wrongRecord.setWrongCount(1);
            wrongRecord.setWordLevel(word.getLevel());
        }
        wrongRecord.setCreatedDate(new Date());
        wrongRecord.setUpdateDate(new Date());
        Word word = wordDao.findOne(wordId);
        if (word != null) {
            ChineseLevel level = word.getLevel();
            wrongRecord.setWordLevel(level);
        }
        wrongWordRecordDao.save(wrongRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCorrectCnt(Integer userId, Integer wordId) {
        WrongWordRecord wrongRecord = wrongWordRecordDao.findByUserIdAndWordId(userId, wordId);
        if (wrongRecord != null) {
            wrongRecord.setCorrectCount(wrongRecord.getCorrectCount() + 1);
        } else {
            wrongRecord = new WrongWordRecord();
            User user = new User();
            user.setId(userId);
            wrongRecord.setUser(user);
            Word word = wordDao.findOne(wordId);
            wrongRecord.setWord(word);
            wrongRecord.setCorrectCount(1);
            wrongRecord.setWordLevel(word.getLevel());
        }
        wrongRecord.setCreatedDate(new Date());
        wrongRecord.setUpdateDate(new Date());
        wrongWordRecordDao.save(wrongRecord);
    }
}
