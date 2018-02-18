package com.example.vicky.myvacationexperience.dialogs;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.trip_list_activity.TripListControl;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;

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

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static NewTripDialogFragment newInstance(Activity act, TripListControl tripListControl) {
        NewTripDialogFragment f = new NewTripDialogFragment();
        activity = act;
        control = tripListControl;

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

        /* View tv = v.findViewById(R.id.text);
        ((TextView)tv).setText("Dialog #" + mNum + ": using style " + getNameForNum(mNum));

        // Watch for button clicks.
        Button button = (Button)v.findViewById(R.id.show);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                ((FragmentDialog)getActivity()).showDialog();
            }
        });

        */
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

                //TODO implementar el llamado a la API que devuelva la fotito

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

                //TODO llamar al nuevo activity (el de listado de layers)

                this.dismiss();
                break;
        }
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
