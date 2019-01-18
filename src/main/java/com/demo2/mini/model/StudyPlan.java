package com.demo2.mini.model;

import com.demo2.user.model.UserLanguage;
import com.demo2.word.model.ChineseLevel;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 学习计划model
 *
 * @author master
 */

@Data
@Entity
@Table(name = "study_plan", uniqueConstraints = @UniqueConstraint(columnNames = {"userId"}))
public class StudyPlan implements Serializable {

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
     * 用户母语
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private UserLanguage userLanguage;


    /**
     * 学习的等级
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 5, nullable = false)
    private ChineseLevel chineseLevel;

    /**
     * 每天背单词数
     */
    @NotNull(message = "每天背单词数不能为空")
    @Max(9999)
    @Column(length = 4)
    private Integer wordCntPerDay;

    /**
     * 完成天数
     */
    @NotNull(message = "完成天数不能为空")
    @Max(9999)
    @Column(length = 4)
    private Integer dayCnt;

    /**
     * 学习偏好(是否学习汉字)
     */
    @Column(length = 1)
    private Boolean isLearnChinese;
    /**
     * 创建时间
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    private Date updateDate;

}
