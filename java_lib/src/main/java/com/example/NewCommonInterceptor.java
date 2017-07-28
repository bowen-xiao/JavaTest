/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * 作者：bowen_xiao
 * 时间：2017/7/26:12:09
 * 邮箱：
 * 说明：
 */
public class NewCommonInterceptor implements Interceptor {

    static String TAG = "NewCommonInterceptor";
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host());

        Request request = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        long startTime = System.currentTimeMillis();
        okhttp3.Response response = chain.proceed(chain.request());
        long endTime = System.currentTimeMillis();
        long duration=endTime-startTime;
        okhttp3.MediaType mediaType = response.body().contentType();
        String content = response.body().string();
        ToolLog.d(TAG,"\n");
        ToolLog.d(TAG,"----------Start----------------");
        ToolLog.d(TAG, "| "+request.toString());
        String method=request.method();
        if("POST".equals(method)){
            StringBuilder sb = new StringBuilder();
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                for (int i = 0; i < body.size(); i++) {
                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                }
                sb.delete(sb.length() - 1, sb.length());
                ToolLog.d(TAG, "| RequestParams:{"+sb.toString()+"}");
            }
        }
        Headers headers = response.headers();
//        Date: Mon, 24 Jul 2017 07:54:11 GMT
        if(headers != null){
            try {
                Date date = new Date(headers.get("Date"));
                DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ToolLog.d("|service time : "+ format2.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ToolLog.d(TAG, "| Response:" + content);
        ToolLog.d(TAG,"----------End:"+duration+"毫秒----------");
        return response.newBuilder()
                .body(okhttp3.ResponseBody.create(mediaType, content))
                .build();
    }

}
