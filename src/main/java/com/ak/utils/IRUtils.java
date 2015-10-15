package com.ak.utils;

/**
 * Created by olko06141 on 15.10.2015.
 */
public class IRUtils {

    public static String normalize(String word) {
        if (word == null) return "";
        if (word.endsWith(".") || word.endsWith(":")) {
            word = word.substring(0, word.length() - 1);
        }
        return word.toLowerCase();
    }
}
