package com.boot.common.utils;

/**
 * IntegerUtils
 *
 * @author EPL
 * @date 2019/12/29 0029
 */
public class IntegerUtils {

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
