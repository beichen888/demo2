package com.demo2.user.model;

import java.io.Serializable;

/**
 * User: demo2
 * Date: 13-1-30
 * Time: 下午2:30
 */
public enum Role implements Serializable {
    ADMIN("管理员"), USER("普通会员");
    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
