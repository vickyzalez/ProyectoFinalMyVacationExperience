package com.example.vicky.myvacationexperience.activities.place_activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.trip_activity.TripActivity;
import com.example.vicky.myvacationexperience.dialogs.NewLayerDialogFragment;
import com.example.vicky.myvacationexperience.dialogs.NewPlaceDialogFragment;
import com.example.vicky.myvacationexperience.dialogs.NewTripDialogFragment;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;
import com.example.vicky.myvacationexperience.viewholders.ItemTripViewHolder;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

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

    public PlacesModel getModel() {
        return model;
    }

    public void setModel(PlacesModel model) {
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
            case R.id.btnAddPlace:
                /*/Para poder mostrar el popup
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = NewPlaceDialogFragment.newInstance(activity, this, model.getTrip(), model.getPlace());
                newFragment.show(ft, "prueba");*/
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

    public void loadPlaces(ArrayList<MarkerOptions> markers, GoogleMap mMap) {
        for(LayerTrip layer: getTrip().getLayers()){
            if (layer.getVisible()){
                for (com.example.vicky.myvacationexperience.entities.Place placeLayer: layer.getPlaces()){
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(placeLayer.getLatitude(),placeLayer.getLongitude()))
                            .title(placeLayer.getName().toString())
                            .snippet(placeLayer.getAddress().toString())
                            .icon(BitmapDescriptorFactory.fromResource(layer.getIcon()));

                    mMap.addMarker(marker);
                    markers.add(marker);

                }
            }
        }
    }

    public void markersZoom(ArrayList<MarkerOptions> markers, GoogleMap mMap) {
        if (markers.size() != 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (MarkerOptions mark : markers) {
                builder.include(mark.getPosition());
            }
            LatLngBounds bounds = builder.build();

            int width = activity.getResources().getDisplayMetrics().widthPixels;
            int height = activity.getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mMap.animateCamera(cu);
        }
    }

    public String getAddressFromLatLng(LatLng latLng, Geocoder geocoder) {
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            return addresses.get(0).getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
