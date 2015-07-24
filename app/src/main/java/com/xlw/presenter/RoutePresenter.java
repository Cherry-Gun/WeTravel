package com.xlw.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.amap.api.maps2d.model.LatLng;
import com.xlw.db.FeelingDBHelper;
import com.xlw.db.LocationDBHelper;
import com.xlw.db.PhotoDBHelper;
import com.xlw.model.Feeling;
import com.xlw.model.Location;
import com.xlw.model.Photo;
import com.xlw.utils.ImagesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxsd on 2015/7/13.
 */
public class RoutePresenter extends BasePresenter {
    IRouteView iRouteView;
    long position;
    private LocationDBHelper locationDBHelper;
    private PhotoDBHelper photoDBHelper;
    private FeelingDBHelper feelingDBHelper;
    public void setView(IRouteView iRouteView,long position){
        this.iRouteView = iRouteView;
        this.position = position;
        locationDBHelper = new LocationDBHelper();
        photoDBHelper = new PhotoDBHelper();
        feelingDBHelper = new FeelingDBHelper();
    }
    public void getPoints(){
        List<LatLng> list = new ArrayList<>();
        List<Location> locations = locationDBHelper.queryLocation(" where TRIP_ID=?",String.valueOf(position));
        for(int i = 0; i<locations.size();i++){
            Location location = locations.get(i);
            String lat = location.getLat();
            String lng = location.getLng();
            LatLng latLng = new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
            list.add(latLng);
        }
        iRouteView.getPoints01(list);
    }

    public void getPath(){
        String path;
        List<Location> locations = locationDBHelper.queryLocation(" where TRIP_ID=?",String.valueOf(position));
        Location location = locations.get(0);
        List<Photo> photos = photoDBHelper.queryPhoto(" where LOCATION_ID=?", String.valueOf(location.getId()));
        path = photos.get(0).getUri();
        iRouteView.getPath01(path);
    }

    public void getFeeling(LatLng latLng,long position){
        String feeling="";
        List<Location> locations = locationDBHelper.queryLocation("where LAT=? and LNG=? and TRIP_ID and TRIP_ID=?",
                String.valueOf(latLng.latitude), String.valueOf(latLng.longitude),String.valueOf(position));
        for(int i = 0; i<locations.size();i++){
            Location location = locations.get(i);
            List<Feeling> feelings = feelingDBHelper.queryFeeling(" where LOCATION_ID=?", String.valueOf(location.getId()));
            for(int j = 0;j<feelings.size();j++){
                feeling = feeling+feelings.get(j).getContent()+"\n";
            }
        }
        iRouteView.getFeeling(feeling);
    }

    public void getBitmaps(LatLng latLng,long position){
        List<Bitmap> list = new ArrayList<>();
        List<Location> locations = locationDBHelper.queryLocation("where LAT=? and LNG=? and TRIP_ID=?",
                String.valueOf(latLng.latitude), String.valueOf(latLng.longitude),String.valueOf(position));
        for(int i = 0;i<locations.size();i++){
            Location location = locations.get(i);
            List<Photo> photos = photoDBHelper.queryPhoto(" where LOCATION_ID=?",location.getId().toString());
            for(int j = 0;j<photos.size();j++){
                Photo photo = photos.get(j);
                String path = photo.getUri();
                if(path.startsWith("/storage/emulated")){
                    Bitmap bitmap = ImagesUtil.loadBitmap(path, 150, 150);
                    Log.d("bitmap", bitmap + "");
                    list.add(bitmap);
                }
            }
        }
        iRouteView.getBitmaps(list);
    }
}
