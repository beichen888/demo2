package com.demo2.mini.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "learn_record")
public class LearnRecord implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "用户Id不能为空")
    @Column(length = 10, nullable = false, updatable = false)
    private Integer userId;

    /**
     * 当前测试是否完成
     */
    @Column(length = 1, nullable = false)
    private Boolean finish;

    @NotNull(message = "创建日期不能为空")
    @Column(updatable = false)
    private Date createdDate;

    @NotNull(message = "更新日期不能为空")
    private Date updateDate;

    /**
     * 是否练习
     */
    private boolean practice = false;

    /**
     * 上一轮记录id
     */
    private Integer preRecordId;

    /**
     * 第几轮
     */
    private int round;


}
