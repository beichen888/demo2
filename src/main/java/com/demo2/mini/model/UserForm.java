package com.demo2.mini.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by miguo on 2018/6/27.
 */
@Data
@Entity
@Table(name = "user_form")
public class UserForm {
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * 用户id
     */
    @NotNull(message = "用户Id不能为空")
    @Column(length = 10, nullable = false, updatable = false)
    private Integer userId;

    /**
     * formId
     */
    @NotBlank(message = "formId不能为空")
    @Column(length = 500,updatable = false)
    private String formId;

    @Column(updatable = false)
    private Date createdDate;
}
