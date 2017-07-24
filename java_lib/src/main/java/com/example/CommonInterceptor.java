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
import java.util.Calendar;
import java.util.Date;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by 肖稳华 on 2016/10/11.
 */
public class CommonInterceptor implements Interceptor {
    private final String mApiKey;
    private final String mApiSecret;

    public CommonInterceptor(String apiKey, String apiSecret) {
        mApiKey = apiKey;
        mApiSecret = apiSecret;
    }

    @Override public Response intercept(Chain chain) throws IOException {
        //String marvelHash = ApiUtils.generateMarvelHash(mApiKey, mApiSecret);
        Request oldRequest = chain.request();

        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                //公共参数，app版本号
                 //.addQueryParameter("appVersion", Parameters.appVersion)
                //添加公共的参数信息,这里不需要
                //.addQueryParameter("token", token)
//                .addQueryParameter(MarvelService.PARAM_TIMESTAMP, ApiUtils.getUnixTimeStamp())
//                .addQueryParameter(MarvelService.PARAM_HASH, marvelHash)
                ;
        //cookie 需要添加到公共参数信息
        //PersistentCookieStore cookieStore = new PersistentCookieStore(MyApplication.getContext());//取cookie
        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

        long t1 = System.nanoTime();
        Response response = chain.proceed(newRequest);
        long t2 = System.nanoTime();
        MediaType contentType = null;
        String bodyString = null;
        if (response.body() != null) {
            contentType = response.body().contentType();
            bodyString = response.body().string();
        }
        // 请求响应时间
        double time = (t2 - t1) / 1e6d;

        Headers headers = response.headers();
//        Date: Mon, 24 Jul 2017 07:54:11 GMT
        if(headers != null){
            try {
                Date date = new Date(headers.get("Date"));
                DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                System.out.println("service time : "+ format2.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //打印请求日志 需要关闭日志
        e("retrofit--> \n",
                  String.format("requestUrl : %s " +
                                "\n Time : %s " +
                                "\n Header : %s " +
                                "\n response Code : %s " +
                                "\n response Header : %s" +
                                "\n response body : %s",
                                newRequest.url(),
                                time,
                                newRequest.headers(),
                                response.code(),
                          headers,
                                (bodyString)));

//        return chain.proceed(newRequest);
        if (response.body() != null) {
            // get完body后原ResponseBody会被清空，需要重新设置body
            ResponseBody body = ResponseBody.create(contentType, bodyString);
            return response.newBuilder().body(body).build();
        } else {
            return response;
        }
    }

    static void e(String tag,String logs){
        System.err.println(tag+"===="+logs);
        Calendar instance = Calendar.getInstance();
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(instance.getTime());
        System.err.println("req time : " + dateStr);
    }
}