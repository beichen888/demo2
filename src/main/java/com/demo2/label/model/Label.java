package com.demo2.label.model;

import com.demo2.user.model.User;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "label")
public class Label implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @NotBlank(message = "标签名字不能为空")
    @Column(length = 20, nullable = false)
    @Length(max = 20, message = "标签名字长度不能超过20个字符")
    private String name;

    /**
     * 创建用户id
     */
    @OneToOne(optional = false)
    @JoinColumn(name="create_user_id", referencedColumnName = "id")
    private User creator;

    /**
     * 创建时间
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createdDate;
}
