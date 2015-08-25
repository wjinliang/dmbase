package com.dm.platform.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class DmDateUtil {
	public static String DateToStr(Date date,String format){
		SimpleDateFormat formatter = new SimpleDateFormat(
				format);
		return formatter.format(date);
	}
	public static String DateToStr(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	public static String Current(){
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return formatter.format(new Date());
	}
	public static String getBetweenDayNumber(String dateA, String dateB) {
		if (dateA != null && !dateA.equals("") && dateB != null
				&& !dateB.equals("")) {
			long dayNumber = 0;
			long mins = 60L * 1000L;
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				java.util.Date d1 = df.parse(dateA);
				java.util.Date d2 = df.parse(dateB);
				dayNumber = (d2.getTime() - d1.getTime()) / mins;
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(dayNumber>60){
				if(dayNumber>1440){
					return dateA;
				}else{
					return "今天 " + dateA.substring(11,16);
				}
			}else{
				if(dayNumber==0){
					return "刚刚";
				}else{
					return dayNumber+"分钟之前";
				}
			}
		} else {
			return "?分钟之前";
		}
	}
	
	public static String formatDateTime(String time){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(time==null ||"".equals(time)){
			return "";
		}
		java.util.Date date = null;
		try {
			 date = format.parse(time);
		} catch (Exception e) {
			return "";
		}
		
		Calendar current = Calendar.getInstance();
		
		Calendar today = Calendar.getInstance();	//今天
		
		today.set(Calendar.YEAR, current.get(Calendar.YEAR));
		today.set(Calendar.MONTH, current.get(Calendar.MONTH));
		today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
		//  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
		today.set( Calendar.HOUR_OF_DAY, 0);
		today.set( Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		
		Calendar yesterday = Calendar.getInstance();	//昨天
		
		yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
		yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
		yesterday.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)-1);
		yesterday.set( Calendar.HOUR_OF_DAY, 0);
		yesterday.set( Calendar.MINUTE, 0);
		yesterday.set(Calendar.SECOND, 0);
		
		current.setTime(date);
		
		if(current.after(today)){
			return "今天 "+time.split(" ")[1].substring(0,5);
		}else if(current.before(today) && current.after(yesterday)){
			
			return "昨天 "+time.split(" ")[1].substring(0,5);
		}else{
			return time.split(" ")[0];
		}
	}
}
