package com.wikitude.sdksamples.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.trip_activity.TripActivity;
import com.wikitude.sdksamples.activities.trip_list_activity.TripListControl;
import com.wikitude.sdksamples.entities.Trip;
import com.wikitude.sdksamples.utilities.FileHandler;

import org.json.JSONException;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Vicky on 11/2/2018.
 */

public class NewTripDialogFragment extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener, DatePicker.OnDateChangedListener{

    private EditText tripName;
    private EditText tripFrom;
    private EditText tripTo;
    private DatePicker datePicker;
    private Boolean isFrom;
    private Button btnTripCancel;
    private Button btnTripOk;
    private static Activity activity;
    private static TripListControl control;
    private static Trip tripModify;
    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static NewTripDialogFragment newInstance(Activity act, TripListControl tripListControl,Trip trip) {
        NewTripDialogFragment f = new NewTripDialogFragment();
        activity = act;
        control = tripListControl;
        tripModify = trip;

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.newtrip_dialog, container, false);
        this.tripName = v.findViewById(R.id.editNameTrip);
        this.tripFrom = v.findViewById(R.id.editFromTrip);
        this.tripTo = v.findViewById(R.id.editToTrip);
        this.datePicker = v.findViewById(R.id.tripDatePicker);
        this.btnTripCancel = v.findViewById(R.id.btnNewTripCancel);
        this.btnTripOk = v.findViewById(R.id.btnNewTripOk);

        this.tripFrom.setOnClickListener(this);
        this.tripTo.setOnClickListener(this);

        this.btnTripOk.setOnClickListener(this);
        this.btnTripCancel.setOnClickListener(this);

        if (tripModify != null){
            this.tripName.setText(tripModify.getName());
            this.tripFrom.setText(tripModify.getFromDate());
            this.tripTo.setText(tripModify.getToDate());
        }

        return v;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override //click normal
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.editFromTrip:
                this.isFrom = true;
                showDatePicker();
                break;

            case R.id.editToTrip:
                this.isFrom = false;
                showDatePicker();
                break;

            case R.id.btnNewTripCancel:
                this.dismiss();
                break;

            case R.id.btnNewTripOk:

               if(emptyFields()){

                   new AlertDialog.Builder(activity)
                           .setTitle(R.string.errorTitle)
                           .setMessage(R.string.emptyFieldsMsg)
                           .setIcon(android.R.drawable.ic_dialog_alert)
                           .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int whichButton) {}})
                           .show();
               } else {

                   if(wrongDate()){
                       new AlertDialog.Builder(activity)
                               .setTitle(R.string.errorTitle)
                               .setMessage(R.string.wrongDate)
                               .setIcon(android.R.drawable.ic_dialog_alert)
                               .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
                                   public void onClick(DialogInterface dialog, int whichButton) {}})
                               .show();

                   } else {

                    if (tripModify == null){
                        //buscamos el id que tendra el nuevo Trip
                            Integer idTrip = 0;
                        try {
                            idTrip = FileHandler.getIdNextTrip(activity);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //creamos el objeto
                        Trip trip = new Trip(idTrip,
                                this.tripName.getText().toString(),
                                this.tripFrom.getText().toString(),
                                this.tripTo.getText().toString(), "");

                        //se guarda en el celu
                        try {
                            FileHandler.saveTrip(trip, activity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Actualiza activity
                        control.updateList(trip, null);

                        //Vas a la otra activity pasando el trip
                        Intent intent = new Intent(activity.getApplicationContext(), TripActivity.class);
                        intent.putExtra("Trip", trip);
                        activity.startActivityForResult(intent, 1);

                    } else {

                        tripModify.setName(this.tripName.getText().toString());
                        tripModify.setFromDate(this.tripFrom.getText().toString());
                        tripModify.setToDate(this.tripTo.getText().toString());

                        //se guarda en el celu
                        try {
                            FileHandler.saveTrip(tripModify, activity);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Actualiza activity
                        control.updateList(tripModify, tripModify.getId());
                    }

                    this.dismiss();
                    break;
                }
           }
        }
    }

    private boolean wrongDate() {
        String fromPath = this.tripFrom.getText().toString();
        String from[] = fromPath.split("/");

        String toPath = this.tripTo.getText().toString();
        String to[] = toPath.split("/");

        Integer toDay = Integer.parseInt(to[0]);
        Integer toMonth = Integer.parseInt(to[1]);
        Integer toYear = Integer.parseInt(to[2]);
        Integer fromDay = Integer.parseInt(from[0]);
        Integer fromMonth = Integer.parseInt(from[1]);
        Integer fromYear = Integer.parseInt(from[2]);

        if(fromYear.equals(toYear)){
            if(fromMonth.equals(toMonth)){
                if(fromDay.equals(toDay)){
                    return false;
                } else {
                    if (fromDay > toDay){
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                if (fromMonth > toMonth){
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            if (fromYear > toYear){
                return true;
            } else {
                return false;
            }
        }
    }

    private boolean emptyFields() {
        return this.tripName.getText().toString().equals("") ||
                this.tripFrom.getText().toString().equals("")||
                this.tripTo.getText().toString().equals("");
    }

    private void showDatePicker() {
        this.showViews(View.GONE);
        this.datePicker.setVisibility(View.VISIBLE);
        this.datePicker.bringToFront();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), this);

    }

    private void showViews(int visibility){
        this.tripName.setVisibility(visibility);
        this.tripFrom.setVisibility(visibility);
        this.tripTo.setVisibility(visibility);
        this.btnTripCancel.setVisibility(visibility);
        this.btnTripOk.setVisibility(visibility);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

        this.datePicker.setVisibility(View.GONE);
        this.showViews(View.VISIBLE);

        if (isFrom){
            this.tripFrom.setText(String.valueOf(i2)+ "/" + String.valueOf(i1+1) + "/" + String.valueOf(i));
        } else {
            this.tripTo.setText(String.valueOf(i2)+ "/" + String.valueOf(i1+1) + "/" + String.valueOf(i));
        }

    }
}
