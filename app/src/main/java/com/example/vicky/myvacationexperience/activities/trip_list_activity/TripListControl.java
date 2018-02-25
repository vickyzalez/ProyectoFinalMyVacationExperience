package com.example.vicky.myvacationexperience.activities.trip_list_activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.DialogFragment;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
        Log.d("TripsMobile", tripsMobile.get(tripsMobile.size()-1).getLayers().toString());
        model.setTrips(tripsMobile);

        if (tripsMobile.isEmpty()){
            view.showMessage();
        }
    }

    public void updateList(Trip trip, Integer id){
        List<Trip> trips = this.model.getTrips();
        if(id == null){
            view.hideMessage();
            trips.add(trip);
            this.view.notifyItemInserted(trips.size()-1);
        }
        else {
            for(int i=0;i<trips.size();i++){
                if(id.intValue() == trips.get(i).getId().intValue()){
                    if(trip == null){
                        trips.remove(i);
                        this.view.notifyItemRemoved(i);
                        if (trips.isEmpty()){
                            view.showMessage();
                        }
                    }
                    else{
                        Log.d("LEL", trip.getName()+" - "+trip.getLayers().toString());
                        this.getTrips().set(i, trip);
                        this.view.notifyItemChanged(i);
                    }
                    break;
                }
            }
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
                final Activity act = this.activity;
                final View view = v;

                PopupMenu popup = new PopupMenu(act, v);
                popup.getMenuInflater().inflate(R.menu.menu_option, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.menu_delete){
                            Trip trip = getSelectedTrip((View)view.getParent());
                            //TODO llamar al popup para confirmar "esta seguro?"
                            try {
                                FileHandler.deleteTrip(trip.getId(),act);
                                updateList(null, trip.getId());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        } else {
                            //TODO editar

                        }
                        return true;
                    }
                });

                popup.show();

                break;

            default:

                Trip trip = this.getSelectedTrip(v);

                Intent intent = new Intent(activity.getApplicationContext(), TripActivity.class);
                intent.putExtra("Trip", trip);
                activity.startActivityForResult(intent, 1);

                break;
        }

    }

    private Trip getSelectedTrip(View v){
        ItemTripViewHolder item = new ItemTripViewHolder(v);

        Integer idTrip = Integer.valueOf(item.getTxtId().getText().toString());
        Trip trip = new Trip();

        //buscar el trip en el listado
        for (Trip currentTrip: this.model.getTrips()){
            if (currentTrip.getId().intValue() == idTrip.intValue()){
                trip = currentTrip;
                break;
            }
        }

        return trip;
    }


}
