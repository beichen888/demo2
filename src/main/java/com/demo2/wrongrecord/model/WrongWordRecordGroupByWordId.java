package com.demo2.wrongrecord.model;

import com.demo2.word.model.ChineseLevel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by miguo on 2018/7/3.
 */
@Data
public class WrongWordRecordGroupByWordId implements Serializable {

    private String wordId;

    private String vocabulary;

    private ChineseLevel wordLevel;

    private int wrongCount;
}
