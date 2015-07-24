package com.xlw.ui.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.xlw.application.XlwApplication;
import com.xlw.utils.UIUtil;

/**
 * Created by xinliwei on 2015/7/4.
 * 使用SmartAndroid的2.42精简库.所谓精简库,即少了很多内容,如ORM.所以需要另外引入ORM
 */
//public abstract class BaseActivity  extends SmartActivity {
public abstract class BaseActivity  extends AppCompatActivity {

    // 上下文实例
    public Context context;

    // 应用全局的实例
    public XlwApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtil.onActivityCreateSetTheme(this);  // 设置theme

        context = getApplicationContext();
        application = XlwApplication.getInstance();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  //禁止横屏
    }

    /**
     * Activity回调接口声明
     */
    public interface OnActivityCallback{

    }
}
