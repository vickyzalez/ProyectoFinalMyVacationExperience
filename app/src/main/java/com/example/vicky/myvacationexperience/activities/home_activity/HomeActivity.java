package com.example.vicky.myvacationexperience.activities.home_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.Trip;

import org.json.JSONException;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private HomeControl control;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        ActionBar bar = this.getSupportActionBar();
        bar.setTitle("My Vacation Experience");

        HomeModel model = new HomeModel();
        HomeControl ctrl = new HomeControl(this, model);
        HomeView view = new HomeView(ctrl, this);

        ctrl.setView(view); //se le inserta la view porque no se instancia en el mismo momento

        control = ctrl;

    }


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

    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == 1){
            super.onActivityResult(requestCode,resultCode,data);
            Trip trip = (Trip) data.getSerializableExtra("Trip");
            this.control.updateList(trip, trip.getId());
        }
    }
    */
}
