package com.xlw.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.xlw.db.TripDBHelper;
import com.xlw.onroad.R;
import com.xlw.presenter.ITravelNewView;
import com.xlw.presenter.TravelNewPresenter;

import java.util.Date;

public class TravelNewActivity extends BaseActivity implements View.OnClickListener, ITravelNewView {

    //控件的变量
    Button btnGo;
    ImageView camera;

    TravelNewPresenter travelNewPresenter;    //主导器
    ProgressDialog progressDialog;
    Bitmap photoBitmap;   //照片

    com.xlw.model.Location location;  //坐标位置
    TripDBHelper tripDBHelper;

  //  LocationAsyncTask task;   //自定义异步任务类的对象
    LocationManagerProxy locationManagerProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_new);

        locationManagerProxy = LocationManagerProxy.getInstance(this);
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 5000, 0, myListener);

        travelNewPresenter = new TravelNewPresenter();
        travelNewPresenter.setView(this);

        initView();
    }

    private void initView() {
        //出发按钮--用于保存旅行标题、内容、第一张照片
        btnGo = (Button) findViewById(R.id.btn_go);
        btnGo.setOnClickListener(this);
        //相机按钮--点击拍旅行出发时的第一张照片
        camera = (ImageView) findViewById(R.id.camera);
        camera.setOnClickListener(this);
        //经纬度位置初始化
        location = new com.xlw.model.Location();
        tripDBHelper = new TripDBHelper();
    }

    double lat, lng;    //经纬度位置监听器--得到当前经纬度
    private AMapLocationListener myListener = new AMapLocationListener()    {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(myListener != null && aMapLocation != null ){
                lat= aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
            }
        }
        @Override
        public void onLocationChanged(Location location) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_go :    //出发“GO”按钮的点击事件
                //1.旅行游记标题
                EditText et01 = (EditText) findViewById(R.id.et01);     //找到用户输入标题的文本框
                String travelTitle = et01.getText().toString();         //得到用户输入的旅行日志标题
                if(TextUtils.isEmpty(travelTitle)) {                    //判断用户输入是否为空
                    Toast.makeText(this, "请命名这次旅行", Toast.LENGTH_SHORT).show();
                    et01.requestFocus();
                    return;
                }
                //2.旅行游记内容
                EditText et02 = (EditText) findViewById(R.id.et02);     //找到用户输入标题的文本框
                String travelDetails = et02.getText().toString();       //得到用户输入的旅行日志内容
                if(TextUtils.isEmpty(travelDetails)) {                  //判断用户输入是否为空
                    Toast.makeText(this, "出发前不想要说点什么吗？", Toast.LENGTH_SHORT).show();
                    et02.requestFocus();
                    return;
                }
                //3.出发时的第一个坐标
   //             task = (LocationAsyncTask) new LocationAsyncTask().execute();   //// 执行异步任务; execute()方法只能调用一次。
                location.setLat(String.valueOf(lat));
                location.setLng(String.valueOf(lng));
                location.setLocDate(new Date());

                travelNewPresenter.saveTripInfo(travelTitle, travelDetails, photoBitmap, location);  // 保存旅行信息(标题，日志内容，照片, 坐标)


                startActivity(new Intent(this, TravelMemoryActivity.class));               //保存信息后跳转页面
                break;
            case R.id.camera :
                //打开手机自带的相机功能
                openCamera();
        }
    }

    private void openCamera() {  //打开相机
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //启动照相程序
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //得到返回的照片
        Bundle bundle = data.getExtras();
        if(bundle != null) {
            photoBitmap = (Bitmap) bundle.get("data");
            displayPhoto(photoBitmap);   //显示照片
            //TODO 将刚拍的新照片替换系统默认的firstPhoto照片
        }
    }

    //坐标地址的异步任务
//    private class LocationAsyncTask extends AsyncTask<Void, Integer, com.xlw.model.Location> {
//
//        @Override
//        protected com.xlw.model.Location doInBackground(Void... params) {
//            location.setLat(String.valueOf(lat));
//            location.setLng(String.valueOf(lng));
//            location.setLocDate(new Date());
//            return location;
//        }
//
//        @Override
//        protected void onPostExecute(com.xlw.model.Location location) {
//            super.onPostExecute(location);
//        }
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // 通知主导器,本view已经加载显示完毕
        travelNewPresenter.viewFinishLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_travel_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void displayPhoto(Bitmap bitmap) {
        ImageView imageView = (ImageView)findViewById(R.id.firstPhoto);
        imageView.setImageBitmap(photoBitmap);
    }

    // 跳转到下一个
    @Override
    public void gotoNextActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this,0);
        progressDialog.setTitle("正在进行人脸识别...");
        progressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void showErrorMsg(String errorMsg) {
        Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendLocation(Double lat, Double lng) {
        travelNewPresenter.getLocation(lat, lng);
    }
}
