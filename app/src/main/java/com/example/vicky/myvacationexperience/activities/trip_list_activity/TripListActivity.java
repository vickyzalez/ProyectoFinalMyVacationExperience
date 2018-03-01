package com.example.vicky.myvacationexperience.activities.trip_list_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
