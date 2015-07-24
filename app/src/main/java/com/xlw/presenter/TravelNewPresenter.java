package com.xlw.presenter;

import android.graphics.Bitmap;
import android.net.Uri;

import com.xlw.db.LocationDBHelper;
import com.xlw.db.PhotoDBHelper;
import com.xlw.db.TripDBHelper;
import com.xlw.model.Location;
import com.xlw.model.Photo;
import com.xlw.model.Trip;
import com.xlw.utils.ImagesUtil;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * Created by hxsd on 2015/7/9.
 */
public class TravelNewPresenter extends BasePresenter{

    ITravelNewView view;     // 代表视图activity

 //   com.xlw.model.Location location;
    Double lat, lng;

    public void setView(ITravelNewView view) {
        this.view = view;
    }

    public ITravelNewView getView(){
        return this.view;
    }

    public void getLocation(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    //保存旅行出发时的标题、内容、第一张照片
    public void saveTripInfo(String topic, String desc, Bitmap photoBitmap, Location location){
        view.showProgressDialog();

        //将照片uri保存到数据库中
        Trip trip = new Trip();
        trip.setTopic(topic);
        trip.setDesc(desc);
        trip.setCategory("国内游");
        trip.setStart(new Date());
        TripDBHelper tripDBHelper = new TripDBHelper();
        long tripId = tripDBHelper.saveTrip(trip);

        try {
            // 将照片保存在文件系统
            Uri uri = ImagesUtil.saveImage(photoBitmap);
            // 将照片uri保存到数据库中
            Photo photo = new Photo();
            photo.setUri(uri.toString());
            // ..... 此处构造照片的其他属性

            PhotoDBHelper photoDBHelper = new PhotoDBHelper();
            photoDBHelper.savePhoto(photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            view.showErrorMsg("照片无法保存");
        }

//        List<Trip> trips = tripDBHelper.loadAllTrip();
//        trip = trips.get(trips.size()-1);

//        view.sendLocation(lat, lng);
     //   location = new Location();
//        location.setLat(String.valueOf(lat));
//        location.setLng(String.valueOf(lng));
//        location.setLocDate(new Date());
//        location.setTripId(trip.getId());
        LocationDBHelper locationDBHelper = new LocationDBHelper();
        locationDBHelper.saveLocation(location);
         //    List<com.xlw.model.Location> locations = locationDBHelper.loadAllLocation();
        //      com.xlw.model.Location location = locations.get(locations.size() - 1);

        view.hideProgressDialog();
        // 跳转到地图页面
        view.gotoNextActivity();
    }



}
