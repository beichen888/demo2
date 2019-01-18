package com.demo2.user.model;

import java.io.Serializable;

/**
 * Created by demo2 on 15/11/30.
 */
public class UserToken implements Serializable{
    private Integer id;
    private String phone;
    private String role;
    private long datetime;

    private UserToken(){}

    public UserToken(Integer id, String role, String phone, long datetime){
        this.id = id;
        this.role = role;
        this.phone = phone;
        this.datetime = datetime;
    }

    public Integer getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public long getDatetime() {
        return datetime;
    }
}
