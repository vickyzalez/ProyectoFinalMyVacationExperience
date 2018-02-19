package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.view.View;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Trip;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 18/2/2018.
 */

public class TripControl implements View.OnClickListener{

    private TripActivity activity;
    private TripModel model;
    private TripView view;


    public TripControl(TripActivity activity, TripModel model)  {
        this.activity = activity;
        this.model = model;


    }

    public void setView(TripView view) {
        this.view = view;
    }

    public void loadList(){

        Trip trip = (Trip) activity.getIntent().getSerializableExtra("Trip");
        model.setTrip(trip);

        if (trip.getLayers().isEmpty()){
            view.showMessage();
        }
    }

    public List<LayerTrip> getLayers() {
        return model.getTrip().getLayers();
    }

    public void updateList(LayerTrip layerTrip, Integer position){
        if(position == null){
            view.hideMessage();
            this.model.getTrip().getLayers().add(layerTrip);
            this.view.notifyItemInserted(this.model.getTrip().getLayers().size()-1);
        } else if (layerTrip == null) {
            this.model.getTrip().getLayers().remove(position.intValue());
            this.view.notifyItemRemoved(position);

            if (model.getTrip().getLayers().isEmpty()){
                view.showMessage();
            }

        } else {
            this.model.getTrip().getLayers().set(position, layerTrip);
            this.view.notifyItemChanged(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addLayer:
                //TODO llamar a una nueva activity de creacion de layer


                break;

            case R.id.btnMoreOptions:
                //se fija por las opciones del trip (editar/borrar)
                //TODO llamar a las opciones de menu

                //TODO falta el case para mostrar el mapa
                //TODO falta el case para ir para atras
                break;

            default:
                //NADA?
                break;
        }

    }
}
