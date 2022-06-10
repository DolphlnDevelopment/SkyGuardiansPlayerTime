package com.dolphln.skyguardiansplayertimer.utils;

public class DoubleUtils {

    public static String removeExtra(double num) {
        String res = String.valueOf(num);
        int length = 2;

        if (res.length() > length){
            res = res.substring((res.length() - length), res.length()).equals(".0")
                    ? res.substring(0, (res.length() - length)) : res;
        }

        return res;
    }
}
