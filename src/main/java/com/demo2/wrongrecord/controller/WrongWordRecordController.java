package com.demo2.wrongrecord.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.user.model.UserToken;
import com.demo2.wrongrecord.model.WrongWordRecord;
import com.demo2.wrongrecord.service.IWrongWordRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by miguo on 2018/6/9.
 */
@RestController
@RequestMapping("/api")
@Auth
public class WrongWordRecordController extends BaseController {

    @Resource
    private IWrongWordRecordService wrongWordRecordService;

    @GetMapping("/wrongWord/getPageByUserId")
    public Result getPageByUserId(@RequestAttribute("userToken") UserToken userToken,
                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "wrongCount");
        Page<WrongWordRecord> recordPage = wrongWordRecordService.findPageListByUserId(userToken.getId(), page,size,sort);
        return renderSuccess(recordPage);
    }

    @GetMapping("/wrongWord/{id}")
    public Result getById(@PathVariable Integer id) {
        WrongWordRecord record = wrongWordRecordService.findById(id);
        return renderSuccess(record);
    }

    @GetMapping("/wrongWord/getPageByWord")
    public Result getPageByWord(@RequestAttribute("userToken") UserToken userToken,
                                @RequestParam(value = "page", defaultValue = "0") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "wrongCount");
        Page<Map<String,Object>> recordPage = wrongWordRecordService.findPageListGroupByWord(page,size,sort);
        return renderSuccess(recordPage);
    }

    @GetMapping("/wrongWord/getPageByLevel")
    public Result getPageByWordLevel(@RequestAttribute("userToken") UserToken userToken,
                                     @RequestParam(value = "page", defaultValue = "0") Integer page,
                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.ASC, "wrongCount");
        Page<Map<String,Object>> recordPage = wrongWordRecordService.findPageListGroupByLevel(page,size,sort);
        return renderSuccess(recordPage);
    }


}
