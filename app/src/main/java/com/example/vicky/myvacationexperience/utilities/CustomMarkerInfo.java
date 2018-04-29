package com.example.vicky.myvacationexperience.utilities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.place_activity.PlacesActivity;
import com.example.vicky.myvacationexperience.activities.place_activity.PlacesControl;
import com.example.vicky.myvacationexperience.dialogs.NewPlaceDialogFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Vicky on 28/4/2018.
 */

public class CustomMarkerInfo implements GoogleMap.InfoWindowAdapter{

    private Context context;
    private PlacesControl control;

    public CustomMarkerInfo(Context ctx, PlacesControl ctrl){
        context = ctx;
        control = ctrl;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_detail, null);

        TextView markerName = view.findViewById(R.id.markerName);
        TextView markerAddress = view.findViewById(R.id.markerAddress);

        MarkerInfoData markerInfoData = (MarkerInfoData) marker.getTag();

        if (markerInfoData != null){
            markerName.setText(markerInfoData.getMarkerName());
            markerAddress.setText(markerInfoData.getMarkerAddress());
        }

        return view;

    }
}
