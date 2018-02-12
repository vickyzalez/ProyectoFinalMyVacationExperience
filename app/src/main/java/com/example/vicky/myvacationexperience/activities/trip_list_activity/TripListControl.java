package com.example.vicky.myvacationexperience.activities.trip_list_activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.trip_list_activity.viewholders.ItemTripViewHolder;
import com.example.vicky.myvacationexperience.dialogs.NewTripDialogFragment;
import com.example.vicky.myvacationexperience.entities.Trip;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class TripListControl implements View.OnClickListener{

    private TripListActivity activity;
    private TripListView view;
    private TripListModel model;

    public TripListControl(TripListActivity activity, TripListModel model) {
        this.activity = activity;
        this.model = model;
        loadList();

    }

    private void loadList() {

        //TODO aca se van a buscar los trips que esten guardados en el celu para mostrarlos
        Calendar today = Calendar.getInstance();

        List<Trip> trips = getTrips();
        trips.add(new Trip(1,"Nueva York", "20/10/2017", "30/10/2018",null));
        trips.add(new Trip(2,"Miami", "20/10/2017", "30/10/2018",null));
        trips.add(new Trip(3,"Los Angeles", "20/10/2017", "30/10/2018",null));
        trips.add(new Trip(4,"Londres", "20/10/2017", "30/10/2018", null));
        trips.add(new Trip(5,"Tu vieja", "20/10/2017", "30/10/2018",null));



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
                DialogFragment newFragment = NewTripDialogFragment.newInstance(this.activity);
                newFragment.show(ft, "prueba");
                break;
            default:
                //Ir a la pantalla del trip seleccionado
                ItemTripViewHolder item = new ItemTripViewHolder(v);
                Log.d(item.getTxtId().getText().toString(), item.getTxtTripName().getText().toString());
        }

    }


}
