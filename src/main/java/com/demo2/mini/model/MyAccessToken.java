package com.demo2.mini.model;

/**
 * Created by miguo on 2018/6/27.
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.demo2.common.exception.AppException;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "access_token")
public class MyAccessToken {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @NotBlank
    @Column(length = 512, nullable = false)
    private String accessToken;

    @Column(nullable = false)
    @NotNull
    private Long expire;

    @NotNull
    @Column(nullable = false, updatable = false)
    private Date createdDate;

    public boolean isValid() throws AppException {
        long now = System.currentTimeMillis() / 1000;
        long created = createdDate.getTime() / 1000;
        return (now - created) <= (expire - 5);
    }
}
