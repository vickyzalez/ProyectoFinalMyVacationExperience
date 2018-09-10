package com.wikitude.sdksamples.activities.place_activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

/**
 * Created by Vicky on 10/2/2018.
 */

public class PlacesView {

    //TODO VER QUE VA A TENER
    private FloatingActionButton addTrip;
    private RecyclerView recyclerView;
    private PlacesControl ctrl;
    private TextView message;

    public PlacesView(PlacesControl ctrl, Activity activity) {
        //boton nuevo trip
       // this.addTrip = (FloatingActionButton) activity.findViewById(R.id.addTrip);
        //addTrip.setOnClickListener(ctrl); //Es el controlador quien implementa Listener



        //this.ctrl = ctrl;

    }

}
