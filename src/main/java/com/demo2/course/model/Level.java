package com.demo2.course.model;

import java.io.Serializable;

public enum Level implements Serializable {
    BEGINNER("新手"), ELEMENTARY("初级"), INTERMEDIATE("中级"), ADVANCED("高级");
    private String name;

    Level(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
