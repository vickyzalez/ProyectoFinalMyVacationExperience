package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.viewholders.ItemLayerViewHolder;

/**
 * Created by Vicky on 18/2/2018.
 */

public class TripView extends RecyclerView.Adapter<ItemLayerViewHolder> {

        private FloatingActionButton addLayer;
        private RecyclerView recyclerView;
        private Button btnMoreOptions2;
        private TripControl ctrl;

        public TripView(TripControl ctrl,Activity activity) {
            //boton nuevo trip
            this.addLayer = (FloatingActionButton) activity.findViewById(R.id.addLayer);
            addLayer.setOnClickListener(ctrl);

            this.recyclerView = (RecyclerView) activity.findViewById(R.id.layers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity); //manejador de lista
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(this);

            this.ctrl = ctrl;


        }

        @Override
        public ItemLayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer, parent, false);
            v.setOnClickListener(ctrl);

            ItemLayerViewHolder ItemLayerViewHolder = new ItemLayerViewHolder(v);
            ItemLayerViewHolder.getBtnMoreOptions().setOnClickListener(ctrl);
            return ItemLayerViewHolder;
        }

        @Override
        public void onBindViewHolder(ItemLayerViewHolder holder, int position) {
            LayerTrip layerTrip = ctrl.getLayers().get(position);

            holder.getTxtLayerName().setText(layerTrip.getName());
            holder.getChkVisible().setChecked(layerTrip.getVisible());
            holder.getIconLayer().setImageResource(layerTrip.getIcon()); //se le pasa el R.drawable directamente al crearlo
            //TODO mapear el listado

        }
        @Override
        public int getItemCount() {
            return this.ctrl.getLayers().size();
        }


    }