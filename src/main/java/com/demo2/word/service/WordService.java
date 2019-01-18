package com.demo2.word.service;

import com.demo2.common.ChineseCharacterTool;
import com.demo2.common.Config;
import com.demo2.common.MessageCode;
import com.demo2.common.excel.ExcelRow;
import com.demo2.common.excel.ExcelSheet;
import com.demo2.common.excel.ExcelUtil;
import com.demo2.common.exception.AppException;
import com.demo2.mini.dao.StudyPlanDao;
import com.demo2.mini.model.StudyPlan;
import com.demo2.mini.service.IUserWordService;
import com.demo2.question.service.IQuestionService;
import com.demo2.word.dao.WordDao;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class WordService implements IWordService {

    @Resource
    private WordDao wordDao;
    @Resource
    private IQuestionService questionService;
    @Resource
    private StudyPlanDao studyPlanDao;
    @Resource
    private IUserWordService userWordService;
    @Resource
    private Config config;

    private static final Logger logger = LoggerFactory.getLogger(WordService.class);

    private static final String PICTURES = "Pictures/";
    private static final String AUDIO_SENTENCE = "Audio_sentence/";
    private static final String AUDIO_WORD = "Audio_word/";

    @Override
    public Word find(Integer id) {
        return wordDao.findOne(id);
    }

    @Override
    public List<Word> list(Sort sort) {
        return wordDao.findAll(sort);
    }

    @Override
    public Page<Word> listByPage(Pageable pageable, ChineseLevel level, String keyword, Date start, Date end) {
        Specification specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (level != null) {
                predicates.add(cb.equal(root.get("level"), level));
            }
            if (StringUtils.isNotBlank(keyword)) {
                predicates.add(cb.like(root.get("vocabulary"), "%" + keyword + "%"));
            }
            if (start != null && end != null) {
                predicates.add(cb.between(root.get("updateDate"), start, end));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return wordDao.findAll(specification, pageable);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Word create(Word word) {
        return wordDao.save(word);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Word update(Word word) {
        Word db = wordDao.findOne(word.getId());
        word.setHskNo(db.getHskNo());
        word.setImagePath(db.getImageGroup());
        word.setUpdateDate(new Date());
        return wordDao.save(word);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        wordDao.delete(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> importExcel(String folder, String excelName) throws AppException, IOException {
        // 读取excel
        List<ExcelSheet> sheets = ExcelUtil.readExcel(folder + excelName);
        Path excelPath = Paths.get(folder + excelName);
        Files.delete(excelPath);
        if (sheets == null || sheets.isEmpty()) {
            return null;
        }
        List<ExcelRow> excelRows = sheets.get(0).getExcelRows();
        int rowIdx = 0;
        Word word;
        List<String> errorCodes = new ArrayList<>();
        List<StudyPlan> studyPlans = studyPlanDao.findAll();
        for (ExcelRow excelRow : excelRows) {
            rowIdx++;
            if (rowIdx == 1) {
                // 第一行是标题栏 略过
                continue;
            }
            if (excelRow.isBlank()) {
                //空白行 跳过
                continue;
            }
            //将excel行数据转换为Word对象
            word = covertRow2WordInfo(folder, excelRow, rowIdx);
            if (word != null) {
                Word dbWord = wordDao.findWordByVocabularyEquals(word.getVocabulary());
                boolean isNewWord = true;
                if (dbWord != null) {
                    word.setId(dbWord.getId());
                    isNewWord = false;
                }
                word = wordDao.save(word);
                //生成题库
                questionService.create(word);
                if (isNewWord) {
                    // 新增数据到UserWord表
                    for (StudyPlan studyPlan : studyPlans) {
                        userWordService.create(studyPlan.getUserId(), word);
                    }
                }
            }
        }
        return errorCodes;
    }

    @Override
    public long countByLevel(ChineseLevel level) {
        Word word = new Word();
        if (level != null) {
            word.setLevel(level);
        }
        return wordDao.count(Example.of(word));
    }

    @Override
    public List<Word> listByLevel(ChineseLevel level) {
        Word word = new Word();
        if (level != null) {
            word.setLevel(level);
        }
        return wordDao.findAll(Example.of(word));
    }

    /**
     * 将excel行数据转换为Word对象
     *
     * @param excelRow excel行数据
     * @return Course对象
     * @throws AppException 异常
     */
    private Word covertRow2WordInfo(String folder, ExcelRow excelRow, int rowIdx) throws AppException {
        Word word = new Word();
        int cellIndex = 0;
        String rowIndex = "第" + rowIdx + "行";
        // 等级
        String chineseLevel = excelRow.getCellValue(cellIndex++);
        if (StringUtils.isBlank(chineseLevel)) {
            throw new AppException(MessageCode.WORD_LEVEL_EMPTY_ERROR, rowIndex);
        } else {
            chineseLevel = chineseLevel.substring(0, 4);
        }
        ChineseLevel level = ChineseLevel.valueOf(chineseLevel);
        word.setLevel(level);
        //hsk序号
        String hskNo = excelRow.getCellValue(cellIndex++);
        if (StringUtils.isBlank(hskNo)) {
            throw new AppException(MessageCode.HKS_NO_EMPTY_ERROR, rowIndex);
        }
        if (!StringUtils.isNumeric(hskNo)) {
            throw new AppException(MessageCode.PRIORITY_EMPTY_ERROR, rowIndex);
        }
        word.setHskNo(Integer.valueOf(hskNo));

        // 优先级
        String priority = excelRow.getCellValue(cellIndex++);
        if (StringUtils.isBlank(priority)) {
            //throw new AppException(MessageCode.PRIORITY_EMPTY_ERROR, rowIndex);
        } else {
            if (StringUtils.isNumeric(priority)) {
                word.setPriority(Integer.valueOf(priority));
            } else {
                throw new AppException(MessageCode.PRIORITY_FORMAT_ERROR, rowIndex);
            }
        }
        // 词汇
        String vocabulary = excelRow.getCellValue(cellIndex++);
        if (StringUtils.isBlank(vocabulary)) {
            throw new AppException(MessageCode.VOCABULARY_EMPTY_ERROR, rowIndex);
        }
        word.setVocabulary(vocabulary);
        //词性1
        String kind1 = excelRow.getCellValue(cellIndex++);
        word.setKind1(kind1);
        //词义1
        String mean1 = excelRow.getCellValue(cellIndex++);
        word.setMean1(mean1);

        //词性2
        String kind2 = excelRow.getCellValue(cellIndex++);
        word.setKind2(kind2);
        //词义2
        String mean2 = excelRow.getCellValue(cellIndex++);
        word.setMean2(mean2);
        //例句
        String sentence = excelRow.getCellValue(cellIndex++);
        word.setSentence(sentence);
        //例句释义
        String sentenceMean = excelRow.getCellValue(cellIndex++);
        word.setSentenceMean(sentenceMean);
        // 图片分组
        String imageGroup = excelRow.getCellValue(cellIndex++);
        word.setImageGroup(imageGroup);
        //图片
        String imageCode = excelRow.getCellValue(cellIndex++);
        File imageFile = getMediaFile(folder + PICTURES, imageCode);
        if (imageFile == null) {
            //throw new AppException(MessageCode.WORD_IMAGE_NOT_EXIST_ERROR, imageCode);
            logger.error("图片:" + imageCode + "不存在");
        } else {
            word.setImagePath(config.getDomain() + "upload/question/" + PICTURES + imageFile.getName());
        }
        // 单词音频
        String vocabularyAudioCode = excelRow.getCellValue(cellIndex++);
        File vocabularyAudio = getMediaFile(folder + AUDIO_WORD, vocabularyAudioCode);
        if (vocabularyAudio == null) {
            //throw new AppException(MessageCode.AUDIO_WORD_NOT_EXIST_ERROR, vocabularyAudioCode);
            logger.error("单词音频:" + vocabularyAudioCode + "不存在");

        } else {
            word.setVocabularyAudioPath(config.getDomain() + "upload/question/" + AUDIO_WORD + vocabularyAudio.getName());
        }
        // 例句音频
        String sentenceAudioCode = excelRow.getCellValue(cellIndex++);
        File sentenceAudioFile = getMediaFile(folder + AUDIO_SENTENCE, sentenceAudioCode);
        if (sentenceAudioFile == null) {
            //throw new AppException(MessageCode.AUDIO_SENTENCE_NOT_EXIST_ERROR, sentenceAudioCode);
            logger.error("例句音频:" + sentenceAudioCode + "不存在");
        } else {
            word.setSentenceAudioPath(config.getDomain() + "upload/question/" + AUDIO_SENTENCE + sentenceAudioFile.getName());
        }
        // 关联词（等级）
        String relatedWordLevel = excelRow.getCellValue(cellIndex++);
        if (StringUtils.isNotBlank(relatedWordLevel)) {
            ChineseLevel relatedLevel = ChineseLevel.valueOf(relatedWordLevel);
            word.setRelatedWordLevel(relatedLevel);
        } else {
            word.setRelatedWordLevel(ChineseLevel.EMPTY);
        }
        // 关联词（拼音）
        String relatedWordPinyin = excelRow.getCellValue(cellIndex++);
        word.setRelatedWordPinyin(relatedWordPinyin);
        // 关联词（词义）
        String relatedWordMean = excelRow.getCellValue(cellIndex++);
        word.setRelatedWordMean(relatedWordMean);
        // 备注
        String remark = excelRow.getCellValue(cellIndex++);
        word.setRemark(remark);
        // 创建日期
        word.setCreatedDate(new Date());
        // 更新时间
        word.setUpdateDate(new Date());
        return word;
    }

    private static File getMediaFile(String dirPath, String fileCode) {
        File dir = new File(dirPath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            String fileName = "";
            for (File file : files) {
                fileName = file.getName();
                //不带扩展名的文件名
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                //移除中文，匹配文件编号
                fileName = ChineseCharacterTool.removeChineseCharacter(fileName).replace("…", "").replace(" ", "");
                if (Objects.equals(fileName, fileCode)) {
                    return file;
                }
            }
        }
        return null;
    }


}
