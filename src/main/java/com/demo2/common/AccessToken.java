package com.demo2.common;

/**
 * Created by demo2 on 17/1/26.
 */
public class AccessToken extends MsgData{
    private String access_token;
    private String openid;
    private long expires_in;

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
