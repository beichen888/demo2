package com.demo2.dashboard.service;

import com.demo2.common.ChineseCharacterTool;
import com.demo2.dashboard.model.WrongWord;
import com.demo2.user.dao.UserDao;
import com.demo2.wrongrecord.dao.WrongWordRecordDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class DashboardService implements IDashboardService {

    @Resource
    private UserDao userDao;
    @Resource
    private WrongWordRecordDao wrongWordRecordDao;

    @Override
    public Integer todayAddCnt() {
        return userDao.getTodayAddCnt();
    }

    @Override
    public Map<String, Integer> getUserCntByDay(Integer limitCnt) {
        List<Object[]> objects = userDao.getUserCntByDay(limitCnt);
        Map<String, Integer> retMap = new HashMap<>();
        for (Object[] object : objects) {
            retMap.put(object[0].toString(), Integer.valueOf(object[1].toString()));
        }
        return retMap;
    }

    @Override
    public Integer getTotalUserCnt() {
        return userDao.findAll().size();
    }

    @Override
    public List<WrongWord> getTop20WrongWord() {
        List<Object[]> objectList = wrongWordRecordDao.findListGroupByWord();
        List<WrongWord> ret = new ArrayList<>();
        for (Object[] objects : objectList) {
            WrongWord wrongWord = new WrongWord();
            String vocabulary = String.valueOf(objects[0]);
            vocabulary = ChineseCharacterTool.getChineseCharacter(vocabulary);
            wrongWord.setVocabulary(vocabulary);
            String level =  String.valueOf(objects[1]);
            wrongWord.setLevel(level);
            String wrongCnt =  String.valueOf(objects[2]);
            wrongWord.setWrongCnt(wrongCnt);
            ret.add(wrongWord);
            if (ret.size() >= 20) {
                break;
            }
        }
        return ret;
    }


}
