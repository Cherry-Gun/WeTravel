package com.xlw.ui.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tandong.sa.bv.BelowView;
import com.tandong.sa.bv.BottomView;
import com.tandong.sa.slideMenu.SlidingMenu;
import com.tandong.sa.view.SmartListView;
import com.xlw.application.AppConfig;
import com.xlw.application.XlwApplication;
import com.xlw.onroad.R;
import com.xlw.ui.adapter.BVAdapter;
import com.xlw.ui.adapter.MenuAdapter;
import com.xlw.utils.UIUtil;

import java.util.ArrayList;

public class MenuActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    //三个可点击图片--旅行、路线、照片墙
    ImageView img01, img02, img03;

    //SlidingMenu..................
    private SlidingMenu menu;
    private SmartListView lv_menu;
    private MenuAdapter menuAdapter;
    private ArrayList<String> menus;
    private Animation animationMenu;
    private BottomView bv;
//    private BelowView blv;
//    private boolean bvIsShow = true;
    //SlidingMenu....................

    private BottomView themeBV;  //切换主题用的BottomView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();  //控件初始化
        initMenu();  //SlidingMenu初始化
//        setupActionBar();    //ActionBar初始化
    }

    private void initView() {
        img01 = (ImageView) findViewById(R.id.img01);
        img02 = (ImageView) findViewById(R.id.img02);
        img03 = (ImageView) findViewById(R.id.img03);
        img01.setOnClickListener(this);
        img02.setOnClickListener(this);
        img03.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.img01 :
                //转到“新的旅行”的Activity--TravelNewActivity
                startActivity(new Intent(this, TravelNewActivity.class));
                break;
            case R.id.img02 :
                //转到“路线规划”的Activity--TravelPlanningActivity
                startActivity(new Intent(this, TravelPlanningActivity.class));
                break;
            case R.id.img03 :
                //转到“照片墙”的Activity--TravelMemoryActivity
                startActivity(new Intent(this, TravelMemoryActivity.class));
                break;
        }
    }

    private void initMenu() {
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT_RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); //设置滑动的屏幕范围
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);  //设置阴影图片
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);   //主屏幕剩余宽度
        menu.setBehindWidth(450);         //SlidingMenu划出的宽度
        menu.setFadeDegree(0.35f);        //SlidingMenu滑动时的渐变程度
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT); //使SlidingMenu附加在Activity上
        menu.setMenu(R.layout.layout_menu);
        menu.setSecondaryMenu(R.layout.layout_menu);// 设置右侧菜单
        menu.setSecondaryShadowDrawable(R.drawable.shadow);// 设置右侧菜单阴影的图片资源
        lv_menu = (SmartListView) menu.findViewById(R.id.lv_menu);
        menu.setOnOpenListener(ool);
        lv_menu.setOnItemClickListener(this);
    }

    //添加SlidingMenu中的选项
    SlidingMenu.OnOpenListener ool = new SlidingMenu.OnOpenListener() {

        @Override
        public void onOpen() {
            menus = new ArrayList<String>();

            menus.add("登录/注册");
            menus.add("更换主题");
            menus.add("使用指南");
            menus.add("关于我们");
            menus.add("新的旅程");
            menus.add("线路规划");
            menus.add("我的回忆");
            menuAdapter = new MenuAdapter(MenuActivity.this, menus);
            lv_menu.setAdapter(menuAdapter);
            animationMenu = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.anim_menu_list);
            LayoutAnimationController lac = new LayoutAnimationController(animationMenu);
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lv_menu.setLayoutAnimation(lac);

        }
    };

    @Override     //显示点击后弹出的窗口
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        String menu = menus.get(position);
        if (menu.contains("登录/注册")) {
            Toast.makeText(this, "登录/注册", Toast.LENGTH_LONG).show();
            //TODO 登录界面
            dialog();
        } else if (menu.contains("更换主题")) {
            Toast.makeText(this, "更换主题", Toast.LENGTH_LONG).show();
            initThemeBottomView();
        } else if (menu.contains("使用指南")) {
            startActivity(new Intent(this, GuideActivity.class));
        } else if (menu.contains("关于我们")) {
            initBottomView();
        } else if (menu.contains("新的旅程")) {
            startActivity(new Intent(this, TravelNewActivity.class));
        } else if (menu.contains("线路规划")) {
            startActivity(new Intent(this, TravelPlanningActivity.class));
        } else if (menu.contains("我的回忆")) {
            startActivity(new Intent(this, TravelMemoryActivity.class));
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        // 使用up 箭头指示器显示home
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //"关于我们"的BottomView内容设置...............start.............................
    private void initBottomView() {
        ListView lv_menu_list;
        final ArrayList<String> menus = new ArrayList<String>();
        menus.add("合作洽谈请拨打:13910954654");
        menus.add("投诉电话:13810821865");
        menus.add("QQ联系:10390342");
        menus.add("微信联系:jason-white");
        menus.add("微游记非常重视您的使用体验与宝贵意见");

        bv = new BottomView(MenuActivity.this, R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
        bv.setAnimation(R.style.BottomToTopAnim);

        bv.showBottomView(true);
        lv_menu_list = (ListView) bv.getView().findViewById(R.id.lv_list);
        BVAdapter adapter = new BVAdapter(MenuActivity.this, menus);
        lv_menu_list.setAdapter(adapter);
        lv_menu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String s_menu = menus.get(arg2);
                if (s_menu.contains("合作洽谈请拨打:13910954654")) {
                    Toast.makeText(MenuActivity.this, "如遇忙线请稍后再拨，谢谢", Toast.LENGTH_LONG).show();
                } else if (s_menu.contains("投诉电话:13810821865")) {
                    Toast.makeText(MenuActivity.this, "如遇忙线请稍后再拨，谢谢", Toast.LENGTH_LONG).show();
                } else if (s_menu.contains("QQ联系:10390342")) {
                    Toast.makeText(MenuActivity.this, "如未能及时回复，请稍等，谢谢", Toast.LENGTH_LONG).show();
                } else if (s_menu.contains("微信联系:jason-white")) {
                    Toast.makeText(MenuActivity.this, "如未能及时回复，请稍等，谢谢", Toast.LENGTH_LONG).show();
                }
                bv.dismissBottomView();
            }
        });
    }
    //"关于我们"的BottomView内容设置...........................end.................................

    //主题切换的BottomView。。。。。start。。。。。。。
    private void initThemeBottomView() {
        ListView lv_theme_list;
        final ArrayList<String> menus = new ArrayList<String>();
        menus.add("MyGreenAppTheme");
        menus.add("MyBlueAppTheme");
        menus.add("MyPinkAppTheme");

        themeBV = new BottomView(MenuActivity.this, R.style.BottomViewTheme_Defalut, R.layout.bottom_view_theme);
        themeBV.setAnimation(R.style.BottomToTopAnim);

        themeBV.showBottomView(true);
        lv_theme_list = (ListView) themeBV.getView().findViewById(R.id.lv_list_theme);
        BVAdapter adapter = new BVAdapter(MenuActivity.this, menus);
        lv_theme_list.setAdapter(adapter);
        lv_theme_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String s_menu = menus.get(arg2);
                if (s_menu.contains("MyGreenAppTheme")) {
                    Toast.makeText(MenuActivity.this, "MyGreenAppTheme", Toast.LENGTH_LONG).show();
                    UIUtil.changeToTheme(MenuActivity.this, 0);
                } else if (s_menu.contains("MyBlueAppTheme")) {
                    Toast.makeText(MenuActivity.this, "MyGreenAppTheme", Toast.LENGTH_LONG).show();
                    UIUtil.changeToTheme(MenuActivity.this, 1);
                } else if (s_menu.contains("MyPinkAppTheme")) {
                    Toast.makeText(MenuActivity.this, "MyGreenAppTheme", Toast.LENGTH_LONG).show();
                    UIUtil.changeToTheme(MenuActivity.this, 2);
                }
                themeBV.dismissBottomView();

            }
        });
    }
    //主题切换的BottomView。。。。。.end。。。。。。。.

    protected void dialog() {
        AlertDialog.Builder builder = new Builder(this);

        builder.setTitle("注册/登录");
        builder.setMessage("用户名:");
//        builder.setTitle("提示");
//        if (mycount==1){
//            builder.setMessage("确认是否开启一次新的旅行吗？");
//            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    startActivity(new Intent(MainActivity.this,ThreeActivity.class));
////                Toast.makeText(MainActivity.this,"1--->OK",Toast.LENGTH_SHORT).show();
//                }
//            });
//            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//        }else {
//            builder.setMessage("确定要继续前面的旅行吗？");
//            builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    startActivity(new Intent(MainActivity.this,FithActivity.class));
////                Toast.makeText(MainActivity.this,"1--->OK",Toast.LENGTH_SHORT).show();
//                }
//            });
//            builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    startActivity(new Intent(MainActivity.this,ThreeActivity.class));
//                }
//            });
//        }


        builder.create().show();
    }


    //返回键的双击退出功能-->单击不退出只提示.............start............
    private long firstTime = 0;
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 800) {
                Toast.makeText(MenuActivity.this, "再按一次返回键退出...", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }
    //返回键的双击退出功能-->单击不退出只提示..............end...........

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home :
                menu.toggle();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
