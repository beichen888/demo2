package com.demo2.dashboard.model;

import com.demo2.common.MsgData;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Created by miguo on 2018/7/18.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class WeUserTrendData extends MsgData {

    private List<WeUserTrendDetail> list;
}
