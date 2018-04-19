package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Place;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;
import com.example.vicky.myvacationexperience.viewholders.ItemLayerViewHolder;

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
            //
            /*this.imgPlace = (ImageButton) activity.findViewById(R.id.btnDeletePlace);
            imgPlace.setOnClickListener(ctrl);

            this.txtNamePlace = (TextView) activity.findViewById(R.id.txtPlaceChild);*/

            this.places = new ArrayList<Place>();
            this.places = layerTrip.getPlaces();
            this.layer = layerTrip;
            this.trip = trip;
            this.index = index;

            this.ctrl = ctrl;

            this.activ = activity;


        }


    @Override
    public int getCount() {
        return places.size();
    }

    /*public void clear() {
        places.clear();
    }

    public void addAll(ArrayList<Place> category) {
        for (int i = 0; i < category.size(); i++) {
            places.add(category.get(i));
        }
    }*/

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
                                    thisLV.ctrl.getView().notifyItemChanged(index);
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