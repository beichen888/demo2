package com.demo2.user.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum  UserLevel implements Serializable {
    ONE("青铜"), TWO("白银"), THREE("黄金"),EMPTY("");
    private String name;

    UserLevel(String name) {
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
