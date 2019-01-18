package com.demo2.mini.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo2.word.model.ChineseLevel;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 记录单词学习顺序以及是否已经学习了
 */

@Data
@Entity
@Table(name = "user_word",uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "wordId"}))
public class UserWord implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @JsonIgnore
    @Column(length = 11, nullable = false, updatable = false)
    @NotNull(message = "用户id不能为空")
    private Integer userId;

    /**
     * 单词id
     */
    @JsonIgnore
    @Column(length = 11, nullable = false, updatable = false)
    @NotNull(message = "单词id不能为空")
    private Integer wordId;

    private Boolean learned = false;

    //private Boolean selected = false;

    @Column(length = 5, nullable = false)
    @Enumerated(EnumType.STRING)
    private ChineseLevel level;

    /**
     * 优先级
     */
    @Column(length = 5)
    @Max(value = 99999, message = "优先级不能超过99999")
    private Integer priority;
    
    private Date learnedDate;

    @Column(updatable = false, nullable = false)
    private Date createDate;
}
