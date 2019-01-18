package com.demo2.course.model;

import java.io.Serializable;

public enum Will implements Serializable {
    YES("是"), NO("否"), NOTSURE("不确定");
    private String name;

    Will(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
