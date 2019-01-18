package com.demo2.common.exception;

import com.demo2.common.MessageUtil;
import com.demo2.common.MessageCode;
import com.demo2.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Created by shannon on 2014/9/14.
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Resource
    private MessageUtil messageUtil;

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public Result handleAppException(AppException ex){
        String msg = messageUtil.getMessage(ex.getMsgCode(), ex.getArgs());
        logger.error(msg, ex);
        return new Result<>(false, msg);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Result handleDataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException ex) throws DataIntegrityViolationException {
        logger.error(ex.getMessage(), ex);
        if(isAjax(request)){
            String msg = messageUtil.getMessage(MessageCode.DATA_INTEGRITY_VIOLATION_EXCEPTION_ERROR);
            return new Result<>(false, msg);
        }else{
            throw ex;
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) throws DataIntegrityViolationException {
        logger.error(ex.getMessage(), ex);
        if(isAjax(request)){
            String[] errorStr = new String[1];
            ex.getConstraintViolations().forEach(v->{
                errorStr[0] = v.getMessage();
            });
            return new Result<>(false, errorStr[0]);
        }else{
            throw ex;
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(HttpServletRequest request, Exception ex) throws Exception {
        logger.error(ex.getMessage(), ex);
        if(isAjax(request)){
            String msg = messageUtil.getMessage(MessageCode.SERVER_ERROR, new Object[]{ex.getMessage()});
            return new Result<>(false, msg);
        }else{
            throw ex;
        }
    }

    private boolean isAjax(HttpServletRequest request){
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        return isAjax || request.getRequestURI().contains("/api/");
    }
}
