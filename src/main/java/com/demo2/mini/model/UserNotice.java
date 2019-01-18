package com.demo2.mini.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by miguo on 2018/6/25.
 */
@Data
@Entity
@Table(name = "user_notice")
public class UserNotice implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @NotNull(message = "用户Id不能为空")
    @Column(length = 10, nullable = false, unique = true, updatable = false)
    private Integer userId;

    /**
     * 提醒时间
     */
    private String noticeTime;
}
