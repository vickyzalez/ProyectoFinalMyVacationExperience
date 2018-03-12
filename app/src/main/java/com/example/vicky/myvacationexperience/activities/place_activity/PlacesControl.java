package com.example.vicky.myvacationexperience.activities.place_activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.trip_activity.TripActivity;
import com.example.vicky.myvacationexperience.dialogs.NewTripDialogFragment;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;
import com.example.vicky.myvacationexperience.viewholders.ItemTripViewHolder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class PlacesControl implements View.OnClickListener{

    private PlacesActivity activity;
    private PlacesView view;
    private PlacesModel model;

    public PlacesControl(PlacesActivity activity, PlacesModel model){
        this.activity = activity;
        this.model = model;

    }


    public void setView(PlacesView view) {
        this.view = view;
    }

    public List<LayerTrip> getLayers() {
        return getTrip().getLayers();
    }

    public Trip getTrip(){
        return model.getTrip();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addTrip:


                break;

            default:
/*
                Intent intent = new Intent(activity.getApplicationContext(), TripActivity.class);
                intent.putExtra("Trip", trip);
                activity.startActivityForResult(intent, 1);
*/
                break;
        }

    }


    public void loadTrip() {

        Trip trip = (Trip) activity.getIntent().getSerializableExtra("Trip");
        model.setTrip(trip);

    }
}
