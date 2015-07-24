package com.xlw.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.xlw.model.Photo;
import com.xlw.model.Trip;
import com.xlw.onroad.R;
import com.xlw.presenter.ITravelMemoryView;
import com.xlw.presenter.TravelMemoryPresenter;
import com.xlw.ui.adapter.ListAdapter;

import java.util.List;

public class TravelMemoryActivity extends BaseActivity implements ITravelMemoryView,AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    Spinner spn;
    String[] category;
    TravelMemoryPresenter travelMemoryPresenter;
    private ListView lst;
    ListAdapter adapter01;
    List<Trip> trips;
    List<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_memory);

        travelMemoryPresenter = new TravelMemoryPresenter();
        travelMemoryPresenter.setView(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);    // 使用up 箭头指示器显示home
        actionBar.setTitle("微游记           ");
        actionBar.setDisplayShowCustomEnabled(true);
        View mCustomView = getLayoutInflater().inflate(R.layout.category_layout, null);
        actionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT | ActionBar.LayoutParams.MATCH_PARENT));
        spn =(Spinner)actionBar.getCustomView().findViewById(R.id.spn);
        category = new String[]{"境内游", "境外游", "全部"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.item, R.id.text, category);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(this);

        lst = (ListView)findViewById(R.id.lst);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        travelMemoryPresenter.viewFinishLoading();
    }

    @Override
    public void gotoNextActivity(Class tClass) {
        startActivity(new Intent(this, tClass));
    }

    @Override
    public void getTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public void getPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    String category01;
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category01 = category[position];
        travelMemoryPresenter.getTrips01(category01);
        travelMemoryPresenter.getPhotos01(category01);
        if(trips.size()>0 && photos.size()>0){
            adapter01 = new ListAdapter(this,trips, photos);
            lst.setAdapter(adapter01);
            lst.setOnItemClickListener(this);
        }else{
            Toast.makeText(this, "没有记录", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category01 = "境内游";
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, AlbumActivity.class);
        Trip trip = (Trip)parent.getItemAtPosition(position);
        intent.putExtra("position",trip.getId());
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_travel_memory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add01 :
                travelMemoryPresenter.gotoNextView(TravelNewActivity.class);
                finish();
                break;
            case android.R.id.home :
                startActivity(new Intent(this, MenuActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
