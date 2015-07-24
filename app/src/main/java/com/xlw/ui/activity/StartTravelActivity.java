package com.xlw.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xlw.onroad.R;

import java.io.File;

public class StartTravelActivity extends ActionBarActivity implements View.OnClickListener {

    //控件的变量
    EditText et01, et02;
    ImageView camera, photo;
    Button btn;

    //拍照功能的变量
    String fileName;  //文件名
    private final static String FILE_PATH = Environment.getExternalStorageDirectory() + "/onRoad/";
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_travel);

        initView();
    }

    private void initView() {
        et01 = (EditText) findViewById(R.id.et01);          //需要用户输入的旅游日志的标题
        et01.getText();
        et02 = (EditText) findViewById(R.id.et02);          //需要用户输入的旅游日志的内容
        et02.getText();
        camera = (ImageView) findViewById(R.id.camera);     //实现拍照功能的可点击的ImageView图标“camera”
        camera.setOnClickListener(this);
        photo = (ImageView) findViewById(R.id.photo);       //用于呈现拍下的照片的ImageView
        photo.setOnClickListener(this);
        btn = (Button) findViewById(R.id.btn_go);           //用于保存日志的所有文字和图片内容，并跳转到指定Activity
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.camera :
                //实现照相功能
                openCamera();  //打开相机
                break;
            case R.id.photo :
                //查看已拍照片
                break;
            case R.id.btn_go :
                //将旅游日志的标题、内容、照片等存入手机文件中
                break;
        }
    }

    private void openCamera() {  //打开相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = System.currentTimeMillis()+".jpg";
        File photo = new File(FILE_PATH,  fileName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        //启动照相程序
        startActivityForResult(intent, 0);
    }

    @Override   //当拍照程序返回结果时，会调用这个方法进行响应
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //获取返回的图像数据
        //Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        //将返回的图像存储到文件中并把路径存放到数据库中
        saveBitmapInDB(fileName);
    }

    //把图片的名称  和路径存放到数据库中
    private void saveBitmapInDB(String fileName) {
//        ContentValues cv = new ContentValues(2);
//        cv.put(DBHelper.BITMAP_NAME,fileName);
//        cv.put(DBHelper.BITMAP_URI, FILE_PATH + fileName);
//        //打开数据库连接
//        mDb = mHelper.getWritableDatabase();
//        mDb.insert(DBHelper.TABLE_NAME, null, cv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_travel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home :
                //返回到给地图加标记的Activity，并放弃写旅游日志
//                Intent intent = new Intent(this, MapActivity.class);
//                startActivity(intent);
                break;
            case R.id.action_settings :
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}
