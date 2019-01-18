package com.demo2.question.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.question.model.Question;
import com.demo2.question.service.IQuestionService;
import com.demo2.user.model.UserToken;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import com.demo2.word.service.IWordService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by miguo on 2018/6/17.
 */
@RestController
@RequestMapping(value = "/api")
@Auth
public class QuestionController extends BaseController {
    @Resource
    private IQuestionService questionService;

    @Resource
    private IWordService wordService;

    @PostMapping("/question/create/{wordId}")
    public Result createQuestion(@PathVariable Integer wordId) {
        Word word = wordService.find(wordId);
        if(word!=null) {
            questionService.create(word);
        }
        return renderSuccess();
    }

    @GetMapping(value = "/questions")
    @ApiOperation(value = "分页取得所有单词", notes = "分页取得所有单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result listByPage(@PageableDefault(sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                             @RequestParam(required = false) ChineseLevel level,
                             @RequestParam(required = false) String keyword) {
        Page<Question> wordPage = questionService.listByPage(pageable, level, keyword, startDate, endDate);
        return renderSuccess(wordPage);
    }

    @GetMapping("/question/{id}")
    public Result getById(@PathVariable Integer id) {
        Question question = questionService.findById(id);
        return renderSuccess(question);
    }

    @PutMapping("/question/{id}")
    public Result update(@PathVariable Integer id, @RequestBody Question question) {
        if (Objects.equals(id, question.getId())) {
            questionService.update(question);
        }
        return renderSuccess();
    }

    @PostMapping("/question/init")
    public Result init(@RequestAttribute("userToken") UserToken userToken, @RequestParam ChineseLevel level) {
        List<Word> wordList = wordService.listByLevel(level);
        for (Word word : wordList) {
            questionService.create(word);
        }
        return renderSuccess();
    }
}
