package com.example.vicky.myvacationexperience.activities.place_activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.viewholders.ItemTripViewHolder;

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
