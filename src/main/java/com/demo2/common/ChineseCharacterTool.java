package com.demo2.common;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by miguo on 2018/6/26.
 */
public  class ChineseCharacterTool {
    static String REGEX_CHINESE = "[\u4e00-\u9fa5]";
    /**
     * 移除字符串中的中文
     *
     * @param str
     * @return
     */
    public static String removeChineseCharacter(String str) {

        if (StringUtils.isBlank(str)) {
            return "";
        }
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(str);
        return mat.replaceAll("");
    }

    /**
     * 获取字符串中的中文
     *
     * @param str
     * @return
     */
    public static String getChineseCharacter(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        Pattern p = Pattern.compile(REGEX_CHINESE);
        Matcher m = p.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (m.find()) {
            sb.append(m.group());
        }
        return sb.toString();
    }
}
