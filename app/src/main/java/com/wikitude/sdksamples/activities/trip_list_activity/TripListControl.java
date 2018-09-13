package com.wikitude.sdksamples.activities.trip_list_activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.trip_activity.TripActivity;
import com.wikitude.sdksamples.viewholders.ItemTripViewHolder;
import com.wikitude.sdksamples.dialogs.NewTripDialogFragment;
import com.wikitude.sdksamples.entities.Trip;
import com.wikitude.sdksamples.utilities.FileHandler;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class TripListControl implements View.OnClickListener{

    private TripListActivity activity;
    private TripListView view;
    private TripListModel model;
    private Trip tripView; //para ver cual seleccionó

    public TripListControl(TripListActivity activity, TripListModel model){
        this.activity = activity;
        this.model = model;

    }

    public void loadList() throws IOException, JSONException {
        
        List<Trip> tripsMobile = new ArrayList<Trip>();

        tripsMobile = FileHandler.getTripsToView(this.activity);

        List<Trip> soonerFirst = soonerTripFirst(tripsMobile);

        model.setTrips(soonerFirst);

        if (tripsMobile.isEmpty()){
            view.showMessage();
        }
    }

    private List<Trip> soonerTripFirst(List<Trip> tripsMobile) {

        for (int x = 0; x < tripsMobile.size(); x++) {

            for (int i = 0; i < tripsMobile.size()-x-1; i++) {

                if(!dateFirst(tripsMobile.get(i).getFromDate(), tripsMobile.get(i+1).getFromDate())){

                    Trip tmp = tripsMobile.get(i+1);
                    tripsMobile.set(i+1, tripsMobile.get(i));
                    tripsMobile.set(i, tmp);

                }
            }
        }

        return tripsMobile;
    }

    private boolean dateFirst(String date1, String date2) {
        String one[] = date1.split("/");

        String two[] = date2.split("/");

        Integer minDay = Integer.parseInt(one[0]);
        Integer minMonth = Integer.parseInt(one[1]);
        Integer minYear = Integer.parseInt(one[2]);
        Integer maxDay = Integer.parseInt(two[0]);
        Integer maxMonth = Integer.parseInt(two[1]);
        Integer maxYear = Integer.parseInt(two[2]);

        if(minYear.equals(maxYear)){
            if(minMonth.equals(maxMonth)){
                if(minDay.equals(maxDay)){
                    return false;
                } else {
                    if (minDay > maxDay){
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                if (minMonth > maxMonth){
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (minYear > maxYear){
                return false;
            } else {
                return true;
            }
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
                DialogFragment newFragment = NewTripDialogFragment.newInstance(this.activity, this, null);
                newFragment.show(ft, "prueba");
                break;

            case R.id.btnMoreOptions:
                final Activity act = this.activity;
                final View view = v;
                final TripListControl ctrl = this;

                PopupMenu popup = new PopupMenu(act, v);
                popup.getMenuInflater().inflate(R.menu.menu_option, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        tripView = getSelectedTrip((View)view.getParent());

                        if (item.getItemId() == R.id.menu_delete){

                            //mensaje de alerta
                            new AlertDialog.Builder(act)
                                    .setTitle(R.string.popupConfirmTitle)
                                    .setMessage(R.string.popupConfirm)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            //por si quiero mostrar
                                            // Toast.makeText(act, R.string.btnOk, Toast.LENGTH_SHORT).show();

                                            try {
                                                FileHandler.deleteTrip(tripView.getId(),act);
                                                updateList(null, tripView.getId());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }})
                                    .setNegativeButton(R.string.btnCancel, null).show();



                        } else {
                            //es edit, llama de nuevo al Dialog
                            FragmentTransaction ft = act.getFragmentManager().beginTransaction();
                            Fragment prev = act.getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            // Create and show the dialog.
                            DialogFragment newFragment = NewTripDialogFragment.newInstance(act, ctrl, tripView);
                            newFragment.show(ft, "prueba");

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
