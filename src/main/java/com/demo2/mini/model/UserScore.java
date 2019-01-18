package com.demo2.mini.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by miguo on 2018/6/25.
 */
@Data
@Entity
@Table(name = "user_score")
public class UserScore implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @NotNull(message = "用户Id不能为空")
    @Column(length = 10, nullable = false, updatable = false)
    private Integer userId;

    /**
     * 分数
     */
    private int score;

    @Column(updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
}
