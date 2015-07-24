package com.xlw.utils;

import com.xlw.exception.AndroidOnRoadException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by xinliwei on 2015/7/7.
 *
 * 网络下载工具类(通过HTTP协议)
 */
public class HttpUtil {
    public static byte[] getHttpResponseAsBytes(String url) throws AndroidOnRoadException{
        byte[] buffer = null;
        InputStream in = openHttpConnection(url);
        try {
            // 打开连接,获得输入流
            in = openHttpConnection(url);
            if(in != null){
                // 使用Apache Commons IO包中的工具类将字节流解析为字符串
                buffer = IOUtils.toByteArray(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AndroidOnRoadException("IO异常");
        }finally {
            // 关闭输入流
            IOUtils.closeQuietly(in);
        }
        return buffer;
    }

    // 下载文本内容的方法
    public static String getHttpResponseAsString(String url) throws AndroidOnRoadException {
        String content = "没有内容";
        InputStream in = null;
        try {
            // 打开连接,获得输入流
            in = openHttpConnection(url);
            if(in != null){
                // 使用Apache Commons IO包中的工具类将字节流解析为字符串
                content = IOUtils.toString(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AndroidOnRoadException("IO异常");
        }finally {
            // 关闭输入流
            IOUtils.closeQuietly(in);
        }

        return content;
    }

    //打开网络连接，并返回一个输入流
    public static InputStream openHttpConnection(String urlStr) throws AndroidOnRoadException  {
        InputStream in = null;
        int responseCode = -1;
        URL url;
        URLConnection conn;
        try {
            url = new URL(urlStr);
            conn = url.openConnection();
        } catch (IOException ex) {
            throw new AndroidOnRoadException("URL格式错误.请检查后重试!");
        }

        if (!(conn instanceof HttpURLConnection)) {
            throw new AndroidOnRoadException("非HTTP连接");
        }

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            conn.setConnectTimeout(15000);	// 设置连接超时的数值
            httpConn.setRequestMethod("GET");	// 设置请求的方法为GET
            httpConn.connect();			// 连接
            responseCode = httpConn.getResponseCode();	// 获得服务器返回的响应码
            if (responseCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();	// 获得输入流
            }else{
                throw new IOException("连接异常");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AndroidOnRoadException("连接错误");
        }

        return in;
    }
}
