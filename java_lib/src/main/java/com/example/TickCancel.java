/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 作者：bowen_xiao
 * 时间：2017/7/27:12:14
 * 邮箱：
 * 说明：取消订单页面
 */
public class TickCancel {

    public  static void main(String args[]){
        cancelOrder();
    }

    //退票信息,账号信息
    static String mCancelUserName = "13129587498";
    static String mCancelUserCode = "143928ec87117ca4de85dc20e5c8a9ad";
    static String mCancelUserID = "94015";
    //需要退的订单ID号
    static String mOrderId = "4442555";

    private static void cancelOrder(){
        //id=4372171&userName=13129587498&userId=94015&keyCode=143928ec87117ca4de85dc20e5c8a9ad&saleDates=
        String url = "http://slb.szebus.net/order/phone/cancel";
        Map<String, Object> params = new HashMap<>();
        params.put("id", mOrderId);
        params.put("userName", mCancelUserName);
        params.put("userId", mCancelUserID);
        params.put("keyCode", mCancelUserCode);
        params.put("saleDates", "");

        Callback callback = (new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("result::" + e.getMessage());
                //postFrom();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                System.out.println("result::" + string);
            }
        });
        new NetWorkUtils().postFrom(url, params, callback);
    }
}
