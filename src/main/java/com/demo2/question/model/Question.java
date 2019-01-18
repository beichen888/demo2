package com.demo2.question.model;

import com.demo2.word.model.ChineseLevel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "question", uniqueConstraints = @UniqueConstraint(columnNames = {"wordId", "type"}))
public class Question implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 单词
     */
    @Column(length = 11, nullable = false, updatable = false)
    private Integer wordId;

    /**
     * 类型
     */
    @Column(length = 5, nullable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    /**
     * 单词级别
     */
    @Column(length = 5, nullable = false,updatable = false)
    @Enumerated(EnumType.STRING)
    private ChineseLevel level;

    /**
     * 题干
     */
    @Column(length = 255)
    @Length(max = 255, message = "题干长度不能超过255个字符")
    private String stem;

    /**
     * 选项（json格式）
     */
    @NotEmpty(message = "选项不能为空")
    @Column(columnDefinition = "text")
    private String optionLst;

    /**
     * 答案
     */
    @Column(length = 255)
    private String answer;

    /**
     * 提示1
     */
    private String reminder1;

    /**
     * 提示2
     */
    private String reminder2;

    private Date updateDate;

    @Column(updatable = false)
    private Date createdDate;

    private transient double correctPercent;


}
