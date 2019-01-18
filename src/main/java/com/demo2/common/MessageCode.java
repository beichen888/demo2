package com.demo2.common;

/**
 * User: demo2
 * Date: 13-4-15
 * Time: 下午3:01
 */
public enum MessageCode implements IMsgCode {
    SERVER_ERROR,
    HEX_DECODE_EXCEPTION_ERROR,
    DATA_INTEGRITY_VIOLATION_EXCEPTION_ERROR,
    FILE_POSTFIX_ERROR,
    ZIP_POSTFIX_ERROR,
    FILE_READ_ERROR,
    FILE_WRITE_ERROR,
    SMS_SEND_ERROR,

    PERMISSION_ERROR,

    USER_GET_AUTHENTICATION_INFO_ERROR,
    USER_NOT_EXIST_ERROR,
    USER_PLAN_NOT_EXIST_ERROR,
    USER_NEW_PASSWORD_BLANK_ERROR,
    USER_TWICE_PASSWORD_NOT_EQUAL_ERROR,
    USER_OLD_PASSWORD_ERROR,
    USER_PASSWORD_BLANK_ERROR,
    USER_DISABLED_ERROR,
    USER_PHONE_EXIST_ERROR,

    FILENAME_TOO_LONG,
    WORD_LEVEL_EMPTY_ERROR,
    HKS_NO_EMPTY_ERROR,
    PRIORITY_EMPTY_ERROR,
    PRIORITY_FORMAT_ERROR,
    VOCABULARY_EMPTY_ERROR,
    WORD_IMAGE_NOT_EXIST_ERROR,
    AUDIO_WORD_NOT_EXIST_ERROR,
    AUDIO_SENTENCE_NOT_EXIST_ERROR,
    // 小程序
    MINI_GET_ACCESS_TOKEN_ERROR,
    FORM_ID_NOT_EXIST_ERROR,
}