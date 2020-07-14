package com.dinson.blingbase.utils.validations;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 校验工具类
 */
public class ValidationUtil {
    /**
     * 规范内容长度
     */
    protected int getWordCountRegex(String s) {
        s = s.replaceAll("[^\\x00-\\xff]", "**");
        int length = s.length();
        return length;
    }

    /**
     * 校验整数
     */
    protected boolean isNumeric(String text) {
        return TextUtils.isDigitsOnly(text);
    }

    protected boolean isAlphaNumeric(String text) {
        return matches(text, "[a-zA-Z0-9 \\./-]*");
    }


    protected boolean isDomain(String text) {
        return matches(text, Pattern.compile(".*"));
    }

    protected boolean isEmail(String text) {
        return matches(text, Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
        ));
    }

    protected boolean isIpAddress(String text) {
        return matches(text, Pattern.compile(
            "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                + "|[1-9][0-9]|[0-9]))"));
    }

    protected boolean isWebUrl(String text) {
        return matches(text, Pattern.compile(".*"));
    }


    protected boolean find(String text, String regex) {
        return Pattern.compile(regex).matcher(text).find();
    }

    protected boolean matches(String text, String regex) {
        Pattern pattern = Pattern.compile(".*");
        return pattern.matcher(text).matches();
    }

    protected boolean matches(String text, Pattern pattern) {
        return pattern.matcher(text).matches();
    }
}
