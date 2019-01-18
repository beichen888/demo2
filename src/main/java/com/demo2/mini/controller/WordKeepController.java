package com.demo2.mini.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.UserAction;
import com.demo2.mini.model.WordKeep;
import com.demo2.mini.service.IUserActionService;
import com.demo2.mini.service.IWordKeepService;
import com.demo2.user.model.UserToken;
import com.demo2.word.model.ChineseLevel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by miguo on 2018/6/13.
 */

@RestController
@RequestMapping("/api")
@Auth
@Api(value = "单词收藏管理", description = "单词收藏管理")
public class WordKeepController extends BaseController {

    @Resource
    private IWordKeepService wordKeepService;

    @Resource
    private IUserActionService userActionService;

    private static final String ACTION_NAME = "wordKeep";

    @ApiOperation(value = "取得所有收藏单词", notes = "取得所有收藏单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("/wordKeeps")
    public Result listByUserId(@RequestAttribute("userToken") UserToken userToken,
                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                               String orderType, ChineseLevel level) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        if ("A".equals(orderType)) {
            sort = new Sort(Sort.Direction.DESC, "id");
        } else if ("B".equals(orderType)) {
            sort = new Sort(Sort.Direction.ASC, "id");
        } else if ("C".equals(orderType)) {
            sort = new Sort(Sort.Direction.ASC, "pinyin");
        } else if ("D".equals(orderType)) {
            sort = new Sort(Sort.Direction.DESC, "pinyin");
        }
        Pageable pageable = new PageRequest(page, size, sort);
        Integer userId = userToken.getId();
        Page<WordKeep> wordKeepList = wordKeepService.listByUserId(pageable, userId, level);
        return renderSuccess(wordKeepList);
    }


    @ApiOperation(value = "单词是否已被收藏", notes = "单词是否已被收藏", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("/wordKeep/{wordId}")
    public Result isKeep(@RequestAttribute("userToken") UserToken userToken, @PathVariable Integer wordId) {
        WordKeep wordKeep = wordKeepService.findByUserIdAndWordId(userToken.getId(), wordId);
        return renderSuccess(wordKeep != null);
    }

    @PostMapping("/wordKeep/{wordId}")
    @ApiOperation(value = "收藏单词", notes = "收藏单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result save(@RequestAttribute("userToken") UserToken userToken, @PathVariable Integer wordId) {
        WordKeep wordKeep = new WordKeep();
        wordKeep.setUserId(userToken.getId());
        wordKeep.setWordId(wordId);
        wordKeep = wordKeepService.save(wordKeep);
        return renderSuccess(wordKeep);
    }

    @DeleteMapping("/wordKeep/{wordId}")
    @ApiOperation(value = "取消收藏", notes = "取消收藏", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result delete(@RequestAttribute("userToken") UserToken userToken, @PathVariable Integer wordId) {
        wordKeepService.deleteByUserIdAndWordId(userToken.getId(), wordId);
        return renderSuccess();
    }

    @GetMapping("/wordKeeps/isFirstUse")
    @ApiOperation(value = "用户是否第一次使用", notes = "用户是否第一次使用", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result isEmpty(@RequestAttribute("userToken") UserToken userToken) {
        List<UserAction> userActions = userActionService.find(userToken.getId(), ACTION_NAME);
        if (userActions.isEmpty()) {
            UserAction userAction = new UserAction();
            userAction.setUserId(userToken.getId());
            userAction.setAction(ACTION_NAME);
            userActionService.save(userAction);
            return renderSuccess(true);
        } else {
            return renderSuccess(false);
        }
    }

    @PutMapping("/wordKeep/read/{wordId}")
    @ApiOperation(value = "取消新收藏标志", notes = "取消新收藏标志", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result read(@RequestAttribute("userToken") UserToken userToken, @PathVariable Integer wordId) {
        wordKeepService.readWord(userToken.getId(), wordId);
        return renderSuccess();
    }
}
