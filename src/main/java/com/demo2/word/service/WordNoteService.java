package com.demo2.word.service;

import com.demo2.mini.model.WordKeep;
import com.demo2.mini.service.IWordKeepService;
import com.demo2.word.dao.WordNoteDao;
import com.demo2.word.model.WordNote;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by miguo on 2018/6/24.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WordNoteService implements IWordNoteService {

    @Resource
    private WordNoteDao wordNoteDao;
    @Resource
    private IWordKeepService wordKeepService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WordNote save(WordNote wordNote) {
        WordNote db = findByUserAndWord(wordNote.getUserId(), wordNote.getWordId());
        if (db != null) {
            wordNote.setId(db.getId());
        }
        wordNote.setCreateDate(new Date());
        String note = wordNote.getNote();
        try {
            note = URLEncoder.encode(note, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        wordNote.setNote(note);
        wordNoteDao.save(wordNote);
        //收藏单词
        WordKeep wordKeep = new WordKeep();
        wordKeep.setUserId(wordNote.getUserId());
        wordKeep.setWordId(wordNote.getWordId());
        wordKeepService.save(wordKeep);
        return wordNote;
    }

    @Override
    public WordNote findByUserAndWord(Integer userId, Integer wordId) {
        WordNote wordNote = wordNoteDao.findByUserEqualsAndWordEquals(userId, wordId);
        if (wordNote != null) {
            String note = wordNote.getNote();
            try {
                note = URLDecoder.decode(note, "utf-8");
                wordNote.setNote(note);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return wordNote;
    }
}
