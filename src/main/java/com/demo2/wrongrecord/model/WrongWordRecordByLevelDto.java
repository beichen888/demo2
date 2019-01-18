package com.demo2.wrongrecord.model;

import com.demo2.word.model.ChineseLevel;
import lombok.Data;

import java.io.Serializable;

@Data
public class WrongWordRecordByLevelDto implements Serializable {

    /**
     * 等级
     */
    private ChineseLevel level;

    /**
     * 错误次数
     */
    private Integer count;

    /**
     * 错误率
     */
    private Double wrongPercent;
}
