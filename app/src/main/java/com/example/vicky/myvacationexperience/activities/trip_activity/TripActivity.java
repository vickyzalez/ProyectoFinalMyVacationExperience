package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.utilities.FileHandler;

import java.io.IOException;

public class TripActivity extends AppCompatActivity {
    TripControl ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        TripModel model = new TripModel();
        TripControl control = new TripControl(this,model);
        TripView view = new TripView(control, this);
        control.setView(view);
        control.loadList();

        ctrl = control;

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(model.getTrip().getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.setResultToPreviousActivity();
                this.finish();
                return true;



            default:
                return false;
        }

    }

    @Override
    public void onBackPressed(){
        this.setResultToPreviousActivity();
        super.onBackPressed();
    }

    private void setResultToPreviousActivity(){
        Intent intent = new Intent();
        intent.putExtra("Trip", ctrl.getTrip());
        this.setResult(1, intent);
    }
}
