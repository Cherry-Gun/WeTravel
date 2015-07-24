package com.xlw.presenter;

import com.xlw.model.Photo;
import com.xlw.model.Trip;

import java.util.List;

/**
 * Created by xinliwei on 2015/7/9.
 */
public interface ITravelMemoryView {

    // 跳转到下一个Activity
    void gotoNextActivity(Class tClass);
    void getTrips(List<Trip> trips);
    void getPhotos(List<Photo> photos);
}
