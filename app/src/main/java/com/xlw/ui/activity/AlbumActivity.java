package com.xlw.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.xlw.db.TripDBHelper;
import com.xlw.model.Trip;
import com.xlw.onroad.R;
import com.xlw.presenter.AlbumPresenter;
import com.xlw.presenter.IDetailView;
import com.xlw.ui.adapter.AlbumAdapter;
import com.xlw.ui.view.GalleryFlow;

import java.text.SimpleDateFormat;
import java.util.List;

public class AlbumActivity extends ActionBarActivity implements IDetailView {

    private TextView detailTopic,detailTime,detailDesc;
    private TripDBHelper tripDBHelper;
    private GalleryFlow galleryFlow;
    AlbumPresenter albumPresenter;
    List<Bitmap> list;
    long position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        detailTopic = (TextView)findViewById(R.id.detailTopic);
        detailTime = (TextView)findViewById(R.id.detailTime);
        detailDesc = (TextView)findViewById(R.id.detailDesc);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("微游记");

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getLong("position");

        albumPresenter = new AlbumPresenter();
        albumPresenter.setView(this, position);
        albumPresenter.getData();

        if(list.size()==0){
            Toast.makeText(this, "没有照片", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("list.size()", list.size() + "");
            AlbumAdapter adapter = new AlbumAdapter(this, list);
            adapter.createReflectedImages();//创建倒影效果
            galleryFlow = (GalleryFlow)findViewById(R.id.galleryFlow);
            galleryFlow.setFadingEdgeLength(0);
            galleryFlow.setSpacing(-100); //图片之间的间距
            galleryFlow.setAdapter(adapter);
        }

        tripDBHelper = new TripDBHelper();

        Trip trip = tripDBHelper.loadTrip(position);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        detailTopic.setText(trip.getTopic());
        detailTime.setText(simpleDateFormat.format(trip.getStart()));
        detailDesc.setText(trip.getDesc());
    }

    protected void onResume() {
        super.onResume();
        albumPresenter.viewFinishLoading();
    }

    @Override
    public void showResult() {
        Toast.makeText(this,"没有照相",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getListBitMap(List<Bitmap> list) {
        this.list = list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.map01:
                Intent intent = new Intent(this,RouteActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                Intent intent1 = new Intent(AlbumActivity.this, TravelMemoryActivity.class);
                startActivity(intent1);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
