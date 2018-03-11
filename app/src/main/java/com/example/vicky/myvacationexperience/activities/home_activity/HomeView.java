package com.example.vicky.myvacationexperience.activities.home_activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.viewholders.ItemTripViewHolder;

/**
 * Created by Vicky on 10/2/2018.
 */

public class HomeView {

    private Button btnGoogle;
    private Button btnWikitude;
    private HomeControl ctrl;

    public HomeView(HomeControl ctrl, Activity activity) {

        //boton My maps
        this.btnGoogle = (Button) activity.findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(ctrl); //Es el controlador quien implementa Listener

        //boton Wikitude
        this.btnWikitude = (Button) activity.findViewById(R.id.btnWikitude);
        btnWikitude.setOnClickListener(ctrl); //Es el controlador quien implementa Listener

        this.ctrl = ctrl;

    }

}
