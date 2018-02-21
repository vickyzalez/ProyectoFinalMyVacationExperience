package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.dialogs.NewLayerDialogFragment;
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
        Log.d("tripLayer", trip.getName()+" - "+trip.getLayers().toString());

        if (trip.getLayers().isEmpty()){
            view.showMessage();
        }
    }

    public List<LayerTrip> getLayers() {
        return getTrip().getLayers();
    }

    public Trip getTrip(){
        return model.getTrip();
    }

    public void updateList(LayerTrip layerTrip, Integer position){
        if(position == null){
            view.hideMessage();
            this.view.notifyItemInserted(this.getLayers().size()-1);
        } else if (layerTrip == null) {
            this.getLayers().remove(position.intValue());
            this.view.notifyItemRemoved(position);

            if (getLayers().isEmpty()){
                view.showMessage();
            }

        } else {
            this.getLayers().set(position, layerTrip);
            this.view.notifyItemChanged(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addLayer:
                //Para poder mostrar el popup
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
                DialogFragment newFragment = NewLayerDialogFragment.newInstance(this.activity, this);
                newFragment.show(ft, "prueba");
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
