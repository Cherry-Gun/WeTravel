package com.xlw.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by xinliwei on 2015/7/6.
 */
public interface IVolleyCallback {

    void responseString(String responseText);

    void responseJSONObject(JSONObject jsonObject);

    void responseJsonArray(JSONArray jsonArray);
}
