package com.demo2.word.controller;

import com.demo2.common.*;
import com.demo2.common.exception.AppException;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.mini.service.IWordKeepService;
import com.demo2.user.model.UserToken;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import com.demo2.word.model.WordNote;
import com.demo2.word.service.IWordNoteService;
import com.demo2.word.service.IWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;


@RestController
@RequestMapping("/api")
@Api(value = "单词管理", description = "单词管理")
@Auth
public class WordController extends BaseController {

    @Resource
    private IWordService wordService;

    @Resource
    private IWordKeepService wordKeepService;
    @Resource
    private IWordNoteService wordNoteService;

    @Resource
    private Config config;

    @ApiOperation(value = "分页取得所有单词", notes = "分页取得所有单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping(value = "/words")
    public Result getByPage(@PageableDefault(sort = {"level"}, direction = Sort.Direction.ASC) Pageable pageable,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                            @RequestParam(required = false) ChineseLevel level,
                            @RequestParam(required = false) String keyword) {
        Page<Word> wordPage = wordService.listByPage(pageable, level, keyword, startDate, endDate);
        return renderSuccess(wordPage);
    }

    @ApiOperation(value = "取得单词详情", notes = "取得单词详情", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping("/words/{id}")
    public Result getById(@PathVariable Integer id, @RequestAttribute("userToken") UserToken userToken) {
        Word word = wordService.find(id);
        if (userToken != null) {
            Integer userId = userToken.getId();
            Boolean isKept = wordKeepService.isKept(userId, id);
            word.setKept(isKept);
            WordNote wordNote = wordNoteService.findByUserAndWord(userId, id);
            if (wordNote != null) {
                String note = wordNote.getNote();
                word.setNote(note);
            }
        }
        return renderSuccess(word);
    }

    @ApiOperation(value = "新增单词", notes = "新增单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping("/word")
    public Result create(Word word) {
        word.setCreatedDate(new Date());
        word = wordService.create(word);
        return renderSuccess(word);
    }

    @ApiOperation(value = "新增、更新笔记", notes = "新增、更新笔记", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping("/word/note/{wordId}")
    public Result addNote(@RequestAttribute("userToken") UserToken userToken, @PathVariable Integer wordId, String note) {
        WordNote wordNote = new WordNote();
        wordNote.setUserId(userToken.getId());
        wordNote.setWordId(wordId);
        wordNote.setNote(note);
        wordNote = wordNoteService.save(wordNote);
        return renderSuccess(wordNote);
    }

    @ApiOperation(value = "更新单词", notes = "更新单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PutMapping("/word")
    public Result update(@RequestBody Word word) {
        word = wordService.update(word);
        return renderSuccess(word);
    }

    @ApiOperation(value = "删除单词", notes = "删除单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @DeleteMapping("/word/{id}")
    public Result delete(@PathVariable Integer id) {
        wordService.delete(id);
        return renderSuccess();
    }

    @ApiOperation(value = "导入单词", notes = "导入单词", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PostMapping("word/import")
    public Result importExcel(@RequestParam("file") MultipartFile file) throws AppException, IOException {
        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf("."));
        if (!ext.contains("zip")) {
            throw new AppException(MessageCode.ZIP_POSTFIX_ERROR);
        }
        String questionFolder = config.getUploadFolder() + "question" + "/";
        if (!new File(questionFolder).exists()) {
            new File(questionFolder).mkdirs();
        }
        byte[] bytes = file.getBytes();
        Path zipPath = Paths.get(questionFolder + file.getOriginalFilename());
        Files.write(zipPath, bytes);
        ZipUtil.unzip(zipPath.toFile(), questionFolder, null);
        String excelName = findExcelName(questionFolder);
        if (excelName == null) {
            throw new AppException(MessageCode.FILE_READ_ERROR);
        }
        wordService.importExcel(questionFolder, excelName);
        Files.delete(zipPath);
        return renderSuccess();
    }

    public String findExcelName(String dirPath) {
        File dir = new File(dirPath);
        // 该文件目录下文件全部放入数组
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(".xlsx") || file.getName().contains(".xls")) {
                    return file.getName();
                }
            }
        }
        return null;
    }

}
