package com.demo2.mini.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.model.CopyWriter;
import com.demo2.mini.service.ICopyWriterService;
import com.demo2.user.model.UserLanguage;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 小程序用文案控制器
 */

@RestController
@Api(value = "小程序用文案查询", description = "小程序用文案查询")
@RequestMapping("/api/copywriter")
@Auth("anon")
public class CopyWriterController extends BaseController{

    @Resource
    private ICopyWriterService copyWriterService;

    /**
     * 取得所有文案
     * @param language 语言类别
     * @return
     */
    @GetMapping("/getByLanguage")
    public Result getByLanguage(UserLanguage language){
        List<CopyWriter> copyWriters = copyWriterService.listByLanguage(language);
        Map<String,String>  copyWriterMap = new HashMap<>();
        for (CopyWriter copyWriter : copyWriters) {
            copyWriterMap.put(copyWriter.getWordKey(),copyWriter.getContent());
        }
        return renderSuccess(copyWriterMap);
    }
}
