package com.wikitude.sdksamples.activities.trip_list_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.entities.Trip;

import org.json.JSONException;

import java.io.IOException;

public class TripListActivity extends AppCompatActivity {

    private TripListControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.trip_list_main);

        ActionBar bar = this.getSupportActionBar();
        bar.setTitle("My Vacation Experience");
        bar.setDisplayHomeAsUpEnabled(true);

        TripListModel model = new TripListModel();
        TripListControl ctrl = new TripListControl(this, model);
        TripListView view = new TripListView(ctrl, this);

        ctrl.setView(view); //se le inserta la view porque no se instancia en el mismo momento
        try {
            ctrl.loadList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        control = ctrl;

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == 1){
            super.onActivityResult(requestCode,resultCode,data);
            Trip trip = (Trip) data.getSerializableExtra("Trip");
            this.control.updateList(trip, trip.getId());
        }
    }


}
