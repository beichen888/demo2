package com.demo2.dashboard.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by miguo on 2018/7/18.
 */
@Data
public class WeUserProperties implements Serializable {
    private Integer id;
    private String name;
    private String value;
}
