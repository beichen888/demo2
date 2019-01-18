package com.demo2.mini.service;

import com.demo2.common.DateUtil;
import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import com.demo2.mini.dao.LearnRecordDao;
import com.demo2.mini.dao.QuestionResultDao;
import com.demo2.mini.dao.StudyPlanDao;
import com.demo2.mini.dao.UserWordDao;
import com.demo2.mini.model.LearnRecord;
import com.demo2.mini.model.QuestionResult;
import com.demo2.mini.model.StudyPlan;
import com.demo2.mini.model.UserWord;
import com.demo2.question.dao.QuestionDao;
import com.demo2.question.model.Question;
import com.demo2.question.model.QuestionType;
import com.demo2.word.dao.WordDao;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class LearnRecordService implements ILearnRecordService {

    @Resource
    private LearnRecordDao learnRecordDao;
    @Resource
    private StudyPlanDao studyPlanDao;
    @Resource
    private WordDao wordDao;
    @Resource
    private UserWordDao userWordDao;

    @Resource
    private QuestionDao questionDao;
    @Resource
    private QuestionResultDao questionResultDao;
    @Resource
    private IQuestionResultService questionResultService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map startTest(Integer userId) throws AppException {
        Map<String, Object> map = new HashMap<>();
        List<QuestionResult> retQuestionResult = new ArrayList<>();
        // 当天是否有学习记录
        LearnRecord record = findTodayUnFinishLearnRecord(userId);
        //当天没有未完成学习记录,新建记录并抽题
        if (record == null) {
            record = createNewLearnRecord(userId, retQuestionResult);
        } else {
            // 中途中断的
            Integer recordId = record.getId();
            List<QuestionResult> results = questionResultService.findListByLearnRecordId(recordId);
            retQuestionResult.addAll(results);
        }
        map.put("record", record);
        map.put("questionResults", retQuestionResult);
        return map;
    }

    /**
     * 取得题目（主题目和练习题）
     *
     * @param userId
     * @param words
     * @param recordId
     * @param limit
     * @return
     * @throws AppException
     */
    private List<QuestionResult> createQuestionResultByWord(Integer userId, List<Word> words, Integer recordId, int limit) throws AppException {
        List<QuestionResult> ret = new ArrayList<>();
        int perRoundWordCnt = words.size() / 5;
        if (words.size() < 5) {
            perRoundWordCnt = 1;
        }

        //生成练习题的word
        List<Word> practiceWords = new ArrayList<>();
        for (int index = 0; index < words.size(); index++) {
            Word word = words.get(index);
            Question mainQuestion = questionDao.findQuestionByWordIdEqualsAndTypeEquals(word.getId(), QuestionType.SIX);
            // 插入学习题目
            QuestionResult questionResult = new QuestionResult();
            questionResult.setRecordId(recordId);
            questionResult.setCreatedDate(new Date());
            questionResult.setQuestion(mainQuestion);
            questionResult.setPractice(false);
            questionResult.setNewWord(true);
            questionResultDao.save(questionResult);
            ret.add(questionResult);
            practiceWords.add(word);
            //到达五分之一，生成练习
            if ((index + 1) % perRoundWordCnt == 0) {
                //打乱单词顺序
                Collections.shuffle(practiceWords);
                for (Word practiceWord : practiceWords) {
                    //插入练习题目
                    List<QuestionResult> practiceResults = questionResultService.createPracticeResultByWord(userId, practiceWord, recordId, limit);
                    ret.addAll(practiceResults);
                }
                practiceWords = new ArrayList<>();
            }
        }
        //最后不足五分之一的部分
        if (practiceWords.size() > 0) {
            Collections.shuffle(practiceWords);
            for (Word practiceWord : practiceWords) {
                //插入练习题目
                List<QuestionResult> practiceResults = questionResultService.createPracticeResultByWord(userId, practiceWord, recordId, limit);
                ret.addAll(practiceResults);
            }
        }
        return ret;
    }

    private LearnRecord createNewLearnRecord(Integer userId, List<QuestionResult> retQuestionResult) throws AppException {
        StudyPlan plan = studyPlanDao.findStudyPlanByUserIdEquals(userId);
        if (plan == null) {
            throw new AppException(MessageCode.USER_PLAN_NOT_EXIST_ERROR);
        }
        //上次的非练习record
        //复习部分的单词=昨日的新词+五分之一个每天单词量的已学过单词，单词随机抽取，有过错误标记的优先
        LearnRecord preRecord = learnRecordDao.findLastLearnRecordByUserId(userId);
        //创建新record
        LearnRecord record = create(userId, false);
        if (preRecord != null) {
            //上条记录的学习记录中的新词(随机排序)
            List<QuestionResult> questionResults = questionResultDao.findNewWordQuestionResultsByRecordId(preRecord.getId());
            // 五分之一之前已经学习过的单词
            Date learnDate = preRecord.getCreatedDate();
            List<QuestionResult> learnedRecords = questionResultDao.findLearnedNewWordQuestionResultsBefore(learnDate, plan.getWordCntPerDay() / 5);
            questionResults.addAll(learnedRecords);

            for (QuestionResult questionResult : questionResults) {
                QuestionResult newResult = new QuestionResult();
                BeanUtils.copyProperties(questionResult, newResult);
                newResult.setId(null);
                newResult.setRecordId(record.getId());
                newResult.setCorrect(null);
                newResult.setNewWord(false);
                newResult.setPractice(true);
                newResult.setCreatedDate(new Date());
                newResult = questionResultDao.save(newResult);
                retQuestionResult.add(newResult);
            }
        }

        //取得要学习的新单词
        int toLearnCnt = plan.getWordCntPerDay();
        ChineseLevel level = plan.getChineseLevel();
        List<UserWord> userWords = userWordDao.findTestWordsByPriorityIsNotNull(userId, level.name(), toLearnCnt);
        if (userWords.size() < toLearnCnt) {
            List<UserWord> userWords2 = userWordDao.findTestWordsByPriorityIsNull(userId, level.name(), toLearnCnt - userWords.size());
            userWords.addAll(userWords2);
        }
        List<Word> words = new ArrayList<>();
        for (UserWord userWord : userWords) {
            Word word = wordDao.findOne(userWord.getWordId());
            words.add(word);
        }
        //取得题目(主题目和练习题)
        List<QuestionResult> questionResults = createQuestionResultByWord(userId, words, record.getId(), 1);
        retQuestionResult.addAll(questionResults);

        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LearnRecord create(Integer userId, boolean isPractice) {
        LearnRecord record = new LearnRecord();
        record.setUserId(userId);
        record.setFinish(false);
        record.setPractice(isPractice);
        record.setRound(0);
        record.setCreatedDate(new Date());
        record.setUpdateDate(new Date());
        return learnRecordDao.save(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createPracticeFromPreRecord(Integer userId, Integer preRecordId) throws AppException {
        Map<String, Object> retMap = new HashMap<>();
        LearnRecord record = null;
        List<QuestionResult> retResult = null;
        LearnRecord preRecord = learnRecordDao.findOne(preRecordId);
        if (preRecord != null) {
            record = new LearnRecord();
            record.setUserId(userId);
            record.setFinish(false);
            record.setPractice(true);
            record.setPreRecordId(preRecordId);
            int round = 0;
            round = preRecord.getRound() + 1;
            record.setRound(round);
            record.setCreatedDate(new Date());
            record.setUpdateDate(new Date());
            record = learnRecordDao.save(record);
            retResult = new ArrayList<>();
            List<QuestionResult> wrongResults = questionResultService.findWrongPracticeResults(preRecordId);
            for (QuestionResult wrongResult : wrongResults) {
                Integer wordId = wrongResult.getQuestion().getWordId();
                Word word = wordDao.findOne(wordId);
                List<QuestionResult> questionResults = questionResultService.createPracticeResultByWord(userId, word, record.getId(), 1);
                retResult.addAll(questionResults);
            }
        }
        retMap.put("record", record);
        retMap.put("questionResults", retResult);
        return retMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> practiceFromKeep(Integer userId, Integer[] wordIds) throws AppException {
        Map<String, Object> map = new HashMap<>();
        LearnRecord record = create(userId, true);

        List<QuestionResult> questionResults = new ArrayList<>();
        List<Word> words = new ArrayList<>();
        for (Integer wordId : wordIds) {
            Word word = wordDao.findOne(wordId);
            words.add(word);
        }
        questionResults.addAll(createQuestionResultByWord(userId, words, record.getId(), 1));
        map.put("record", record);
        map.put("questionResults", questionResults);
        return map;
    }


    @Override
    public LearnRecord findById(Integer id) {
        return learnRecordDao.findOne(id);
    }

    @Override
    public LearnRecord findUnFinishByUserId(Integer userId) {
        LearnRecord record = learnRecordDao.findLearnRecordByUserIdEqualsAndFinishIsFalseAndPracticeIsFalse(userId);
        return record;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LearnRecord update(LearnRecord record) {
        LearnRecord db = learnRecordDao.findOne(record.getId());
        if (db != null) {
            record.setUpdateDate(new Date());
            record = learnRecordDao.save(record);
            return record;
        }
        return null;
    }

    /**
     * 查找当天未完成的学习记录
     * @param userId
     * @return
     */
    private LearnRecord findTodayUnFinishLearnRecord(Integer userId) {
        Date from = DateUtil.getTodayFirstSecond();
        Date end = DateUtil.getTodayLastSecond();
        return learnRecordDao.findLearnRecordByUserIdEqualsAndCreatedDateBetweenAndPracticeIsFalseAndFinishIsFalse(userId, from, end);
    }

    @Override
    public List<LearnRecord> findAllFinishByUserId(Integer userId) {
        return learnRecordDao.findLearnRecordByUserIdEqualsAndFinishIsTrue(userId);
    }
}
