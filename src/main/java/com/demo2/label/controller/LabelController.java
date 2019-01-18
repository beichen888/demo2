package com.demo2.label.controller;

import com.demo2.common.BaseController;
import com.demo2.common.Result;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.label.model.Label;
import com.demo2.label.service.ILabelService;
import com.demo2.user.model.User;
import com.demo2.user.model.UserToken;
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

@RestController
@RequestMapping("/api")
@Auth
public class LabelController extends BaseController {
    @Resource
    private ILabelService labelService;

    @ApiOperation(value = "分页取得所有标签", notes = "分页取得所有标签", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping(value = "/labels")
    public Result getByPage(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                            @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate, String keyword) {
        Page<Label> labelPage = labelService.listByPage(pageable, keyword, startDate, endDate);
        return renderSuccess(labelPage);
    }

    @ApiOperation(value = "取得所有标签", notes = "取得所有标签", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @GetMapping(value = "/labels/all")
    public Result listAll() {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Label> labelList = labelService.list(sort);
        return renderSuccess(labelList);
    }

    @GetMapping("/label/{id}")
    public Result getLabel(@PathVariable Integer id) {
        Label label = labelService.find(id);
        return renderSuccess(label);
    }

    @PostMapping("/label")
    public Result create(@RequestAttribute("userToken") UserToken userToken, String name) {
        Label label = new Label();
        label.setName(name);
        if (userToken != null) {
            User user = new User();
            user.setId(userToken.getId());
            label.setCreator(user);
        }
        label = labelService.create(label);
        return renderSuccess(label);
    }

    @PutMapping("/label/{id}")
    public Result update(@RequestBody Label label) {
        label = labelService.update(label);
        return renderSuccess(label);
    }

    @DeleteMapping("/label/{id}")
    public Result delete(@PathVariable Integer id) {
        labelService.delete(id);
        return renderSuccess();
    }


}
