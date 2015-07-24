package com.xlw.ui.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlw.model.Photo;
import com.xlw.model.Trip;
import com.xlw.onroad.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by hxsd on 2015/7/10.
 */
public class ListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    Context context;
    List<Trip> tripList;
    List<Photo> photoList;
    SimpleDateFormat simpleDateFormat01;
    SimpleDateFormat simpleDateFormat;
    public ListAdapter(Context context, List<Trip> trips, List<Photo> photos){
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        simpleDateFormat01 = new SimpleDateFormat("yyyy年");
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        tripList = trips;
        photoList = photos;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = photoList.get(position);
        String path = photo.getUri();
        Log.d("path", path);
        Trip trip = tripList.get(position);
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_memory,null);
            Holder holder = new Holder();
            holder.year = (TextView)convertView.findViewById(R.id.year);
            holder.lstImage = (ImageView)convertView.findViewById(R.id.lstImage);
            holder.lstTopic = (TextView)convertView.findViewById(R.id.lstTopic);
            holder.lstTime = (TextView)convertView.findViewById(R.id.lstTime);
            holder.lstDesc = (TextView)convertView.findViewById(R.id.lstDesc);
            holder.line = (TextView)convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        }
        Holder holder = (Holder)convertView.getTag();
        String time01 = simpleDateFormat01.format(trip.getStart());
        holder.year.setText(time01);
        if(path.equals("没有照相")){
            holder.lstImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            holder.lstImage.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        holder.lstTopic.setText(trip.getTopic());
        String time = simpleDateFormat.format(trip.getStart());
        holder.lstTime.setText(time);
        holder.lstDesc.setText(trip.getDesc());
        Trip last = position == 0 ? null : tripList.get(position - 1);
        if (last == null || !simpleDateFormat01.format(last.getStart()).equals(time01) ) {
            holder.year.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
        } else {
            holder.year.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        }
        return convertView;
    }
    class Holder{
        public TextView year;
        public ImageView lstImage;
        public TextView lstTopic;
        public TextView lstDesc;
        public TextView lstTime;
        public TextView line;
    }
}
