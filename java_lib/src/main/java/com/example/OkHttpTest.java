/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example;

import com.example.bean.BookResult;
import com.example.bean.DateUtils;
import com.example.bean.SearchResult;
import com.example.bean.TextUtils;
import com.example.bean.TicketNumberResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * 作者：bowen_xiao
 * 时间：2017/7/21:20:09
 * 邮箱：
 * 说明：
 */
public class OkHttpTest {

    //抢的车次号 584-1
    //static  String mBuyLine = "P414-1";

    // 1312958账号 小马
//    static  String mBuyLine = "P628-1";
    static  String mBuyLine = "P414-1";
    static String mUserId = "94015";
    static String mPhoneNumber = "13129587498";
    static String mKeyCode = "143928ec87117ca4de85dc20e5c8a9ad";

    // 1705105账号
    /*
    static  String mBuyLine = "P113-1";
    static String mUserId = "179792";
    static String mPhoneNumber = "17051052812";
    static String mKeyCode = "b20740c94e131278c952dfc62f40a158";*/

    //170999账号
//    static  String mBuyLine = "P414-1";
//    static String mUserId = "84276";
//    static String mPhoneNumber = "17099946533";
//    static String mKeyCode = "844bda7f667fe7d1867596ebf4cc9413";

    //keyCode=a37da0fdf94154e86a1a555ef63d29ca&pageNo=1&pageSize=5&payStatus=2&userId=185707&userName=18718680620
    //吴雷 584-1
//    static  String mBuyLine = "584-1";
//    static String mUserId = "185707";
//    static String mPhoneNumber = "18718680620";
//    static String mKeyCode = "a37da0fdf94154e86a1a555ef63d29ca";

    //抢下个月的票true，还是当前月的票
    static boolean isNextMonth = true;
    //抢票间隔,一秒最少可以抢2次
    static int FAST_TIME = 600;
    public static void main(String[] args) throws Exception {
        postFrom();
        //查看订单信息
//        bookEnd();
    }

