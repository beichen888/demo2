package com.demo2.dashboard.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 访问趋势数据
 */
@Data
public class WeUserTrendDetail implements Serializable {
    // 时间： 如： "20170313"
    private String ref_date;
    // 打开次数
    private Integer session_cnt;
    // 访问次数
    private Integer visit_pv;
    // 访问人数
    private Integer visit_uv;
    // 新用户数
    private Integer visit_uv_new;
    // 人均停留时长 (浮点型，单位：秒)
    private Double stay_time_uv;
    // 次均停留时长 (浮点型，单位：秒)
    private Double stay_time_session;
    // 平均访问深度 (浮点型)
    private Double visit_depth;
}
