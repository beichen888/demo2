package com.demo2.dashboard.controller;

import com.demo2.common.*;
import com.demo2.common.exception.AppException;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.dashboard.model.WeAnalysisData;
import com.demo2.dashboard.model.WeUserTrendData;
import com.demo2.dashboard.model.WeUserTrendDetail;
import com.demo2.dashboard.model.WrongWord;
import com.demo2.dashboard.service.IDashboardService;
import com.demo2.mini.service.IMyAccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Auth
public class DashboardController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(DashboardController.class);
    @Resource
    private IDashboardService dashboardService;

    @Resource
    private IMyAccessTokenService myAccessTokenService;

    @GetMapping("/dashBoard")
    public Result getUserCnt() throws IOException, AppException {
        Map<String, Object> result = new HashMap<>(4);
        Integer todayAddCnt = dashboardService.todayAddCnt();
        result.put("todayAddCnt", todayAddCnt);
        Integer userTotalCnt = dashboardService.getTotalUserCnt();
        result.put("userTotalCnt", userTotalCnt);
        //前20个高发错误单词
        List<WrongWord> top20WrongWords = dashboardService.getTop20WrongWord();
        result.put("top20WrongWords", top20WrongWords);
        // 日趋势
        Map<String, Map<String, Integer>> userDailyTrend = getUserDailyTrend(7);
        result.put("userDailyTrend", userDailyTrend);
        //周趋势
        Map<String, Map<String, Integer>> userWeeklyTrend = getUserWeeklyTrend(4);
        result.put("userWeeklyTrend", userWeeklyTrend);
        //月趋势
        Map<String, Map<String, Integer>> userMonthlyTrend = getUserMonthlyTrend(4);
        result.put("userMonthlyTrend", userMonthlyTrend);
        return renderSuccess(result);
    }

    /**
     * 获取小程序新增或活跃用户的画像分布数据
     *
     * @param beginDate 开始日期 "2017-06-11"
     * @param endDate   结束日期，开始日期与结束日期相差的天数限定为0/6/29，分别表示查询最近1/7/30天数据，end_date允许设置的最大值为昨日 "2017-06-17"
     * @return 画像分布数据
     */
    private WeAnalysisData analysisUser(String beginDate, String endDate) throws AppException, IOException {
        String accessToken = myAccessTokenService.getAccessToken();
        String url = "https://api.weixin.qq.com/datacube/getweanalysisappiduserportrait?access_token=" + accessToken;
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("begin_date", beginDate);
        dateMap.put("end_date", endDate);
        String postContent = JacksonUtil.toJson(dateMap);
        String wxResponse = HttpsUtils.post(url, postContent, "utf-8");
        //logger.info(wxResponse);
        WeAnalysisData weAnalysisData = JacksonUtil.fromJson(wxResponse, WeAnalysisData.class);
        return weAnalysisData;
    }

    /**
     * 取得用户日趋势
     *
     * @param dayCnt 几天内的数据(一周内：7)
     * @return
     * @throws AppException
     */
    private LinkedHashMap<String, Map<String, Integer>> getUserDailyTrend(int dayCnt) throws AppException, IOException {
        String accessToken = myAccessTokenService.getAccessToken();
        String url = "https://api.weixin.qq.com/datacube/getweanalysisappiddailyvisittrend?access_token=" + accessToken;
        Map<String, Integer> currentDayTrendMap;
        LinkedHashMap<String, Map<String, Integer>> ret = new LinkedHashMap<>();
        Map<String, String> dateMap = new HashMap<>();
        for (int i = dayCnt; i >= 1; i--) {
            String beginDate = DateUtil.getPastDate(i);
            String endDate = DateUtil.getPastDate(i);
            dateMap.put("begin_date", beginDate);
            dateMap.put("end_date", endDate);
            String postContent = JacksonUtil.toJson(dateMap);
            String wxResponse = HttpsUtils.post(url, postContent, "utf-8");
            WeUserTrendData weUserTrendData = JacksonUtil.fromJson(wxResponse, WeUserTrendData.class);
            List<WeUserTrendDetail> WeUserTrendDetailLst = weUserTrendData.getList();
            currentDayTrendMap = new HashMap<>();
            if (WeUserTrendDetailLst != null && !WeUserTrendDetailLst.isEmpty()) {
                currentDayTrendMap.put("visit_pv", weUserTrendData.getList().get(0).getVisit_pv());//访问次数
                currentDayTrendMap.put("visit_uv", weUserTrendData.getList().get(0).getVisit_pv());// 访问人数
                currentDayTrendMap.put("visit_uv_new", weUserTrendData.getList().get(0).getVisit_uv_new());// 新用户数
            } else {
                currentDayTrendMap.put("visit_pv", 0);//访问次数
                currentDayTrendMap.put("visit_uv", 0);// 访问人数
                currentDayTrendMap.put("visit_uv_new", 0);// 新用户数
            }
            ret.put(beginDate, currentDayTrendMap);
        }
        return ret;
    }

    /**
     * 取得用户周趋势
     *
     * @param weekCnt 周数 默认4
     * @return
     * @throws AppException
     */
    private LinkedHashMap<String, Map<String, Integer>> getUserWeeklyTrend(int weekCnt) throws AppException, IOException {
        String accessToken = myAccessTokenService.getAccessToken();
        String url = "https://api.weixin.qq.com/datacube/getweanalysisappidweeklyvisittrend?access_token=" + accessToken;
        Map<String, String> dateMap = new HashMap<>();
        LinkedHashMap<String, Map<String, Integer>> ret = new LinkedHashMap<>();
        for (int i = weekCnt; i >= 1; i--) {
            String beginDate = DateUtil.getPastWeekMonday(i);
            String endDate = DateUtil.getPastWeekSunday(i);
            dateMap.put("begin_date", beginDate);
            dateMap.put("end_date", endDate);
            String postContent = JacksonUtil.toJson(dateMap);
            String wxResponse = HttpsUtils.post(url, postContent, "utf-8");
            WeUserTrendData weUserTrendData = JacksonUtil.fromJson(wxResponse, WeUserTrendData.class);
            List<WeUserTrendDetail> WeUserTrendDetailLst = weUserTrendData.getList();
            Map<String, Integer> currentWeekTrendMap = new HashMap<>();
            if (WeUserTrendDetailLst != null && !WeUserTrendDetailLst.isEmpty()) {
                currentWeekTrendMap.put("visit_pv", weUserTrendData.getList().get(0).getVisit_pv());//访问次数
                currentWeekTrendMap.put("visit_uv", weUserTrendData.getList().get(0).getVisit_pv());// 访问人数
                currentWeekTrendMap.put("visit_uv_new", weUserTrendData.getList().get(0).getVisit_uv_new());// 新用户数
            } else {
                currentWeekTrendMap.put("visit_pv", 0);//访问次数
                currentWeekTrendMap.put("visit_uv", 0);// 访问人数
                currentWeekTrendMap.put("visit_uv_new", 0);// 新用户数
            }
            ret.put(endDate, currentWeekTrendMap);
        }
        return ret;
    }

    /**
     * 取得用户月趋势
     *
     * @return
     * @throws AppException
     */
    private LinkedHashMap<String, Map<String, Integer>> getUserMonthlyTrend(int monthCnt) throws AppException, IOException {
        String accessToken = myAccessTokenService.getAccessToken();
        String url = "https://api.weixin.qq.com/datacube/getweanalysisappidmonthlyvisittrend?access_token=" + accessToken;
        Map<String, String> dateMap = new HashMap<>();
        LinkedHashMap<String, Map<String, Integer>> ret = new LinkedHashMap<>();
        for (int i = monthCnt; i >= 1; i--) {
            // 开始日期，为自然月第一天
            String beginDate = DateUtil.getPastMonthFirstDay(i);
            // 结束日期，为自然月最后一天，限定查询一个月数据
            String endDate = DateUtil.getPastMonthLastDay(i);
            dateMap.put("begin_date", beginDate);
            dateMap.put("end_date", endDate);
            String postContent = JacksonUtil.toJson(dateMap);
            String wxResponse = HttpsUtils.post(url, postContent, "utf-8");
            WeUserTrendData weUserTrendData = JacksonUtil.fromJson(wxResponse, WeUserTrendData.class);
            List<WeUserTrendDetail> WeUserTrendDetailLst = weUserTrendData.getList();
            Map<String, Integer> currentMonthTrendMap = new HashMap<>();
            if (WeUserTrendDetailLst != null && !WeUserTrendDetailLst.isEmpty()) {
                currentMonthTrendMap.put("visit_pv", weUserTrendData.getList().get(0).getVisit_pv());//访问次数
                currentMonthTrendMap.put("visit_uv", weUserTrendData.getList().get(0).getVisit_pv());// 访问人数
                currentMonthTrendMap.put("visit_uv_new", weUserTrendData.getList().get(0).getVisit_uv_new());// 新用户数
            } else {
                currentMonthTrendMap.put("visit_pv", 0);//访问次数
                currentMonthTrendMap.put("visit_uv", 0);// 访问人数
                currentMonthTrendMap.put("visit_uv_new", 0);// 新用户数
            }
            ret.put(endDate, currentMonthTrendMap);
        }
        return ret;
    }


}
