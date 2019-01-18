package com.demo2.dashboard.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by miguo on 2018/9/29.
 */
@Data
public class WrongWord implements Serializable {
    private String vocabulary;
    private String level;
    private String wrongCnt;
}
