package com.xlw.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.PolylineOptions;
import com.tandong.sa.bv.BottomView;
import com.xlw.application.AppConfig;
import com.xlw.onroad.R;
import com.xlw.presenter.IRouteView;
import com.xlw.presenter.RoutePresenter;
import com.xlw.ui.adapter.AlbumAdapter;
import com.xlw.ui.view.GalleryFlow;
import com.xlw.utils.ImagesUtil;
import java.util.List;

public class RouteActivity extends ActionBarActivity implements IRouteView, View.OnClickListener {

    private MapView map02;
    private AMap aMap;
    LocationManagerProxy locationManagerProxy;
    double lat;
    double lng;
    LatLng latLng;
    AMap.InfoWindowAdapter infoWindowAdapter;
    Marker marker;
    private GalleryFlow galleryFlow2;
    ProgressDialog progressDialog;
    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation != null){
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                latLng = new LatLng(lat,lng);
            }
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
    RoutePresenter routePresenter;
    long position;
    List<LatLng> points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("微游记");

        map02 = (MapView)findViewById(R.id.map02);
        map02.onCreate(savedInstanceState);


        locationManagerProxy = LocationManagerProxy.getInstance(this);
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 5000, 0, aMapLocationListener);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载页面...");
        progressDialog.show();

        init();
        setMap();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getLong("position");

        routePresenter = new RoutePresenter();
        routePresenter.setView(this, position);

        routePresenter.getPoints();

        if(points != null && points.size() > 0){
            drawPolyLine(points);
        }

    }
    public void setMap(){
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.hideInfoWindow();
                }
                Toast.makeText(RouteActivity.this,latLng.latitude + "" + latLng.longitude,Toast.LENGTH_SHORT).show();
            }
        });
        infoWindowAdapter = new AMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_window,null);
                render(marker,infoWindow);
                return infoWindow;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        };
        aMap.setInfoWindowAdapter(infoWindowAdapter);

    }
    LatLng point;
    String path;
    public void render(Marker marker,View view){


        ImageView infoImage = (ImageView)view.findViewById(R.id.info_image);
        routePresenter.getPath();
        if(path.startsWith("/storage/emulated")){
            try {
                Bitmap bitmap = ImagesUtil.loadBitmap(path, 150, 150);
                infoImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            infoImage.setImageResource(R.mipmap.ic_launcher);
        }


        point = marker.getPosition();


        String title = marker.getTitle();
        TextView titleUi = (TextView)view.findViewById(R.id.info_title);
        if(titleUi != null){
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.BLUE),0,titleText.length(),0);
            titleUi.setTextSize(14);
        }else{
            titleUi.setText("空的");
        }


        String snippet = marker.getSnippet();
        TextView snippetUi = (TextView)view.findViewById(R.id.info_snippet);
        if(snippet != null){
            SpannableString spannableText = new SpannableString(snippet);
            spannableText.setSpan(new ForegroundColorSpan(Color.rgb(100,200,100)),0,
                    spannableText.length(),0);
            snippetUi.setTextSize(12);
            snippetUi.setText(snippet);
        }else{
            snippetUi.setText("空的");
        }


        String city = marker.getPosition().latitude + "," + marker.getPosition().longitude;
        TextView cityUi = ((TextView) view.findViewById(R.id.info_city));
        if (city != null) {
            SpannableString cityText = new SpannableString(city);
            cityText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                    cityText.length(), 0);
            cityUi.setTextSize(10);
            cityUi.setText(cityText);
        } else {
            cityUi.setText("");
        }


        ImageButton photoButton = (ImageButton)view.findViewById(R.id.btn_photo);
        photoButton.setOnClickListener(this);

        ImageButton editButton = (ImageButton) view.findViewById(R.id.btn_edit);
        editButton.setOnClickListener(this);
    }



    public void init(){
        if(aMap == null){
            aMap = map02.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                    progressDialog.dismiss();
                }
            });
        }
    }
    public void drawPolyLine(List<LatLng> points){
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(points);
        polylineOptions.width(15);
        polylineOptions.color(Color.rgb(255, 120, 60));
        aMap.addPolyline(polylineOptions);
        addMarksToMap(points);
        points.clear();
    }



    public void addMarksToMap(List<LatLng> points){
        for(int i = 0;i<points.size();i++){
            if(i == 0 || i==points.size()-1) {
                addMarkToMap(points.get(i), AppConfig.MARKER_COLOR[0]);
            }else if(i>AppConfig.MARKER_COLOR.length){
                addMarkToMap(points.get(i), AppConfig.MARKER_COLOR[points.size()-1]);
            }else{
                addMarkToMap(points.get(i), AppConfig.MARKER_COLOR[i]);
            }
        }
    }
    String address;
    public void addMarkToMap(final LatLng point,float markerColor){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("我在这里");
        markerOptions.snippet(address);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(markerColor));
        markerOptions.draggable(false);
        marker = aMap.addMarker(markerOptions);
        marker.setObject("1001");
        marker.showInfoWindow();
    }



    @Override
    protected void onResume() {
        super.onResume();
        map02.onResume();
        routePresenter.viewFinishLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map02.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map02.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map02.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_route, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case android.R.id.home:
                Intent intent = new Intent(RouteActivity.this,TravelMemoryActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }




    View view;
    BottomView bottomView;
    String f;
    List<Bitmap> list;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_edit:
                bottomView = new BottomView(this, R.style.BottomViewTheme_Defalut, R.layout.bottom_view_detail);
                bottomView.setAnimation(R.style.BottomToTopAnim);
                bottomView.showBottomView(false);

                view = bottomView.getView();
                TextView feeling = (TextView)view.findViewById(R.id.talk1);
                routePresenter.getFeeling(point, position);
                if(f.length()==0){
                    feeling.setText("没有发表说说");
                }else{
                    feeling.setText(f);
                }
                break;
            case R.id.btn_photo:
                bottomView = new BottomView(this, R.style.BottomViewTheme_Defalut, R.layout.bottom_view_talk);
                bottomView.setAnimation(R.style.BottomToTopAnim);
                bottomView.showBottomView(false);

                View view = bottomView.getView();
                routePresenter.getBitmaps(point, position);
                if(list.size()==0){
                    Toast.makeText(this, "没有照片", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("list01.size",list.size()+"");
                    AlbumAdapter adapter = new AlbumAdapter(this, list);
                    adapter.createReflectedImages();//创建倒影效果
                    galleryFlow2 = (GalleryFlow)view.findViewById(R.id.galleryFlow2);
                    galleryFlow2.setFadingEdgeLength(0);
                    galleryFlow2.setSpacing(-100); //图片之间的间距
                    galleryFlow2.setAdapter(adapter);
                }
                break;
        }
    }

    @Override
    public void getPoints01(List<LatLng> points) {
        this.points = points;
    }

    @Override
    public void getPath01(String path) {
        this.path = path;
    }

    @Override
    public void getFeeling(String feeling) {
        this.f =feeling;
    }

    @Override
    public void getBitmaps(List<Bitmap> bitmaps) {
        this.list = bitmaps;
    }


}
