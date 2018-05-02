package com.example.vicky.myvacationexperience.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.place_activity.PlacesActivity;
import com.example.vicky.myvacationexperience.activities.place_activity.PlacesControl;
import com.example.vicky.myvacationexperience.activities.trip_activity.LayerView;
import com.example.vicky.myvacationexperience.activities.trip_activity.TripControl;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Place;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 11/2/2018.
 */

public class NewPlaceDialogFragment extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener{

    private EditText placeName;
    private Spinner spinner;

    private Button btnCancel;
    private Button btnOk;

    private static PlacesActivity activity;
    private static OnMapReadyCallback onMapReadyCallback;
    private static PlacesControl control;

    private static Trip tripSelected;
    private static Place placeSelected;

    private static String[] layers;
    private static Marker markerSelected;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static NewPlaceDialogFragment newInstance(PlacesActivity act, PlacesControl placesControl, Trip trip, Place place, Marker marker) {
        NewPlaceDialogFragment f = new NewPlaceDialogFragment();
        activity = act;
        control = placesControl;
        tripSelected = trip;
        placeSelected = place;
        onMapReadyCallback = (OnMapReadyCallback) act;
        markerSelected = marker;

        layers = new String[trip.getLayers().size()];
        int i = 0;
        for (LayerTrip layer: trip.getLayers()){
            layers[i] = layer.getName();
            i++;
        }

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.newplace_dialog, container, false);

        this.placeName = v.findViewById(R.id.txtPlaceName);
        //this.spinner = v.findViewById(R.id.spinner);
        //spinner.setOnItemSelectedListener(this);

        this.btnOk = v.findViewById(R.id.btnNewPlaceOk);
        this.btnCancel = v.findViewById(R.id.btnNewPlaceCancel);

        placeName.setText(placeSelected.getName());
        this.btnOk.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override //click normal
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnNewPlaceCancel:
                this.dismiss();
                break;

            case R.id.btnNewPlaceOk:

                for (LayerTrip layerTrip: tripSelected.getLayers()){
                    //TODO consulta por la categoria seleccionada. Falta Spinner
                    if (layerTrip.getName().toString().equals("capita")){
                        placeSelected.setName(placeName.getText().toString());

                        layerTrip.getPlaces().add(placeSelected);
                        break;
                    }
                }
                //se guarda en el celu
                try {
                    FileHandler.saveTrip(tripSelected, activity);
                    onMapReadyCallback.onMapReady(activity.getmMap());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Actualiza activity
               // control.updateList(layerModify, positionLayer);
                markerSelected.setVisible(false);
                this.dismiss();
                break;

            default:
                this.dismiss();
                break;
        }

    }

}
