package com.demo2.user.controller;

import com.demo2.common.Result;
import com.demo2.common.exception.AppException;
import com.demo2.common.security.interceptor.Auth;
import com.demo2.label.service.ILabelService;
import com.demo2.user.model.User;
import com.demo2.user.model.UserDto;
import com.demo2.user.model.UserQuery;
import com.demo2.user.service.IUserService;
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
@Auth
public class UserAdminController {
    @Resource
    private IUserService userService;
    @Resource
    private ILabelService labelService;

    @GetMapping(value = "/users")
    public Result list(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> users = userService.list(pageable, null);
        return new Result<>(true, users);
    }

    @GetMapping(value = "/usersByPage")
    public Result listByPage(UserQuery userQuery, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<User> users = userService.list(pageable, userQuery);
        return new Result<>(true, users);
    }

    @Auth("anon")
    @GetMapping(value = "/users/{id}")
    public Result getById(@PathVariable Integer id) throws AppException {
        UserDto userDto = userService.findUserDto(id);
        return new Result<>(true, userDto);
    }

    /**
     * 禁用用户
     */
    @PutMapping(value = "/user/disable/{id}")
    public Result disable(@PathVariable Integer id) {
        userService.disable(id);
        return new Result();
    }

    /**
     * 启用用户
     */
    @PutMapping(value = "/user/enable/{id}")
    public Result enable(@PathVariable Integer id) throws AppException {
        userService.enable(id);
        return new Result();
    }

    /**
     * 设置用户标签
     *
     * @param userId
     * @param labelIds
     * @return
     */
    @ApiOperation(value = "设置标签,标签[labelIds]用逗号连接", notes = "设置标签", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @PutMapping(value = "/user/setLabelIds/{userId}")
    public Result setLabels(@PathVariable Integer userId, @RequestParam("labelIds") String[] labelIds) {
        User user = userService.find(userId);
        if (user != null) {
            if (labelIds != null) {
                StringBuilder label = new StringBuilder();
                for (String labelId : labelIds) {
                    label.append(labelId).append(",");
                }
                label.deleteCharAt(label.length()-1);
                user.setLabelIds(label.toString());
            } else {
                user.setLabelIds(null);
            }
            userService.update(user);
        }
        return new Result();
    }
}
