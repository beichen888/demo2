package com.demo2.course.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 试听课报名学生
 */
@Entity
public class TrialClassStudent implements Serializable{
    @Id
    @GeneratedValue
    private Integer id;

    private Integer trialClassId;

    @Column(length = 64)
    @NotBlank
    private String name;

    @Column(length = 11)
    @NotBlank
    private String phone;

    @Column(length = 32)
    @NotBlank
    private String email;

    @Column(length = 18)
    private String nationality;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Level level;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Will will;

    private Date createdDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTrialClassId() {
        return trialClassId;
    }

    public void setTrialClassId(Integer trialClassId) {
        this.trialClassId = trialClassId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Will getWill() {
        return will;
    }

    public void setWill(Will will) {
        this.will = will;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
