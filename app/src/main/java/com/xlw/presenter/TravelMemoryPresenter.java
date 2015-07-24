package com.xlw.presenter;

import android.util.Log;

import com.xlw.db.LocationDBHelper;
import com.xlw.db.PhotoDBHelper;
import com.xlw.db.TripDBHelper;
import com.xlw.model.Location;
import com.xlw.model.Photo;
import com.xlw.model.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinliwei on 2015/7/9.
 */
public class TravelMemoryPresenter extends BasePresenter {

    TripDBHelper tripDBHelper = new TripDBHelper();
    PhotoDBHelper photoDBHelper = new PhotoDBHelper();
    LocationDBHelper locationDBHelper = new LocationDBHelper();

    List<Trip> trips = tripDBHelper.loadAllTrip();

    List<Trip> trips01 =  new ArrayList<>();
    List<Trip> trips02 =  new ArrayList<>();
    List<Photo> photos01 = new ArrayList<>();
    List<Photo> photos02 = new ArrayList<>();
    List<Photo> photos03;
    List<Photo> photos04 = new ArrayList<>();
    ITravelMemoryView view;
    public void setView(ITravelMemoryView view) {
        this.view = view;
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            String category01 = trips.get(i).getCategory();
            if(category01.equals("境内游")){
                trips01.add(trip);
            }else if(category01.equals("境外游")){
                trips02.add(trip);
            }
        }
        for (int i = 1; i <= trips.size(); i++) {
            String category01 = trips.get(i-1).getCategory();
            List<Location> locations = locationDBHelper.queryLocation(" where TRIP_ID=?", String.valueOf(i));
            photos03 = photoDBHelper.queryPhoto(" where LOCATION_ID=?", locations.get(0).getId().toString());
            photos04.add(photos03.get(0));
            if(category01.equals("境内游")){
                photos01.add(photos03.get(0));
            }else if(category01.equals("境外游")){
                photos02.add(photos03.get(0));
            }
        }
    }

    public ITravelMemoryView getView() {
        return this.view;
    }

    // 跳转到视图的下一个activity
    public void gotoNextView(Class tClass) {
        view.gotoNextActivity(tClass);
    }
    List<Trip> trips04;
    public void getTrips01(String category){
        if(category.equals("境内游")){
            trips04 = trips01;
        }else if(category.equals("境外游")){
            trips04 = trips02;
        }else if(category.equals("全部")){
            trips04 = tripDBHelper.loadAllTrip();
        }
        view.getTrips(trips04);
    }
    List<Photo> photos;
    public void getPhotos01(String category){
        if(category.equals("境内游")){
            photos = photos01;
        }else if(category.equals("境外游")){
            photos = photos02;
        }else if(category.equals("全部")){
            Log.d("photos.size()", photos04.size() + "");
            photos = photos04;
        }
        view.getPhotos(photos);
    }
}