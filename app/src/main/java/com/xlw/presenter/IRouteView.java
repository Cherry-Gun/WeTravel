package com.xlw.presenter;

import android.graphics.Bitmap;
import com.amap.api.maps2d.model.LatLng;
import java.util.List;

/**
 * Created by hxsd on 2015/7/13.
 */
public interface IRouteView {
    void getPoints01(List<LatLng> points);
    void getPath01(String path);
    void getFeeling(String feeling);
    void getBitmaps(List<Bitmap> bitmaps);
}
