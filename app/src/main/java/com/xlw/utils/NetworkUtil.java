package com.xlw.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by xinliwei on 2015/7/5.
 *
 * 网络工具类.需要网络权限:
 * <uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 */
public class NetworkUtil {

    /** 判断是否能连接上互联网
     *
     * @param context   上下文
     * @param ip        网络测试ip.Google公共DNS-"8.8.8.8", 百度公共DNS-"180.76.76.76";
     * @param port      端口号.若是公共商品号,为53
     * @return  boolean类型值,如果能连上互联网,返回true;否则,返回false
     */
    public static boolean isNetworkReachable(Context context, String ip, int port){
        final ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();

        // 如果连网络都没有,那就什么也不要说了
        boolean connected = (null != activeNetworkInfo) && activeNetworkInfo.isConnected();
        if(!connected){
            return false;
        }

        // 如果有网络的话,也有可能只是连接上了本地的无线wi-fi,所以要进一步测试
        // 检查我们是否能访问远端的一个服务器(通常是知名网站的DNS服务器)
        boolean routeExists = false;
        try{
            // 检查对公共DNS的连接(如Google,百度等)
//            InetAddress host = InetAddress.getByName("8.8.8.8");
            InetAddress host = InetAddress.getByName(ip);

            Socket socket = new Socket();
//            socket.connect(new InetSocketAddress(host,53),5000);
            socket.connect(new InetSocketAddress(host,port),5000);

            //如果没有异常抛出,说明该远端服务器存在
            routeExists = true;
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 如果有活动的网络,判断其状态:
        // DISCONNECTED、CONNECTING、CONNECTED、DISCONNECTING
        // 当状态返回CONNECTED时，认为网络是稳定的，我们可以使用它来访问远端的资源。
        return (connected && routeExists);
    }

    /** 判断是否wi-fi连接
     *
     * @param context 上下文
     * @return boolean类型返回值
     */
    public static boolean isWifiReachable(Context context){
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo current = manager.getActiveNetworkInfo();
        if(current == null) {
            return false;
        }
        return (current.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /** 判断mobile网络(移动数据网络)是否可用
     *
     * @param context 上下文
     * @return boolean类型值
     */
    public static boolean isMobileConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = manager.getActiveNetworkInfo();
        if (current == null) {
            return false;
        }

        return (current.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**获取网络连接类型
     *
     * @param context 上下文
     * @return int类型值,如果为-1,则表示无法获取网络连接信息
     */
    public static int getConnectedType(Context context) {
        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo current = manager.getActiveNetworkInfo();
        if (current == null) {
            return -1;
        }

        return current.getType();
    }

}
