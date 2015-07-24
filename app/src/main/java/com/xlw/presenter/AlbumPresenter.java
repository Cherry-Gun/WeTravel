package com.xlw.presenter;

import android.graphics.Bitmap;
import android.util.Log;

import com.xlw.db.LocationDBHelper;
import com.xlw.db.PhotoDBHelper;
import com.xlw.model.Location;
import com.xlw.model.Photo;
import com.xlw.utils.ImagesUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxsd on 2015/7/16.
 */
public class AlbumPresenter extends BasePresenter{

    IDetailView iDetailView;
    long position;
    private LocationDBHelper locationDBHelper;
    private PhotoDBHelper photoDBHelper;

    public void setView(IDetailView iDetailView,long position){
        this.iDetailView = iDetailView;
        this.position = position;
        locationDBHelper = new LocationDBHelper();
        photoDBHelper = new PhotoDBHelper();
    }

    public void getData(){
        List<Bitmap> list = new ArrayList<>();
        Log.d("position", position + "");
        List<Location> locations = locationDBHelper.queryLocation(" where TRIP_ID=?",String.valueOf(position));
        for(int i = 0; i<locations.size();i++){
            Location location = locations.get(i);
            List<Photo> photos = photoDBHelper.queryPhoto(" where LOCATION_ID=?", String.valueOf(location.getId()));
            for(int j = 0;j<photos.size();j++){
                Photo photo = photos.get(j);
                String path = photo.getUri();
                Log.d("path01",path);
                if(path.startsWith("/storage/emulated")){
                    Bitmap bitmap = ImagesUtil.loadBitmap(path, 150, 150);
                    Log.d("bitmap", bitmap + "");
                    list.add(bitmap);
                }
            }
        }
        iDetailView.getListBitMap(list);
    }

}
