package com.xlw.ui.activity;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.xlw.onroad.R;

public class StartupActivity extends ActionBarActivity {

    private boolean mIsHeads;
    private AnimatorSet mFlipper;
    private Bitmap mHeadsImage,mTailsImage;
    private ImageView mFlipImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                switchActivity();
            }

        }, 4000);

        //动画
        mHeadsImage = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
        mTailsImage = BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon);
        mFlipImage = (ImageView) findViewById(R.id.flip_image_set);
        mFlipImage.setImageBitmap(mHeadsImage);
        mIsHeads = true; // 状态:当前ImageView 中放的是heads.png
        mFlipper = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.flip);
        mFlipper.setTarget(mFlipImage);
        ObjectAnimator flipAnimator = (ObjectAnimator)mFlipper.getChildAnimations().get(0);
        flipAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            // 在动画执行期间,提供周期性的回调方法
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // getAnimatedFraction()返回当前动画已经完成的百分比
                if (animation.getAnimatedFraction() >= 0.25f && mIsHeads) {
                    mFlipImage.setImageBitmap(mTailsImage); // 沿Y 轴转动超过25%时,换图片
                    mIsHeads = false;
                }
                if (animation.getAnimatedFraction() >= 0.75f && !mIsHeads) {
                    mFlipImage.setImageBitmap(mHeadsImage); // 沿Y 轴转动超过75%时,换图片
                    mIsHeads = true;
                }
            }
        });

    }

//    //程序开始Logo自动翻转
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mFlipper.start();
//    }

    //点击Logo翻转
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            mFlipper.start();
            return true; // 截断事件的传播:我已经处理过了,你们不必再管这个事件
        }
        return super.onTouchEvent(event);
    }

    private void switchActivity() {
        SharedPreferences sp = this.getSharedPreferences("Translation.cfg", Context.MODE_PRIVATE);

        if(sp.getBoolean("isFirst", true)) {
            Intent intent = new Intent(this, GuideActivity.class);
            this.startActivity(intent);
            sp.edit().putBoolean("isFirst", false).commit();
        }else {
            Intent intent = new Intent(this, MenuActivity.class);
            this.startActivity(intent);
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startup, menu);
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
}
