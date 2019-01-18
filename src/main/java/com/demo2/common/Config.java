package com.demo2.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class Config {
    @Value("${web.domain}")
    private String domain;

    @Value("${upload.folder}")
    private String uploadFolder;

    @Value("${mail.host}")
    private String mailHost;

    @Value("${mail.username}")
    private String mailUsername;

    @Value("${mail.password}")
    private String mailPassword;

    @Value("${wx.appid}")
    private String appid;

    @Value("${wx.secret}")
    private String secret;

    @Value("${project.env}")
    private String env;
    @Value("${wx.templateId}")
    private String wxTemplateId;

    private final int hashIterations = 256;
    private final String hashAlgorithm = "SHA-1";
    private final int saltSize = 8;

    public int getHashIterations() {
        return hashIterations;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public int getSaltSize() {
        return saltSize;
    }

    public String getMailHost() {
        return mailHost;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getUploadFolder() {
        return checkSlash(uploadFolder);
    }

    public String getDomain() {
        return domain;
    }

    public String getAppid() {
        return appid;
    }

    public String getSecret() {
        return secret;
    }

    public String getEnv(){
        return env;
    }

    public String getWxTemplateId(){
        return wxTemplateId;
    }

    protected String checkSlash(String path){
        if(!path.endsWith(File.separator)){
            return path + "/";
        }else{
            return path;
        }
    }
}
