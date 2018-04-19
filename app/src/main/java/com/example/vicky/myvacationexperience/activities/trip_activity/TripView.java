package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Place;
import com.example.vicky.myvacationexperience.viewholders.ItemLayerViewHolder;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Vicky on 18/2/2018.
 */

public class TripView extends RecyclerView.Adapter<ItemLayerViewHolder> {

        private FloatingActionButton addLayer;
        private RecyclerView recyclerView;
        private TripControl ctrl;
        private TextView message;
        private Activity activ;
        private CheckBox checkBox;
        private Boolean isFirstDeletion;

    public Boolean getFirstDeletion() {
        return isFirstDeletion;
    }

    private Boolean isVisibiltyGone;

    public void setVisibiltyGone(Boolean visibiltyGone) {
        isVisibiltyGone = visibiltyGone;
    }

    public TripView(TripControl ctrl, Activity activity) {
            //boton nuevo trip
            this.addLayer = (FloatingActionButton) activity.findViewById(R.id.addLayer);
            addLayer.setOnClickListener(ctrl);

            this.recyclerView = (RecyclerView) activity.findViewById(R.id.layers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity); //manejador de lista
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(this);

            this.message = (TextView) activity.findViewById(R.id.txtNoLayerMessage);

            this.ctrl = ctrl;

            this.activ = activity;

            this.isFirstDeletion = true;

            this.isVisibiltyGone = false;



        }

        @Override
        public ItemLayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layer, parent, false);
            v.setOnClickListener(ctrl);

            ItemLayerViewHolder itemLayerViewHolder = new ItemLayerViewHolder(v);
            itemLayerViewHolder.getBtnMoreOptions().setOnClickListener(ctrl);
            itemLayerViewHolder.getExpandable().setOnClickListener(ctrl);
            itemLayerViewHolder.getChkVisible().setOnClickListener(ctrl);

            return itemLayerViewHolder;
        }

        @Override
        public void onBindViewHolder(ItemLayerViewHolder holder, int position) {
            LayerTrip layerTrip = ctrl.getLayers().get(position);

            holder.getTxtLayerName().setText(layerTrip.getName());
            holder.getChkVisible().setChecked(layerTrip.getVisible());
            holder.getIconLayer().setImageResource(layerTrip.getIcon()); //se le pasa el R.drawable directamente al crearlo
            holder.getTxtPosition().setText(String.valueOf(position));
            holder.getExpandable().setText(activ.getResources().getString(R.string.txtExpandable) + " (" + layerTrip.getPlaces().size() + ")");
            holder.getListView().setAdapter(this.ctrl.getAdapterList().get(position));
            // para desplegable 1er delete
            if(this.isVisibiltyGone){
                holder.getListView().setVisibility(View.VISIBLE);
                this.isVisibiltyGone = false;
                this.isFirstDeletion = false;
            }


        }
        @Override
        public int getItemCount() {
            return this.ctrl.getLayers().size();
        }


        public void showMessage(){
            this.message.setVisibility(View.VISIBLE);
        }

        public void hideMessage(){
            this.message.setVisibility(View.GONE);
        }

    }