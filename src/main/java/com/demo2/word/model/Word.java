package com.demo2.word.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "word")
public class Word implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * hsk序号
     */
    @NotNull(message = "hsk序号不能为空")
    @Column(length = 4, nullable = false,updatable = false)
    @Max(value = 99999, message = "hsk序号不能超过99999")
    private Integer hskNo;

    /**
     * 等级 对应code表的汉语等级
     */
    @Column(length = 5, nullable = false)
    @Enumerated(EnumType.STRING)
    private ChineseLevel level;

    /**
     * 优先级
     */
    @Column(length = 5)
    @Max(value = 99999, message = "优先级不能超过99999")
    private Integer priority;

    /**
     * 词汇
     */
    @NotBlank(message = "词汇不能为空")
    @Column(length = 100, nullable = false)
    @Length(max = 100, message = "词汇长度不能超过100个字符")
    private String vocabulary;

    /**
     * 词性1
     */
    //@NotBlank(message = "词性1不能为空")
    @Column(length = 50)
    @Length(max = 50, message = "词性1长度不能超过50个字符")
    private String kind1;

    /**
     * 词义1
     */
    //@NotBlank(message = "词义1不能为空")
    @Column(columnDefinition = "text")
    private String mean1;

    /**
     * 词性2
     */
    //@NotBlank(message = "词性2不能为空")
    @Column(length = 50)
    @Length(max = 50, message = "词性2长度不能超过50个字符")
    private String kind2;

    /**
     * 词义2
     */
    //@NotBlank(message = "词义2不能为空")
    @Column(columnDefinition = "text")
    private String mean2;

    /**
     * 例句
     */
    //@NotBlank(message = "例句不能为空")
    @Column(columnDefinition = "text")
    private String sentence;

    /**
     * 例句释义
     */
    //@NotBlank(message = "例句释义不能为空")
    @Column(columnDefinition = "text")
    private String sentenceMean;

    /**
     * 图片分组
     */
    private String imageGroup;

    /**
     * 图片路径
     */
    private String imagePath;

    /**
     * 单词音频路径
     */
    private String vocabularyAudioPath;

    /**
     * 例句音频路径
     */
    private String sentenceAudioPath;

    /**
     * 关联词（等级）
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 5)
    private ChineseLevel relatedWordLevel;

    /**
     * 关联词（拼音）
     */
    private String relatedWordPinyin;

    /**
     * 关联词（词义）
     */
    private String relatedWordMean;

    /**
     * 备注
     */
    private String remark;

    @Column(updatable = false)
    private Date createdDate;

    /**
     * 更新时间
     */
    private Date updateDate;

    private transient Boolean kept;

    private transient String note;
}
