package com.demo2.mini.service;


import com.demo2.common.exception.AppException;

public interface IMyAccessTokenService {

    String getAccessToken() throws AppException;
}
