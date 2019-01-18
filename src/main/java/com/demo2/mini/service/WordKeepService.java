package com.demo2.mini.service;

import com.demo2.common.Chinese2Pinyin;
import com.demo2.common.ChineseCharacterTool;
import com.demo2.mini.dao.WordKeepDao;
import com.demo2.mini.model.WordKeep;
import com.demo2.word.dao.WordDao;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by miguo on 2018/6/13.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WordKeepService implements IWordKeepService {

    @Resource
    private WordKeepDao wordKeepDao;

    @Resource
    private WordDao wordDao;

    @Override
    public WordKeep findByUserIdAndWordId(Integer userId, Integer wordId) {
        WordKeep wordKeep = wordKeepDao.findWordKeepByUserIdAndWordId(userId, wordId);
        if (wordKeep != null) {
            Word word = wordDao.findOne(wordKeep.getWordId());
            wordKeep.setWord(word);
        }
        return wordKeep;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordKeep save(WordKeep wordKeep) {
        WordKeep db = findByUserIdAndWordId(wordKeep.getUserId(), wordKeep.getWordId());
        if (db == null) {
            Word word = wordDao.findOne(wordKeep.getWordId());
            if (word != null) {
                // 拼音首字母
                String chineseChar = ChineseCharacterTool.getChineseCharacter(word.getVocabulary());
                String pinyin = Chinese2Pinyin.getAllFirstLetter(chineseChar);
                wordKeep.setPinyin(pinyin);
                wordKeep.setLevel(word.getLevel());
                wordKeep.setCreatedDate(new Date());
                db = wordKeepDao.save(wordKeep);
            }
        }
        return db;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIdAndWordId(Integer userId, Integer wordId) {
        wordKeepDao.deleteByUserIdAndWordId(userId, wordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readWord(Integer userId, Integer wordId) {
        WordKeep wordKeep = findByUserIdAndWordId(userId, wordId);
        if (wordKeep != null) {
            wordKeep.setIsNew(false);
            wordKeepDao.save(wordKeep);
        }
    }

    @Override
    public Boolean isKept(Integer userId, Integer wordId) {
        return wordKeepDao.findWordKeepByUserIdAndWordId(userId, wordId) != null;
    }

    @Override
    public Page<WordKeep> listByUserId(Pageable pageable, Integer userId, ChineseLevel level) {
        Page<WordKeep> list;
        if (level != null) {
            list = wordKeepDao.findWordKeepsByUserIdEqualsAndLevelEquals(userId, level, pageable);
        } else {
            list = wordKeepDao.findWordKeepsByUserIdEquals(userId, pageable);
        }
        for (WordKeep wordKeep : list) {
            if (wordKeep != null) {
                Word word = wordDao.findOne(wordKeep.getWordId());
                wordKeep.setWord(word);
            }
        }
        return list;
    }
}
