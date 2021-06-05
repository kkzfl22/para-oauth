package com.paraview.oauth.utils;

public class StringUtil {

    public static boolean startWith(String str1, String str2) {
        if (str1 == null || str2 == null) {
            return false;
        }
        char[] ch1 = str1.toCharArray();
        char[] ch2 = str2.toCharArray();
        if (ch2.length > ch1.length) {
            return false;
        }
        for (int i = 0; i < ch2.length; i++) {
            if (ch2[i] != ch1[i]) {
                return false;
            }
        }
        return true;
    }

}
