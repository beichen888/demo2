package com.demo2.mini.model;

import com.demo2.question.model.Question;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by miguo on 2018/6/19.
 */
@Data
@Entity
@Table(name = "question_result")
public class QuestionResult {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "学习记录Id不能为空")
    @Column(length = 10, nullable = false, updatable = false)
    private Integer recordId;

    @ManyToOne(optional = false)
    @JoinColumn(name="question_id", referencedColumnName = "id")
    private Question question;

    private boolean practice = false;

    private boolean newWord = false;

    /**
     * 是否正确
     */
    private Boolean correct;

    @NotNull(message = "创建日期不能为空")
    @Column(updatable = false)
    private Date createdDate;
}
