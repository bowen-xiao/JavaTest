/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by bowen
 * on 2019/4/23
 */
public class DecodeKey
{

    public static String stringMD5(String input) {

        try {

            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）

            MessageDigest messageDigest =MessageDigest.getInstance("MD5");



            // 输入的字符串转换成字节数组

            byte[] inputByteArray = input.getBytes();



            // inputByteArray是输入字符串转换得到的字节数组

            messageDigest.update(inputByteArray);



            // 转换并返回结果，也是字节数组，包含16个元素

            byte[] resultByteArray = messageDigest.digest();



            // 字符数组转换成字符串返回

            return byteArrayToHex(resultByteArray);



        } catch (NoSuchAlgorithmException e) {

            return null;

        }
    }

    //下面这个函数用于将字节数组换成成16进制的字符串

    public static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符

        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };



        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））

        char[] resultCharArray =new char[byteArray.length * 2];



        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去

        int index = 0;

        for (byte b : byteArray) {

            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];

            resultCharArray[index++] = hexDigits[b& 0xf];

        }



        // 字符数组组合成字符串返回

        return new String(resultCharArray);
    }


    private static final String[] a = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static String a(byte paramByte)
    {
        if (paramByte < 0)
            paramByte += 256;
        int i = paramByte / 16;
        int j = paramByte % 16;
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append(a[i]);
        localStringBuilder.append(a[j]);
        return localStringBuilder.toString();
    }

    public static String a(String paramString)
    {
        try
        {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes("UTF-8"));
            String str = a(localMessageDigest.digest());
            return str;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return paramString;
    }

    public static String a(byte[] paramArrayOfByte)
    {
        StringBuilder localStringBuilder = new StringBuilder();
        int i = paramArrayOfByte.length;
        for (int j = 0; j < i; j++)
            localStringBuilder.append(a(paramArrayOfByte[j]));
        return localStringBuilder.toString();
    }
}