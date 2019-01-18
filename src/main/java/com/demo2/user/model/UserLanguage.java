package com.demo2.user.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * 语言枚举
 */
public enum UserLanguage implements Serializable {
    CHINESE("CHINESE"), ENGLISH("ENGLISH"), KOREAN("KOREAN"),JPANESE("JPANESE"),SPANISH("SPANISH");
    private String name;

    UserLanguage(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
