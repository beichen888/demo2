package com.demo2.user.model;

import com.demo2.word.model.ChineseLevel;
import lombok.Data;

import java.util.Date;

@Data
public class UserQuery {
    private String nikeOrPhone;
    public Integer labelId;
    private Date start;
    private Date end;
    private ChineseLevel level;
    private UserLevel userLevel;
    private Gender gender;

}
