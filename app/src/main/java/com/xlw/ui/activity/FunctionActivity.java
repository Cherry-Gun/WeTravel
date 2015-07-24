package com.xlw.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.tandong.sa.bv.BottomView;
import com.xlw.db.FeelingDBHelper;
import com.xlw.db.LocationDBHelper;
import com.xlw.db.PhotoDBHelper;
import com.xlw.model.Feeling;
import com.xlw.model.Photo;
import com.xlw.onroad.R;
import com.xlw.utils.ImagesUtil;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

public class FunctionActivity extends ActionBarActivity implements View.OnClickListener{
    private MapView mapView;
    private AMap aMap;
    LocationManagerProxy locationManagerProxy;
    double lat;
    double lng;
    long tripId;
    String address;
    Marker marker;
    AMap.InfoWindowAdapter infoWindowAdapter;
    LatLng point;
    private int initZoomLevel = 19;
    String path;
    LocationDBHelper locationDBHelper;
    PhotoDBHelper photoDBHelper;
    FeelingDBHelper feelingDBHelper;
    long locationId;
    ProgressDialog progressDialog;
    String content = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("乐游记");
        mapView = (MapView)findViewById(R.id.map1);
        mapView.onCreate(savedInstanceState);
        init();
        Bundle bundle = getIntent().getExtras();
        tripId = bundle.getLong("id");
        path = bundle.getString("path");
        locationDBHelper = new LocationDBHelper();
        photoDBHelper = new PhotoDBHelper();
        feelingDBHelper = new FeelingDBHelper();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载地图...");
        progressDialog.show();
        locationManagerProxy = LocationManagerProxy.getInstance(this);
        setUpMap();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progressDialog.dismiss();
        }
    };
    public void setUpMap(){

        LocationListener locationListener = new LocationListener();
        locationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 5000, 0, locationListener);

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker != null) {
                    marker.hideInfoWindow();
                }
            }
        });
        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getBaseContext(),
                        marker.getTitle() + marker.getSnippet(),
                        Toast.LENGTH_SHORT
                ).show();
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

    public void render(Marker marker,View view){
        ImageView infoImage = (ImageView)view.findViewById(R.id.info_image);
        if(path.equals("没有照相")){
            infoImage.setImageResource(R.mipmap.ic_launcher);
        }else{
            try {
                Bitmap bitmap = ImagesUtil.loadBitmap(path,150,150);
                infoImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton editButton = (ImageButton) view.findViewById(R.id.btn_edit);
        editButton.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        try {
            path = ImagesUtil.saveImage2(bitmap);
            Date date = new Date();
            Photo photo = new Photo();
            photo.setTakeDate(date);
            photo.setUri(path);
            photo.setLocationId(locationId);
            photoDBHelper.savePhoto(photo);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    View view;
    BottomView bottomView;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.btn_edit:
                bottomView = new BottomView(this, R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
                bottomView.setAnimation(R.style.BottomToTopAnim);
                bottomView.showBottomView(false);
                view = bottomView.getView();
                TextView address01 = (TextView)view.findViewById(R.id.address01);
                address01.setText(address);
                break;
        }
    }
    public void onClick01(View v){
        int id = v.getId();
        switch(id){
            case R.id.save:
                EditText editText = (EditText)view.findViewById(R.id.talk);
                content = editText.getText().toString();
                Feeling feeling = new Feeling();
                feeling.setContent(content);
                feeling.setLocationId(locationId);
                feelingDBHelper.saveFeeling(feeling);
                Toast.makeText(getApplicationContext(),"成功",Toast.LENGTH_SHORT).show();
                bottomView.dismissBottomView();
                break;
            case R.id.reset:
                editText = (EditText)view.findViewById(R.id.talk);
                editText.setText("");
                break;
        }
    }


    public class LocationListener implements AMapLocationListener{
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if(aMapLocation != null){
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                point = new LatLng(lat,lng);
            }
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }
    public void init(){
        if(aMap == null){
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(initZoomLevel));
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(point));
                    handler.sendEmptyMessage(0);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_function, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.add:
                Date date = new Date();
                com.xlw.model.Location location = new com.xlw.model.Location();
                location.setLat(String.valueOf(lat));
                location.setLng(String.valueOf(lng));

                getLatLng(point);
                location.setLocDate(date);
                location.setTripId(tripId);
                locationDBHelper.saveLocation(location);
                List<com.xlw.model.Location> locations = locationDBHelper.loadAllLocation();
                location = locations.get(locations.size() - 1);
                locationId = location.getId();
                break;
            case android.R.id.home:
                Intent intent = new Intent(this,MenuActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void getLatLng(final LatLng point){
        final GeocodeSearch geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                    address = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    if (address == null) {
                        address = "中国";
                    }
                    moveToPoint(point);
                    addMarkToMap(point);
                }

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
        LatLonPoint latLonPoint = new LatLonPoint(point.latitude,point.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint,200,GeocodeSearch.AMAP);
        geocodeSearch.getFromLocationAsyn(query);
    }
    private void moveToPoint(LatLng point){
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(point));
        aMap.animateCamera(CameraUpdateFactory.zoomTo(initZoomLevel));
    }
    public void addMarkToMap(final LatLng point){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.title("我在这里");
        markerOptions.snippet(address);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitMap("mars")));
        markerOptions.draggable(false);
        marker = aMap.addMarker(markerOptions);
        marker.setObject("1001");
        marker.showInfoWindow();
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(point)) {
                    aMap.setInfoWindowAdapter(infoWindowAdapter);
                }
                return false;
            }
        });
    }

    public Bitmap getMarkerBitMap(String text) {
        Bitmap markerBitmap = BitmapDescriptorFactory.defaultMarker().getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Bitmap bitmap = Bitmap.createBitmap(markerBitmap, 0, 0, markerBitmap.getWidth(), markerBitmap.getHeight());
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(20f);
        textPaint.setColor(Color.RED);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText(text, 5, 35, textPaint);
        return bitmap;
    }



}
