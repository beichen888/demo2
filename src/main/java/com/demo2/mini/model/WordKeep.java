package com.demo2.mini.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 单词收藏
 */
@Data
@Entity
@Table(name = "word_keep", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "wordId"}))
public class WordKeep implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
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

    /**
     * 创建时间
     */
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    private transient Word word;

    @JsonIgnore
    @Column(nullable = false, updatable = false)
    private String pinyin;
    @JsonIgnore
    @Column(length = 5, nullable = false)
    @Enumerated(EnumType.STRING)
    private ChineseLevel level;

    private Boolean isNew = true;
}