    static void postData() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://www.baidu.com")
                    .header("User-Agent", "My super agent")
                    .addHeader("Accept", "text/html")
                    .build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }

            System.out.println(response.header("Server"));
            System.out.println(response.headers("Set-Cookie"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void post2() {
        OkHttpClient client = new OkHttpClient();
        final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");
        final String postBody = "Hello World";

        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                return MEDIA_TYPE_TEXT;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(postBody);
            }

            @Override
            public long contentLength() throws IOException {
                return postBody.length();
            }
        };

        Request request = new Request.Builder()
                .url("http://192.168.13.93:8080")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("服务器端错误: " + response);
            }

            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void postFrom() {
        String url = "http://slb.szebus.net/bc/phone/data";
        Map<String, Object> params = new HashMap<>();
        params.put("lineNo", mBuyLine);
        params.put("pageNo", "1");
        params.put("pageSize", "5");

        Callback callback = (new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //System.out.println("result::" + e.getMessage());
                postFrom();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                //System.out.println("result::" + string);
                if (string != null && string.length() > 0) {
                    SearchResult searchResult = new Gson().fromJson(string, SearchResult.class);
                    //.toJson(string, SearchResult.class);
                    if (searchResult != null) {
                        if (searchResult.getReturnCode().equals("500")) {
                            List<SearchResult.ReturnDataBean> returnData = searchResult.getReturnData();
                            if (returnData != null && returnData.size() > 0) {
                                SearchResult.ReturnDataBean dataBean = returnData.get(0);
                                clickItem = dataBean;
                                checkTickNumber();
                                return;
                            }
                        }
                    }
                }
                postFrom();
            }
        });
        new NetWorkUtils().postFrom(url, params, callback);
    }

    static SearchResult.ReturnDataBean clickItem;

    //抢票模式检查票的数量
    private static void checkTickNumber() {

        java.util.Random random=new java.util.Random();
        int result= random.nextInt(FAST_TIME)+FAST_TIME;// 返回[0,10)集合中的整数，注意不包括10
        final Timer timer = new Timer();
        TimerTask task = new TimerTask (){
            public void run() {
                checkTicketNumberNetWork();
            }
        };
        if(isNextMonth){
            Calendar calendar = Calendar.getInstance();
            // 指定11：59：46点执行
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            calendar.set(Calendar.MINUTE, 59);
            //为了防止电脑时间与服务器时间不一致，提前准备
            calendar.set(Calendar.SECOND, 46);
            Date date = calendar.getTime();
            if(Calendar.getInstance().after(calendar)){
                timer.schedule(task, result);
            }else{
                //11：59：46启动抢票程序
                timer.schedule(task,date);
            }
        }else{
            //最快 600L 抢一次
            result = result * 3 + FAST_TIME;
            timer.schedule(task, result);
        }
    }

    private static void checkTicketNumberNetWork(){
        /**
         * customerId=179792
         * &customerName=17051052812
         * &keyCode=b20740c94e131278c952dfc62f40a158
         * &lineId=41650&vehTime=0725&beginDate=20170610&endDate=20170630
         **/
        Map<String, Object> params = new HashMap<>();
        params.put("customerId", mUserId);
        params.put("customerName", mPhoneNumber);
        params.put("keyCode", mKeyCode);
        params.put("lineId", clickItem.getLineId());
        params.put("vehTime", clickItem.getVehTime());
        //开始天数和结束天数,
        params.put("beginDate", isNextMonth ? DateUtils.getBeginDate() : DateUtils.getDayInfo2(Calendar.getInstance()));
        params.put("endDate",isNextMonth ?  DateUtils.getEndDate() : DateUtils.getCurrentMonthEndDate() );
        //请求参数信息
        String url = "http://slb.szebus.net/bc/phone/surplus/ticket/new";
        new NetWorkUtils().postFrom(url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                checkTickNumber();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if (string != null && string.length() > 0) {
                    TicketNumberResult searchResult = new Gson().fromJson(string, TicketNumberResult.class);
                    String tickets = searchResult.getReturnData().getTickets();
                    if(isNextMonth){
                        getBuyParam(tickets);
                    }else{
                        getBuyParam2(tickets);
                    }
                    return;
                }
                checkTickNumber();
            }
        });
    }


    //设置参数信息
    private static void getBuyParam2(String tickets){
        if(!TextUtils.isEmpty(tickets)){
            String[] split = tickets.split(",");
            List<String> dateList = new ArrayList<>();
            //获取票的数量
            Calendar instance = Calendar.getInstance();
            for (int i = 0; i < split.length; i++) {
                String ticketNumber = split[i];
                String dayInfo = DateUtils.getDayInfo(instance);
                //				ToolLog.e("dayInfo",dayInfo);
                //				ToolLog.e("ticketNumber",ticketNumber);

                try {
                    Integer ticketIntValue = Integer.valueOf(ticketNumber);
                    //周几
//					Calendar.SUNDAY  周末
                    int dayWeek = instance.get(Calendar.DAY_OF_WEEK);
                    if(ticketIntValue >=1){
                        dateList.add(dayInfo);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                //每次加一天,用于下一次计算
                instance.add(Calendar.DAY_OF_MONTH, 1);
            }

            if(dateList != null && dateList.size() != 0){
                //可以去买票,需要去掉所有空格
                String buyDate = dateList.toString().replaceAll(" ","");
                String price = String.valueOf(dateList.size() * 5.0f);
                buyDate = buyDate.substring(1,buyDate.length() -1);
                //根据价格购买
                bookTickByDate(buyDate,price);
//				ToastUtil.showToast(mActivity,"捡漏"+buyDate);
            }else{
                //检查剩余票的数量
                checkTickNumber();
            }
        }
    }


    //设置参数信息
    private static void getBuyParam(String tickets){
        if(!TextUtils.isEmpty(tickets)){
            String[] split = tickets.split(",");
            List<String> dateList = new ArrayList<>();
            //获取票的数量
            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.MONTH,instance.get(Calendar.MONTH) + 1);
            for (int i = 0; i < split.length; i++) {
                String ticketNumber = split[i];
                instance.set(Calendar.DATE,i+1);
                String dayInfo = DateUtils.getDayInfo(instance);
                //				ToolLog.e("dayInfo",dayInfo);
                //				ToolLog.e("ticketNumber",ticketNumber);

                try {
                    Integer ticketIntValue = Integer.valueOf(ticketNumber);
                    //周几
                    int dayWeek = instance.get(Calendar.DAY_OF_WEEK);
                    if(ticketIntValue >=1){
                        dateList.add(dayInfo.trim());
                    }
                    //不是周末就加入
                    //					if(dayWeek != Calendar.SATURDAY && dayWeek != Calendar.SUNDAY){
                    //						dateList.add(dayInfo);
                    //					}

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if(dateList != null && dateList.size() != 0){
                //可以去买票
                String buyDate = dateList.toString();
                StringBuilder dateStr = new StringBuilder();
                for (int i = 0; i < dateList.size(); i++) {
                    String dateInfo = dateList.get(i);
                    dateStr.append(dateInfo.trim());
                    if(i < dateList.size()){
                        dateStr.append(",");
                    }
                }
                String price = String.valueOf(dateList.size() * 5.0f);
                buyDate = buyDate.substring(1,buyDate.length() -1);
                //根据价格购买
                buyDate = URLDecoder.decode(dateStr.toString().trim());
				/* 可以下单*/
                bookTickByDate(buyDate,price);
            }else{
                //检查剩余票的数量
                checkTickNumber();
            }
        }
    }

    //去访问网络，订票  414-1 的信息
    private static void bookTickByDate(final String buyDate,final String price) {
        /**
         *   userId	179792
         userName	17051052812
         keyCode	b20740c94e131278c952dfc62f40a158
         saleDates	2017-06-19,2017-06-20,2017-06-21,2017-06-22,2017-06-23
         lineId	41651
         vehTime	1755
         startTime	1802
         onStationId	7383
         offStationId	7577
         tradePrice	25.0
         payType	3
         sztNo	362317893
         */
        Map<String, Object> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("userName",mPhoneNumber);
        params.put("keyCode",mKeyCode);
        //线路ID号
        params.put("lineId",clickItem.getLineId());
        //始发站的时间 1755 = 17:55 0725 = 07:25
        params.put("vehTime",clickItem.getVehTime());
        //上车站的时间
        params.put("startTime",clickItem.getStartTime());
        //上车站的ID编号
        params.put("onStationId",clickItem.getOnStationId());
        //下车站的编号
        params.put("offStationId",clickItem.getOffStationId());
        //付款方式，3为深圳通 2 微信 ，1支付宝
        params.put("payType","3");
        // 深圳通ID号
        params.put("sztNo","362317893");

        //需要配置的2个参数
        //1）乘车日期
        params.put("saleDates",buyDate);
        //2)车票总价，天数 * 票价
        params.put("tradePrice",price);
//		params.putAll(otherInfo);
        String url = "http://slb.szebus.net/order/phone/create";
        new NetWorkUtils().postFrom(url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                checkTickNumber();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(!TextUtils.isEmpty(string)){
                    BookResult s = new Gson().fromJson(string, BookResult.class);
                    if(s != null && s.getReturnCode().equals("500")){
                        System.err.println("订票成功");
                        //打印日志信息
                        bookEnd();
                        return;
                    }
                }
                checkTickNumber();
            }
        });
    }

    private static void bookEnd(){
        Map<String, Object> params = new HashMap<>();
        params.put("userId",mUserId);
        params.put("userName",mPhoneNumber);
        params.put("keyCode",mKeyCode);

        params.put("pageNo",1);
        params.put("pageSize",5);
        params.put("payStatus",1);
        String url = "http://slb.szebus.net/order/phone/main/data";
        new NetWorkUtils().postFrom(url, params, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //打印错误日志信息
                System.err.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                if(!TextUtils.isEmpty(string)){
                   System.out.println("book list : "+string);
                }
            }
        });
    }

}
