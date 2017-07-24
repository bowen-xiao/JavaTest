/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.bean;

/**
 * 作者：bowen_xiao
 * 时间：2017/7/24:11:24
 * 邮箱：
 * 说明：
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        boolean result = true;
        if(str != null && str.length()>0){
            result = false;
        }
        return result;
    }
}
