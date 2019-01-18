package com.demo2.user.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * User: demo2
 * Date: 13-1-30
 * Time: 下午2:30
 */
public enum Gender implements Serializable {

    MALE("male"), FEMALE("female"),PRIVATE("private");
    private String name;

    Gender(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
