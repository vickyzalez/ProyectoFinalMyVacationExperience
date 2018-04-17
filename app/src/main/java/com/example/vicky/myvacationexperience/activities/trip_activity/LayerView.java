package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Place;
import com.example.vicky.myvacationexperience.viewholders.ItemLayerViewHolder;

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

        public LayerView(TripControl ctrl, Activity activity, LayerTrip layerTrip) {
            //
            /*this.imgPlace = (ImageButton) activity.findViewById(R.id.btnDeletePlace);
            imgPlace.setOnClickListener(ctrl);

            this.txtNamePlace = (TextView) activity.findViewById(R.id.txtPlaceChild);*/

            this.places = new ArrayList<Place>();
            this.places = layerTrip.getPlaces();
            this.layer = layerTrip;

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
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activ.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_place, null);
        }

        Place place = places.get(position);

        TextView name = (TextView) v.findViewById(R.id.txtPlaceChild);
        name.setText(place.getName());

        ImageButton imgButton = (ImageButton) v.findViewById(R.id.btnDeletePlace);

        return v;
    }
}