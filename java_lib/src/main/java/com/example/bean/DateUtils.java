/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.bean;


import java.util.Calendar;

/**
 * Created by 肖稳华 on 2017/6/12.
 */

public class DateUtils {

	public static String getDayInfo(Calendar day){
		String showDate = "%s-%s-%s";
		int year = day.get(Calendar.YEAR);
		int monthOfYear = day.get(Calendar.MONTH);
		int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);
		//需要+1
		monthOfYear +=1;
		return String.format(showDate,
							 year,
							 (monthOfYear <= 9 ? "0" + monthOfYear : monthOfYear),
							 (dayOfMonth <= 9 ? "0" + dayOfMonth : dayOfMonth));
	}

	//获取开始的日期
	public static String getBeginDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) +1);
		calendar.set(Calendar.DATE,1);
		return getDayInfo2(calendar);
	}

	//获取结束的日期
	public static String getEndDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH) +1);
		calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDayInfo2(calendar);
	}

	public static String getDayInfo2(Calendar day){
		String showDate = "%s%s%s";
		int year = day.get(Calendar.YEAR);
		int monthOfYear = day.get(Calendar.MONTH);
		int dayOfMonth = day.get(Calendar.DAY_OF_MONTH);
		//需要+1
		monthOfYear +=1;
		return String.format(showDate,
							 year,
							 (monthOfYear <= 9 ? "0" + monthOfYear : monthOfYear),
							 (dayOfMonth <= 9 ? "0" + dayOfMonth : dayOfMonth));
	}

	//获取当前月的最后一天
	public static String getCurrentMonthEndDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return getDayInfo2(calendar);
	}

}
