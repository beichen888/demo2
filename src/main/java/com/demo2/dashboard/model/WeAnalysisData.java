package com.demo2.dashboard.model;

import com.demo2.common.MsgData;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by miguo on 2018/7/18.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class WeAnalysisData extends MsgData {
    private String ref_date;
    //新用户
    private WeUser visit_uv_new;
    //活跃用户
    private WeUser visit_uv;
}
