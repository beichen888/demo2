package com.demo2.mini.model;

import lombok.Data;

import java.util.Map;

/**
 * 封装微信模板消息类
 */
@Data
public class WxTemplate {

    private String touser;

    private String template_id;

    private String page;

    private String form_id;

    private Map<String, TemplateData> data;

    private String color;
}
