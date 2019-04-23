/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 作者：bowen_xiao
 * 时间：2017/7/24:10:37
 * 邮箱：
 * 说明：
 */
public class NetWorkUtils {

    static long TIME_OUT_SECOND = 6;

    public void postFrom(String url , Map<String,Object> params,final Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.sslSocketFactory(sslSocketFactory)
                .proxy(Proxy.NO_PROXY)//禁用代理
                //新的拦截器，设置核心内容
                .addInterceptor(new EbusInterceptor())
                .addInterceptor(new CommonInterceptor("11","11"))
                .connectTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
                .build();

        FormBody.Builder builder = new FormBody.Builder();

        if (params != null) {
            //增强for循环遍历
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
//                .url("http://slb.szebus.net/bc/phone/data")
                .url(url)
                .post(requestBody)
                //.addHeader("token", "helloworldhelloworldhelloworld")
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
