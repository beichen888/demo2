package com.demo2.feedback.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.user.model.User;
import com.demo2.user.model.UserToken;
import com.demo2.feedback.model.WordFeedback;
import com.demo2.feedback.service.IWordFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
@Api(value = "小程序反馈管理", description = "小程序反馈管理")
@Auth
public class WordFeedbackController extends BaseController {

    @Resource
    private IWordFeedbackService wordFeedbackService;

    /**
     * 反馈
     */
    @ApiOperation(value = "新增反馈信息", notes = "新增反馈信息", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping(value = "/wordfeedback")
    public Result create(@RequestAttribute("userToken") UserToken userToken,@RequestBody WordFeedback wordFeedback) {
        User user = new User();
        user.setId(userToken.getId());
        wordFeedback.setCreator(user);
        wordFeedbackService.save(wordFeedback);
        return renderSuccess();
    }

    /**
     * 收录
     */
    @ApiOperation(value = "收录反馈信息", notes = "收录反馈信息", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PutMapping("/wordfeedback/include/{id}")
    public Result include(@PathVariable Integer id) {
        WordFeedback wordFeedback = wordFeedbackService.find(id);
        if (id != null) {
            wordFeedback.setIsIncluded(true);
            wordFeedbackService.save(wordFeedback);
        }
        return renderSuccess();
    }

    /**
     * 反馈列表
     */
    @GetMapping(value = "/wordfeedbacks")
    @ApiOperation(value = "取得反馈列表", notes = "取得反馈列表", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result list(@PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC) Pageable pageable, String keyword) {
        Page<WordFeedback> feedbackPage = wordFeedbackService.list(pageable,keyword);
        return renderSuccess(feedbackPage);
    }
}
