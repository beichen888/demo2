package com.demo2.mini.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.exception.AppException;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.LearnRecord;
import com.demo2.mini.model.QuestionResult;
import com.demo2.mini.model.StudyPlan;
import com.demo2.mini.model.UserScore;
import com.demo2.mini.service.*;
import com.demo2.user.model.UserToken;
import com.demo2.word.service.IWordService;
import com.demo2.wrongrecord.service.IWrongWordRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习流程controller
 */
@RestController
@RequestMapping(value = "/api")
@Api(value = "测试流程", description = "测试流程")
@Auth
public class LearnController extends BaseController {

    @Resource
    private IStudyPlanService studyPlanService;
    @Resource
    private ILearnRecordService learnRecordService;
    @Resource
    private IWordService wordService;
    @Resource
    private IQuestionResultService questionResultService;
    @Resource
    private IWrongWordRecordService wrongWordRecordService;
    @Resource
    private IUserWordService userWordService;
    @Resource
    private IUserScoreService userScoreService;

    @RequestMapping(value = "/learnStatus")
    public Result learnStatus(@RequestAttribute("userToken") UserToken userToken) throws ParseException {
        Integer userId = userToken.getId();
        Map<String, Object> resultMap = new HashMap<>();
        // 学习天数(打卡天数)
        StudyPlan plan = studyPlanService.findByUserId(userId);
        List<UserScore> scoreByDay = userScoreService.findAddScoreDaysByUserId(userToken.getId());
        resultMap.put("learnDays", scoreByDay.size());
        // 今天已经学习的单词数
        int todayLearnedCnt = userWordService.findTodayLearned(userId).size();
        // 今天还要学习单词数
        int toLearnCnt = plan.getDayCnt() - todayLearnedCnt;
        resultMap.put("toLearnCnt", toLearnCnt);
        //当前级别单词总量
        long total = wordService.countByLevel(plan.getChineseLevel());
        resultMap.put("total", total);
        //当前级别所有已学单词数量
        int totalLearnedCnt = userWordService.findLearnedLst(userId, plan.getChineseLevel()).size();
        resultMap.put("totalLearnedCnt", totalLearnedCnt);
        return renderSuccess(resultMap);
    }

    /**
     * 开始学习
     *
     * @param userToken 用户token
     * @return
     */
    @PostMapping("/learnRecord/start")
    @ApiOperation(value = "开始测试入口", notes = "开始测试入口", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result createLearnRecord(@RequestAttribute("userToken") UserToken userToken, Boolean changePlan) throws AppException {
        Map map = learnRecordService.startTest(userToken.getId());
        return renderSuccess(map);
    }

    /**
     * 保存答题结果
     *
     * @param resultId
     * @param correct
     * @return
     */
    @ApiOperation(value = "保存答案", notes = "保存答案", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping("/questionResult/{resultId}")
    public Result createQuestionResult(@RequestAttribute("userToken") UserToken userToken, @PathVariable Integer resultId, Boolean correct) {
        QuestionResult result = questionResultService.findById(resultId);
        if (result != null) {
            //保存答案
            result.setCorrect(correct);
            questionResultService.save(result, userToken.getId());

            //保存单词正确率
            if (Boolean.TRUE.equals(correct)) {
                wrongWordRecordService.addCorrectCnt(userToken.getId(), result.getQuestion().getWordId());
            } else {
                wrongWordRecordService.addWrongCnt(userToken.getId(), result.getQuestion().getWordId());
            }
        }
        //设置为已学
        userWordService.setLearned(userToken.getId(), result.getQuestion().getWordId());
        return renderSuccess(result);
    }

    @ApiOperation(value = "完成学习", notes = "完成学习", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping("/learnRecord/finish/{recordId}")
    public Result finish(@PathVariable Integer recordId) {
        LearnRecord record = learnRecordService.findById(recordId);
        if (record != null) {
            record.setFinish(true);
            learnRecordService.update(record);
        }
        return renderSuccess(record);
    }

    @PostMapping("/learnRecord/practiceAfterLearn")
    @ApiOperation(value = "学习后练习入口", notes = "学习后练习入口", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result practiceAfterLearn(@RequestAttribute("userToken") UserToken userToken, Integer preLearnRecordId) throws AppException {
        Map<String, Object> retMap = learnRecordService.createPracticeFromPreRecord(userToken.getId(), preLearnRecordId);
        return renderSuccess(retMap);
    }


    /**
     * 收藏单词练习入口
     *
     * @param userToken
     * @param wordIds
     * @return
     */
    @ApiOperation(value = "收藏单词练习入口", notes = "收藏单词练习入口", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping("/learnRecord/practiceFromKeep")
    public Result practiceFromKeep(@RequestAttribute("userToken") UserToken userToken, @RequestParam(value = "wordIds[]") Integer[] wordIds) throws AppException {
        Map<String, Object> map = learnRecordService.practiceFromKeep(userToken.getId(), wordIds);
        return renderSuccess(map);
    }

}
