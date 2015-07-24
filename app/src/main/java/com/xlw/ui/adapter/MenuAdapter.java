package com.xlw.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xlw.onroad.R;

import java.util.ArrayList;


public class MenuAdapter extends BaseAdapter {

    private Context c;
    public static ArrayList<String> ss;

    public MenuAdapter(Context context, ArrayList<String> str) {
        this.c = context;
        this.ss = str;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ss.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return ss.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(c, R.layout.item_menu, null);
            holder = new ViewHolder();
            holder.tv_menu = (TextView) convertView.findViewById(R.id.tv_menu);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s = ss.get(position);
        holder.tv_menu.setText(s);
        if (s.contains("登录/注册")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.users);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        } else if (s.contains("更换主题")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.changetheme_btn);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        } else if (s.contains("使用指南")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.useway);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        } else if (s.contains("关于我们")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.aboutus);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        } else if (s.contains("新的旅程")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.travelnew);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        } else if (s.contains("线路规划")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.travelline);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        } else if (s.contains("我的回忆")) {
            Drawable drawableHome = c.getResources().getDrawable(R.mipmap.travelmemory);
            drawableHome.setBounds(0, 0, drawableHome.getMinimumWidth(), drawableHome.getMinimumHeight());
            holder.tv_menu.setCompoundDrawables(drawableHome, null, null, null);
        }

        if (s.contains("更换主题")) {
            holder.tv_number.setVisibility(View.VISIBLE);
        } else {
            holder.tv_number.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_menu;
        TextView tv_number;
    }
}
