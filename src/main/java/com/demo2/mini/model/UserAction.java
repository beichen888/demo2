package com.demo2.mini.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 用户是否做过某个操作
 */
@Entity
@Data
@Table(name = "user_action")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户id
     */
    @NotNull(message = "用户Id不能为空")
    @Column(length = 10, nullable = false, unique = true, updatable = false)
    private Integer userId;

    /**
     * 动作
     */
    @Column(length = 100, nullable = false)
    private String action;

    /**
     * 创建时间
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createdDate;
}
