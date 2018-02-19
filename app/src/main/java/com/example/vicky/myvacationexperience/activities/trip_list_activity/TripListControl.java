package com.example.vicky.myvacationexperience.activities.trip_list_activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.DialogFragment;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.trip_activity.TripActivity;
import com.example.vicky.myvacationexperience.viewholders.ItemTripViewHolder;
import com.example.vicky.myvacationexperience.dialogs.NewTripDialogFragment;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;

import org.json.JSONException;

import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class TripListControl implements View.OnClickListener{

    private TripListActivity activity;
    private TripListView view;
    private TripListModel model;

    public TripListControl(TripListActivity activity, TripListModel model){
        this.activity = activity;
        this.model = model;

    }

    public void loadList() throws IOException, JSONException {


        List<Trip> tripsMobile = new ArrayList<Trip>();

        tripsMobile = FileHandler.getTripsToView(this.activity);
        model.setTrips(tripsMobile);

        if (tripsMobile.isEmpty()){
            view.showMessage();
        }
    }

    public void updateList(Trip trip, Integer position){
        if(position == null){
            view.hideMessage();
            this.model.getTrips().add(trip);
            this.view.notifyItemInserted(this.model.getTrips().size()-1);
        } else if (trip == null) {
            this.model.getTrips().remove(position.intValue());
            this.view.notifyItemRemoved(position);

            if (model.getTrips().isEmpty()){
                view.showMessage();
            }

        } else {
            this.model.getTrips().set(position, trip);
            this.view.notifyItemChanged(position);
        }
    }

    public void setView(TripListView view) {
        this.view = view;
    }

    public List<Trip> getTrips() {
        return model.getTrips();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addTrip:
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
                DialogFragment newFragment = NewTripDialogFragment.newInstance(this.activity, this);
                newFragment.show(ft, "prueba");
                break;

            case R.id.btnMoreOptions:
                //se fija por las opciones del trip (editar/borrar)
                //TODO llamar a las opciones de menu
                break;

            default:
                //Ir a la pantalla del trip seleccionado
                //TODO ir a la pantalla del trip
                ItemTripViewHolder item = new ItemTripViewHolder(v);
                Log.d(item.getTxtId().getText().toString(), item.getTxtTripName().getText().toString());

                Integer idTrip = Integer.valueOf(item.getTxtId().getText().toString());
                Trip trip = new Trip();

                //buscar el trip en el listado
                for (Trip currentTrip: this.model.getTrips()){
                    if (currentTrip.getId() == idTrip){
                        trip = currentTrip;
                        break;
                    }
                }

                Intent intent = new Intent(activity.getApplicationContext(), TripActivity.class);
                intent.putExtra("Trip", trip);
                activity.startActivity(intent);

                break;
        }

    }


}
