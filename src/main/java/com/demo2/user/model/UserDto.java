package com.demo2.user.model;

import com.demo2.word.model.ChineseLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserDto {

    private Integer id;

    private String openid;

    private String avatar;

    private String nickname;

    private String phone;

    private Gender gender;

    private String country;

    private String province;

    private String city;

    private String language;

    private String birthday;

    private String labelIds;

    private ChineseLevel chineseLevel;

    private UserLevel userLevel;

    private String email;

    private Date createdDate;

    private Boolean active;

    private Role role;

    private List<String> labelNames;

    private int learnTotalDays;

    private int continueLearnDays;

    private List<Double> levelPercent;


}
