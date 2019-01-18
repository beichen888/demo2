package com.demo2.question.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * 题目类型枚举
 */
public enum QuestionType implements Serializable {
     /** 给单词选图片，考察字形和场景的关联 */
    ONE("ONE")
    /** 给单词选词义，考察字形和字义的关联 */
    ,TWO("TWO"),
    /** 给发音选汉字，考察字音和字形的关联 */
    THREE("THREE"),
    /** 给发音选拼音，考察字音和拼音的关联*/
    FOUR("FOUR"),
    /** 给单词发音选词义，考察字音和字义的关联 */
    FIVE("FIVE"),
    /** 给例句选图片，考察字形和场景的关联(主题型) */
    SIX("SIX");

    private String name;

    QuestionType(String name) {
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
