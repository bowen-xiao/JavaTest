/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 肖稳华 on 2016/10/11.
 */
public class EbusInterceptor implements Interceptor {

    private static final SimpleDateFormat data = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
    @Override public Response intercept(Chain chain) throws IOException {
        //String marvelHash = ApiUtils.generateMarvelHash(mApiKey, mApiSecret);
        Request oldRequest = chain.request();

        Request.Builder locala = chain.request().newBuilder();
        Request localz1 = locala.build();
        HttpUrl locals = localz1.url();
        if ((!locals.url().toString().contains("http://eread.szebus.net/")) && (!locals.url().toString().contains("http://ewrite.szebus.net/")))
            return chain.proceed(localz1);
        locala.removeHeader("User-Agent").addHeader("User-Agent", "okhttp/3.3.0");
        locala.addHeader("platform", "android");
        locala.addHeader("version", "2.5.2");
        String str1 = String.valueOf(System.currentTimeMillis());
        String str2 = data.format(new Date());
        TreeMap localTreeMap = new TreeMap();
        StringBuilder localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append(locals.url().toString());
        localStringBuilder1.append("?t=");
        localStringBuilder1.append(str1);

        locala.url(locals.resolve(localStringBuilder1.toString()));
        RequestBody localaa = localz1.body();
        if ((!(localaa instanceof MultipartBody)) && ((localaa instanceof FormBody)))
        {
            FormBody localp = (FormBody)localaa;
            for (int i = 0; i < localp.size(); i++)
                localTreeMap.put(localp.name(i), localp.value(i));
        }
        localTreeMap.put("t", str1);
        StringBuffer localStringBuffer1 = new StringBuffer();
        Iterator localIterator = localTreeMap.entrySet().iterator();
        while (localIterator.hasNext())
        {
            Map.Entry localEntry = (Map.Entry)localIterator.next();
            localStringBuffer1.append((String)localEntry.getKey());
            localStringBuffer1.append("=");
            localStringBuffer1.append((String)localEntry.getValue());
            localStringBuffer1.append("&");
        }
        StringBuffer localStringBuffer2 = localStringBuffer1.deleteCharAt(-1 + localStringBuffer1.length());
        localStringBuffer2.append("|");
        localStringBuffer2.append(str2);
        localStringBuffer2.append("|zxw");
        String str3 = DecodeKey.stringMD5(localStringBuffer2.toString());
        locala.addHeader("token", str3);
        Request localz2 = locala.build();
        StringBuilder localStringBuilder2 = new StringBuilder();
        localStringBuilder2.append("size");
        localStringBuilder2.append(str3.length());
//        Log.w("http", localStringBuilder2.toString());
        return chain.proceed(localz2);
    }

    static void e(String tag,String logs){
        System.err.println(tag+"===="+logs);
        Calendar instance = Calendar.getInstance();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(instance.getTime());
        System.err.println("req time : " + dateStr);
    }

}