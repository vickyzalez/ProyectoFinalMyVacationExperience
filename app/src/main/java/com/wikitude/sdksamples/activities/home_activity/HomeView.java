package com.wikitude.sdksamples.activities.home_activity;

import android.app.Activity;
import android.widget.Button;

import com.wikitude.sdksamples.R;

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
