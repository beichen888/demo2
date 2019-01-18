package com.demo2.common.exception;

import com.demo2.common.IMsgCode;

/**
 * User: demo2
 * Date: 12-12-28
 * Time: 下午9:10
 */
public class AppException extends Exception{
    private Object[] args;
    private IMsgCode msgCode;

    public AppException(){

    }

    public AppException(IMsgCode msgCode, Object... args){
        this.args = args;
        this.msgCode = msgCode;
    }

    public Object[] getArgs() {
        return args;
    }

    public IMsgCode getMsgCode() {
        return msgCode;
    }
}
