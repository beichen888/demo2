package com.demo2.question.service;

import com.demo2.common.ChineseCharacterTool;
import com.demo2.common.JacksonUtil;
import com.demo2.question.dao.QuestionDao;
import com.demo2.question.model.Question;
import com.demo2.question.model.QuestionType;
import com.demo2.word.dao.WordDao;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * Created by miguo on 2018/6/16.
 */
@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class QuestionService implements IQuestionService {

    @Resource
    private WordDao wordDao;
    @Resource
    private QuestionDao questionDao;


    @Override
    public Page<Question> listByPage(Pageable pageable, ChineseLevel level, String keyword, Date start, Date end) {
        Specification specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (level != null) {
                predicates.add(cb.equal(root.get("level"), level));
            }
            if (StringUtils.isNotBlank(keyword)) {
                predicates.add(cb.like(root.get("stem"), "%" + keyword + "%"));
            }
            if (start != null && end != null) {
                predicates.add(cb.between(root.get("updateDate"), start, end));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return questionDao.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(Word word) {
        // 创建题目
        createTypeOne(word);
        createTypeTwo(word);
        createTypeThree(word);
        createTypeFour(word);
        createTypeFive(word);
        createTypeSix(word);
        return true;
    }

    @Override
    public Question findById(Integer id) {
        return questionDao.findOne(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Question question) {
        question.setUpdateDate(new Date());
        questionDao.save(question);
        return true;
    }

    @Override
    public List<Question> findByWordId(Integer wordId) {
        return questionDao.findQuestionsByWordIdEquals(wordId);
    }

    /**
     * 给单词选图片，考察字形和场景的关联
     *
     * @param word
     */
    private void createTypeOne(Word word) {
        Question question = new Question();

        question.setType(QuestionType.ONE);
        question.setStem(word.getVocabulary());
        // 选项四张图片
        String imageGroup = word.getImageGroup();
        List<String> imageLst = wordDao.findImagesByImageGroup(imageGroup, word.getId(), word.getLevel().name(), 3);
        String answer = getRandomAnswer(imageLst, word.getImagePath());
        String optionsJson = JacksonUtil.toJson(imageLst);
        question.setOptionLst(optionsJson);
        question.setAnswer(answer);
        saveQuestion(question, word);
    }

    /**
     * 给单词选词义，考察字形和字义的关联
     *
     * @param word
     */
    private void createTypeTwo(Word word) {
        Question question = new Question();
        question.setType(QuestionType.TWO);
        question.setStem(word.getVocabulary());
        List<String> options = new ArrayList<>();
        List<Word> wordList = wordDao.findWordsByLevel(word.getLevel().name(), word.getId(), 3);

        for (Word word1 : wordList) {
            String option = word1.getKind1() + " " + word1.getMean1() + " " + word1.getKind2() + " " + word1.getMean2();
            options.add(option);
        }
        String correctOption = word.getKind1() + " " + word.getMean1() + " " + word.getKind2() + " " + word.getMean2();
        String answer = getRandomAnswer(options, correctOption);
        String optionsJson = JacksonUtil.toJson(options);
        question.setOptionLst(optionsJson);
        question.setAnswer(answer);
        saveQuestion(question, word);
    }

    /**
     * 给发音选汉字，考察字音和字形的关联
     *
     * @param word
     */
    private void createTypeThree(Word word) {
        Question question = new Question();
        question.setType(QuestionType.THREE);
        question.setStem(word.getVocabularyAudioPath());
        List<Word> wordList = wordDao.findWordsByLevel(word.getLevel().name(), word.getId(), 3);
        List<String> options = new ArrayList<>();
        for (Word word1 : wordList) {
            options.add(ChineseCharacterTool.getChineseCharacter(word1.getVocabulary()));
        }
        String correctOption = ChineseCharacterTool.getChineseCharacter(word.getVocabulary());
        String answer = getRandomAnswer(options, correctOption);
        question.setAnswer(answer);
        String optionsJson = JacksonUtil.toJson(options);
        question.setOptionLst(optionsJson);
        saveQuestion(question, word);
    }

    /**
     * 给发音选拼音，考察字音和拼音的关联
     *
     * @param word
     */
    private void createTypeFour(Word word) {
        Question question = new Question();
        question.setType(QuestionType.FOUR);
        question.setStem(word.getVocabularyAudioPath());
        List<Word> wordList = wordDao.findWordsByLevel(word.getLevel().name(), word.getId(), 3);
        List<String> options = new ArrayList<>();
        for (Word word1 : wordList) {
            options.add(ChineseCharacterTool.removeChineseCharacter(word1.getVocabulary()).replace("(", "").replace(")", ""));
        }
        String correctOption = ChineseCharacterTool.removeChineseCharacter(word.getVocabulary()).replace("(", "").replace(")", "");
        String answer = getRandomAnswer(options, correctOption);
        question.setAnswer(answer);
        String optionsJson = JacksonUtil.toJson(options);
        question.setOptionLst(optionsJson);
        saveQuestion(question, word);
    }

    /**
     * 给发音选词义，考察字音和字义的关联
     *
     * @param word
     */
    private void createTypeFive(Word word) {
        Question question = new Question();
        question.setType(QuestionType.FIVE);
        question.setStem(word.getVocabularyAudioPath());
        List<String> options = new ArrayList<>();
        List<Word> wordList = wordDao.findWordsByLevel(word.getLevel().name(), word.getId(), 3);

        for (Word word1 : wordList) {
            String option = word1.getKind1() + " " + word1.getMean1() + " " + word1.getKind2() + " " + word1.getMean2();
            options.add(option);
        }
        String correctOption = word.getKind1() + " " + word.getMean1() + " " + word.getKind2() + " " + word.getMean2();
        String answer = getRandomAnswer(options, correctOption);
        question.setAnswer(answer);
        String optionsJson = JacksonUtil.toJson(options);
        question.setOptionLst(optionsJson);
        saveQuestion(question, word);

    }

    /**
     * 给例句选图片，考察字形和场景的关联
     *
     * @param word
     */
    private void createTypeSix(Word word) {
        Question question = new Question();

        question.setType(QuestionType.SIX);
        question.setStem(word.getSentence());
        // 选项四张图片
        String imageGroup = word.getImageGroup();
        List<String> imageLst = wordDao.findImagesByImageGroup(imageGroup, word.getId(), word.getLevel().name(), 3);
        String answer = getRandomAnswer(imageLst, word.getImagePath());
        String optionsJson = JacksonUtil.toJson(imageLst);
        question.setOptionLst(optionsJson);
        question.setAnswer(answer);
        saveQuestion(question, word);
    }

    private void saveQuestion(Question question, Word word) {
        question.setWordId(word.getId());
        question.setLevel(word.getLevel());
        question.setReminder1(word.getSentenceMean());
        Map<String, String> reminder2Map = new HashMap<>();
        reminder2Map.put("kind1", word.getKind1());
        reminder2Map.put("mean1", word.getMean1());
        reminder2Map.put("kind2", word.getKind2());
        reminder2Map.put("mean2", word.getMean2());
        String reminder2 = JacksonUtil.toJson(reminder2Map);
        question.setReminder2(reminder2);
        Question dbQuestion = questionDao.findQuestionByWordIdEqualsAndTypeEquals(question.getWordId(), question.getType());
        if (dbQuestion != null) {
            question.setId(dbQuestion.getId());
        }
        question.setCreatedDate(new Date());
        question.setUpdateDate(new Date());
        questionDao.save(question);
    }

    private static String getRandomAnswer(List<String> optionLst, String option) {
        if (optionLst != null) {
            int index = new Random().nextInt(optionLst.size() + 1);
            optionLst.add(index, option);
            if (index == 0) {
                return "A";
            } else if (index == 1) {
                return "B";
            } else if (index == 2) {
                return "C";
            } else if (index == 3) {
                return "D";
            }
        }
        return null;
    }
}
