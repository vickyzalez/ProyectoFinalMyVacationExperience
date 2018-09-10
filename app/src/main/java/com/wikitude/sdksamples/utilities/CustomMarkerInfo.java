package com.wikitude.sdksamples.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.place_activity.PlacesControl;
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
        //TODO acá ocultar el "+" fijándose por el name "Nuevo lugar"

        MarkerInfoData markerInfoData = (MarkerInfoData) marker.getTag();

        if (markerInfoData != null){
            markerName.setText(markerInfoData.getMarkerName());
            markerAddress.setText(markerInfoData.getMarkerAddress());
        }

        return view;

    }
}
