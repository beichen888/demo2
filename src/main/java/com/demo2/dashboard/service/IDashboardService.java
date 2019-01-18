package com.demo2.dashboard.service;

import com.demo2.dashboard.model.WrongWord;

import java.util.List;
import java.util.Map;


public interface IDashboardService {

    /**
     * 取得今日新增人数
     * @return 今日新增人数
     */
    Integer todayAddCnt();

    /**
     * 取得某天以内的每天用户量
     * @param limitCnt 最近多少天
     * @return 日期及用户量
     */
    Map<String,Integer> getUserCntByDay(Integer limitCnt);

    Integer getTotalUserCnt();

    List<WrongWord> getTop20WrongWord();
}
