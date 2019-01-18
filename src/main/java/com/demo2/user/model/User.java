package com.demo2.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.demo2.word.model.ChineseLevel;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * User: demo2
 * Date: 13-1-30
 * Time: 下午2:30
 */
@Entity
@Data
@Table(name = "users")
@JsonIgnoreProperties({"salt", "password"})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Length(max = 48)
    @Column(length = 48, unique = true)
    private String openid;

    @NotBlank
    @Length(max = 48)
    @Column(length = 48, nullable = false)
    private String password;

    /**
     * 头像
     */
    @Column(length = 500)
    private String avatar;

    /**
     * 昵称
     */
    @Column(length = 64)
    @Length(max = 64, message = "昵称长度不能超过64个字符")
    private String nickname;



    /**
     * 手机号码
     */
    @Length(max = 11, message = "手机号码不能超过11个字符")
    @Column(length = 11, unique = true)
    private String phone;

    /**
     * 性别(微信数据)
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Gender gender;

    /**
     * 国家(微信数据)
     */
    @Column(length = 20)
    private String country;

    /**
     * 用户所在省份(微信数据)
     */
    @Column(length = 20)
    private String province;

    /**
     * 用户所在城市(微信数据)
     */
    @Column(length = 20)
    private String city;

    /**
     * 用户的语言(微信数据)
     */
    @Column(length = 20)
    private String language;

    @Column(length = 10)
    private String birthday;

    /**
     * 用户标签
     */
    @Length(max = 50)
    @Column(length = 50)
    private String labelIds;

    /**
     * 用户汉语等级
     */
    @Column(length = 5)
    @Enumerated(EnumType.STRING)
    private ChineseLevel chineseLevel;

    /**
     * 用户等级
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private UserLevel userLevel;

    @Length(max = 64, message = "Email长度不能超过64个字符")
    @Column(length = 64)
    @Email(message = "Email格式不正确")
    private String email;


    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    private Boolean active;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private Role role;

    @NotBlank
    @Length(max = 16)
    @Column(length = 16)
    private String salt;

    public User() {
    }

    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

}
