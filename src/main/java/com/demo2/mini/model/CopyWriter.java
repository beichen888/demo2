package com.demo2.mini.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo2.user.model.UserLanguage;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by miguo on 2018/6/10.
 */
@Data
@Entity
@Table(name = "copy_writer", uniqueConstraints = @UniqueConstraint(columnNames = {"language", "wordKey"}))
public class CopyWriter implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    /**
     * 语言
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 8, nullable = false)
    @JsonIgnore
    private UserLanguage language;

    /**
     * key
     */
    @NotNull(message = "wordKey不能为空")
    @Column(length = 100, nullable = false)
    private String wordKey;

    /**
     * 文案内容
     */
    @Column(length = 255, nullable = false)
    private String content;
}
