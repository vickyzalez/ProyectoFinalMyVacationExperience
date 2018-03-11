package com.example.vicky.myvacationexperience.activities.home_activity;

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
import com.example.vicky.myvacationexperience.activities.trip_list_activity.TripListActivity;
import com.example.vicky.myvacationexperience.dialogs.NewTripDialogFragment;
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

public class HomeControl implements View.OnClickListener{

    private HomeActivity activity;
    private HomeView view;
    private HomeModel model;

    public HomeControl(HomeActivity activity, HomeModel model){
        this.activity = activity;
        this.model = model;

    }



    public void setView(HomeView view) {
        this.view = view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGoogle:

                Intent intent = new Intent(activity.getApplicationContext(), TripListActivity.class);
                activity.startActivity(intent);

                break;

            case R.id.btnWikitude:

                //TODO start wikitude activity


                break;

            default:

                break;
        }

    }



}
