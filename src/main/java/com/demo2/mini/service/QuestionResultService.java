package com.demo2.mini.service;

import com.demo2.common.MessageCode;
import com.demo2.common.exception.AppException;
import com.demo2.mini.dao.QuestionResultDao;
import com.demo2.mini.dao.StudyPlanDao;
import com.demo2.mini.dao.UserWordDao;
import com.demo2.mini.model.QuestionResult;
import com.demo2.mini.model.StudyPlan;
import com.demo2.question.dao.QuestionDao;
import com.demo2.question.model.Question;
import com.demo2.word.model.Word;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class QuestionResultService implements IQuestionResultService {

    @Resource
    private QuestionResultDao questionResultDao;
    @Resource
    private QuestionDao questionDao;
    @Resource
    private UserWordDao userWordDao;

    @Resource
    private StudyPlanDao studyPlanDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public QuestionResult save(QuestionResult result,Integer userId) {
        return questionResultDao.save(result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<QuestionResult> createPracticeResultByWord(Integer userId, Word word, Integer recordId, int limit) throws AppException {
        StudyPlan plan = studyPlanDao.findStudyPlanByUserIdEquals(userId);
        if (plan == null) {
            throw new AppException(MessageCode.USER_PLAN_NOT_EXIST_ERROR);
        }
        Boolean isLearnChinese = plan.getIsLearnChinese();
        //插入练习题目
        List<Question> wordQuestions;
        List<QuestionResult> ret = new ArrayList<>();
        if (isLearnChinese != null && isLearnChinese) {
            wordQuestions = questionDao.findRandomQuestionByWordIdAndTypeNotFour(word.getId(), limit);
        } else {
            wordQuestions = questionDao.findRandomQuestionByWordIdAndTypeNotThree(word.getId(), limit);
        }
        for (Question question : wordQuestions) {
            //插入记录到QuestionResult
            QuestionResult questionResult = new QuestionResult();
            questionResult.setRecordId(recordId);
            questionResult.setCreatedDate(new Date());
            questionResult.setQuestion(question);
            questionResult.setNewWord(false);
            questionResult.setPractice(true);
            questionResultDao.save(questionResult);
            ret.add(questionResult);
        }
        return ret;
    }

    @Override
    public List<QuestionResult> findDoneByLearnRecordId(Integer learnRecordId) {
        return questionResultDao.findQuestionResultsByCorrectIsNotNullAndRecordIdEqualsOrderById(learnRecordId);
    }

    @Override
    public List<QuestionResult> findListByLearnRecordId(Integer learnRecordId) {
        return questionResultDao.findQuestionResultsByRecordIdEqualsOrderById(learnRecordId);
    }

    @Override
    public List<QuestionResult> findUnDoneByLearnRecordId(Integer learnRecordId) {
        return questionResultDao.findQuestionResultsByCorrectIsNullAndRecordIdEqualsOrderById(learnRecordId);
    }

    @Override
    public List<QuestionResult> findWrongPracticeResults(Integer learnRecordId) {
        return questionResultDao.findQuestionResultsByRecordIdEqualsAndCorrectIsFalseAndPracticeIsTrueAndNewWordIsTrue(learnRecordId);
    }

    @Override
    public QuestionResult findById(Integer id) {
        return questionResultDao.findOne(id);
    }
}
