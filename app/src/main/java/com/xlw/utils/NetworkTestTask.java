package com.xlw.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by xinliwei on 2015/7/5.
 *
 * 异步任务类，检测网络是否能访问互联网
 */
public class NetworkTestTask extends AsyncTask<Object,Void,Boolean> {

    Context context;

    NetworkTestTask(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Object... params) {
        boolean isNetworkReachable =
                NetworkUtil.isNetworkReachable(context, (String)params[0], (Integer)params[1]);
        return isNetworkReachable;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if(aBoolean){
            Toast.makeText(context, "网络状况良好", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("目前无网络连接");
            builder.setMessage("网络不可用,请稍后重试.");
            builder.setPositiveButton("确定",null);
            builder.create().show();
        }
    }
}
