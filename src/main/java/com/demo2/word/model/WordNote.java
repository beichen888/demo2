package com.demo2.word.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by miguo on 2018/6/24.
 */
@Data
@Entity
@Table(name = "word_Note")
public class WordNote implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer wordId;

    @NotBlank(message = "笔记内容不能为空 ")
    @Column(columnDefinition = "text")
    @Length(max = 500,message = "笔记不能超过500字")
    private String note;

    /**
     * 创建日期
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createDate;

}
