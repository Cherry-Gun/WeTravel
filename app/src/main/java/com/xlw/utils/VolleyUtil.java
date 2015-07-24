package com.xlw.utils;

import com.tandong.sa.vl.Request;
import com.tandong.sa.vl.Response;
import com.tandong.sa.vl.VolleyError;
import com.tandong.sa.vl.toolbox.JsonArrayRequest;
import com.tandong.sa.vl.toolbox.JsonObjectRequest;
import com.tandong.sa.vl.toolbox.StringRequest;
import com.xlw.application.XlwApplication;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/5.
 */
public class VolleyUtil {

    IVolleyCallback callback;

    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    JsonArrayRequest jsonArrayRequest;

    public VolleyUtil(IVolleyCallback callback){
        this.callback = callback;
    }

    // 请求服务器端字符串
    public Request requestString(String url_str){
        // 创建一个StringRequest对象
        stringRequest = new StringRequest(Request.Method.GET,url_str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(callback != null){
                            callback.responseString(s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                    }
        });

        // 将request请求对象添加到requestQueue请求队列
        XlwApplication.requestQueue.add(stringRequest);

        return stringRequest;
    }

    // 请求JSON格式字符串
    public Request requestJSONObject(String url_str){
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url_str, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // 服务器端正确响应时,回调此函数
                        if(callback != null){
                            callback.responseJSONObject(jsonObject);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 服务器端未正确响应时,回调此函数

            }
        });

        // 将request请求对象添加到requestQueue请求队列
        XlwApplication.requestQueue.add(jsonObjectRequest);

        return jsonObjectRequest;
    }

    // 请求JSON格式数组字符串
    public Request requestJsonArray(String url_str){
        // 创建一个StringRequest对象
        jsonArrayRequest = new JsonArrayRequest(url_str,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        // 服务器端正确响应时,回调此函数
                        callback.responseJsonArray(jsonArray);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 服务器端未正确响应时,回调此函数
            }
        });

        // 第三步,将request请求对象添加到requestQueue请求队列
        XlwApplication.requestQueue.add(jsonArrayRequest);

        return jsonArrayRequest;
    }

    public void cancelRequest(Request request){
        if(request != null){
            request.cancel();
        }
    }
}
