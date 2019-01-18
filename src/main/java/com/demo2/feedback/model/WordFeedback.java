package com.demo2.feedback.model;

import com.demo2.user.model.User;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "word_feedback")
public class WordFeedback implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @ManyToOne(optional = false)
    @JoinColumn(name="create_user_id", referencedColumnName = "id")
    private User creator;

    /**
     * 反馈类型
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 4)
    private FeedbackType type;

    /**
     * 反馈内容
     */
    @NotBlank
    @Column(length = 600,nullable = false)
    private String content;

    /**
     * 配图
     */
    @Column(length = 500)
    private String picture;

    /**
     * 创建日期
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createDate;

    /**
     * 收录情况
     */
    private Boolean isIncluded;
}
