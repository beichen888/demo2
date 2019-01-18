package com.demo2.wrongrecord.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.demo2.user.model.User;
import com.demo2.word.model.ChineseLevel;
import com.demo2.word.model.Word;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "wrong_word_record")
@JsonIgnoreProperties(ignoreUnknown = true)
public class WrongWordRecord implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    @NotNull(message = "用户Id不能为空")
    private User user;

    /**
     * 词汇id
     */
    @ManyToOne
    @JoinColumn(name="word_id")
    @NotNull(message = "词汇Id不能为空")
    private Word word;

    /**
     * 词汇等级
     */
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    private ChineseLevel wordLevel;

    /**
     * 正确次数
     */
    @Column(length = 4)
    private int correctCount;

    /**
     * 错误次数
     */
    @Column(length = 4)
    private int wrongCount;

    /**
     * 错误率
     */
    private transient Double wrongPercent;

    /**
     * 创建时间
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    /**
     * 更新时间
     */
    @NotNull
    private Date updateDate;
}
