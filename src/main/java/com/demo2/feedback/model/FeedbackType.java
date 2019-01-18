package com.demo2.feedback.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by miguo on 2018/6/13.
 */
public enum FeedbackType {
    USER("用户反馈"), WORD("单词报错");
    private String name;

    FeedbackType(String name) {
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
