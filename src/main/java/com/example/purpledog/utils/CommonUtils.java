package com.example.purpledog.utils;

public class CommonUtils {
    public static boolean isTextNull(String text) {
        return (text == null || text.trim().equals("")) ? true:false;
    }
}
