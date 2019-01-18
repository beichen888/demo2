package com.demo2.word.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public enum ChineseLevel implements Serializable {
    HSK1("HSK1/A1"), HSK2("HSK2/A2"), HSK3("HSK3/B1"),HSK4("HSK4/B2"),HSK5("HSK5/C1"), HSK6("HSK6/C2"), EMPTY("");
    private String name;

    ChineseLevel(String name) {
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
