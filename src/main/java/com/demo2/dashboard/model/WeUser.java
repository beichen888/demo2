package com.demo2.dashboard.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by miguo on 2018/7/18.
 */
@Data
public class WeUser implements Serializable {
 private List<WeUserProperties> province;
 private List<WeUserProperties> city;
 private List<WeUserProperties> genders;
 private List<WeUserProperties> platforms;
 private List<WeUserProperties> devices;
 private List<WeUserProperties> ages;
}
