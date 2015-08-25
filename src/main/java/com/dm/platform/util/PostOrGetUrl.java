package com.dm.platform.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class PostOrGetUrl {
	public static String getJsonp(String posturl,String parameters) throws IOException{
		 /**   
         * 首先要和URL下的URLConnection对话。 URLConnection可以很容易的从URL得到。比如： // Using   
         *  java.net.URL and //java.net.URLConnection   
         *   
         *  验证的的结果返回到另一个页面 
         *   
         */    
		URL url = new URL(posturl);
        URLConnection connection = url.openConnection();     
        /**   
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。   
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：   
         */    
        connection.setDoOutput(true);
        /**   
         * 最后，为了得到OutputStream，简单起见，把它约束在Writer并且放入POST信息中，例如： ...   
         */    
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");     
        out.write(parameters);  //向页面传递数据。post的关键所在！     
        // remember to clean up     
        out.flush();     
        out.close();     
        /**   
         * 这样就可以发送一个看起来象这样的POST：    
         * POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:   
         * text/plain Content-type: application/x-www-form-urlencoded   
         * Content-length: 99 username=bob password=someword   
         */    
        // 一旦发送成功，用以下方法就可以得到服务器的回应：     
        String sCurrentLine;     
        String sTotalString;     
        sCurrentLine = "";     
        sTotalString = "";     
        InputStream l_urlStream;     
        l_urlStream = connection.getInputStream();     
        // 传说中的三层包装阿！     
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(     
                l_urlStream));     
        while ((sCurrentLine = l_reader.readLine()) != null) {     
            sTotalString += sCurrentLine;     
        }     
        return sTotalString;
	}
	
	public static String getJsonp(String geturl) throws IOException {
		URL url = new URL(geturl);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url
				.openConnection();
		/**
		 * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
		 * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
		 */
		httpURLConnection.setDoOutput(true);// 打开写入属性
		httpURLConnection.setDoInput(true);// 打开读取属性
		httpURLConnection.setRequestMethod("GET");// 设置提交方法
		httpURLConnection.setConnectTimeout(50000);// 连接超时时间
		httpURLConnection.setReadTimeout(50000);
		httpURLConnection.connect();
		/**
		 * 这样就可以发送一个看起来象这样的POST： POST /jobsearch/jobsearch.cgi HTTP 1.0 ACCEPT:
		 */
		// 一旦发送成功，用以下方法就可以得到服务器的回应：
		String sCurrentLine;
		String sTotalString;
		sCurrentLine = "";
		sTotalString = "";
		InputStream l_urlStream;
		l_urlStream = httpURLConnection.getInputStream();
		// 传说中的三层包装阿！
		BufferedReader l_reader = new BufferedReader(new InputStreamReader(
				l_urlStream));
		while ((sCurrentLine = l_reader.readLine()) != null) {
			sTotalString += sCurrentLine;

		}
		return sTotalString;
	}
}
