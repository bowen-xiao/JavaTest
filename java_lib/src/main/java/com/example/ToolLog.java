/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

/**
 * 作者：bowen_xiao
 * 时间：2017/7/28:17:32
 * 邮箱：
 * 说明：
 */
public class ToolLog {
    public static void d(String tag, String s) {
        d(s);
    }

    public static void d( String s) {
        System.out.println(s);
    }
}
