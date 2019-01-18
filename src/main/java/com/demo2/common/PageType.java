package com.demo2.common;

import java.io.Serializable;

public enum PageType implements Serializable {
    INDEX("首页"), COURSE("课程页"), COM("企业课程页");
    private String name;

    PageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
