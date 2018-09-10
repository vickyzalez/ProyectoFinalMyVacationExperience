package com.wikitude.sdksamples.activities.trip_activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.place_activity.PlacesActivity;
import com.wikitude.sdksamples.entities.LayerTrip;
import com.wikitude.sdksamples.entities.Place;
import com.wikitude.sdksamples.entities.Trip;
import com.wikitude.sdksamples.utilities.FileHandler;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 18/2/2018.
 */

public class LayerView extends BaseAdapter {

        private TripControl ctrl;
        private Activity activ;
        private TextView txtNamePlace;
        private ImageButton imgPlace;
        private LayerTrip layer;
        private List<Place> places;
        private Trip trip;
        private int index;


    public LayerView(TripControl ctrl, Activity activity, LayerTrip layerTrip, Trip trip, int index) {

            this.places = new ArrayList<Place>();
            this.places = layerTrip.getPlaces();
            this.layer = layerTrip;
            this.trip = trip;
            this.index = index;

            this.ctrl = ctrl;

            this.activ = activity;


        }

    public void setLayer(LayerTrip layer) {
        this.layer = layer;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @Override
    public int getCount() {
        return places.size();
    }

    @Override
    public Object getItem(int i) {
        return places.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activ.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_place, null);
        }


        final Place place = places.get(position);

        TextView name = (TextView) v.findViewById(R.id.txtPlaceChild);
        name.setText(place.getName());
        //TODO llamar al mapa con el zoom del place cuando se seleccione
        LatLng placeLatlng = new LatLng(place.getLatitude(),place.getLongitude());
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activ.getApplicationContext(), PlacesActivity.class);
                intent.putExtra("PlaceSelected", place);
                intent.putExtra("Trip", ctrl.getTrip());
                activ.startActivityForResult(intent, 1);
            }
        });

        ImageButton imgButton = (ImageButton) v.findViewById(R.id.btnDeletePlace);
        final Activity act = activ; //para el alert
        final LayerView thisLV = this;
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(act)
                        .setTitle(R.string.popupConfirmTitle)
                        .setMessage(R.string.popupConfirmPlace)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                //elimina el place del layer
                                for(LayerTrip layerTrip: trip.getLayers()){
                                    if(layerTrip.equals(layer)){
                                        layerTrip.getPlaces().remove(place);
                                        break;
                                    }
                                }

                                try {
                                    FileHandler.saveTrip(trip, act);
                                    thisLV.notifyDataSetChanged();
                                    TripView tripView = thisLV.ctrl.getView();
                                    //para que no se comprima la primera vez
                                    if(tripView.getFirstDeletion()){
                                        tripView.setVisibiltyGone(true);
                                    }
                                    tripView.notifyItemChanged(index);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }})
                        .setNegativeButton(R.string.btnCancel, null).show();
            }
        });

        Log.d("listView", position + " " + place.getName());
        return v;
    }
}