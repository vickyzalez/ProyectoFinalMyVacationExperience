package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.content.Intent;
import android.media.Rating;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.place_activity.PlacesActivity;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Place;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TripActivity extends AppCompatActivity {
    TripControl ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        List<LayerView> adapterList = new ArrayList<LayerView>();

        TripModel model = new TripModel();
        TripControl control = new TripControl(this,model);
        TripView view = new TripView(control, this);
        control.setView(view);
        control.loadList();

        int indexLayer = 0;

        for (LayerTrip layerTrip: model.getTrip().getLayers()){
            //TODO se debe eliminar cuando ya se puedan cargar los plances
            if (layerTrip.getPlaces().size() == 0){
                layerTrip.getPlaces().add(new Place("ChIJfdN7-eVYwokRUi2PrjJ99e4", "Dylan's Candy Bar", "1011 3rd Ave, New York, NY 10065, EE. UU.",
                        new LatLng(40.7622797,-73.9679976)));
                layerTrip.getPlaces().add(new Place("ChIJqaiomQBZwokRTHOaUG7fUTs", "New York Public Library", "476 5th Ave, New York, NY 10018, EE. UU.",
                        new LatLng(40.7540274,-73.9839411)));
            }

            LayerView layerView = new LayerView(control,this,layerTrip, model.getTrip(), indexLayer);
            adapterList.add(layerView);
            control.setAdapterList(adapterList);
            indexLayer++;
        }




        ctrl = control;

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(model.getTrip().getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_layers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.setResultToPreviousActivity();
                this.finish();
                return true;

            case R.id.actionMap:

                Intent intent = new Intent(this.getApplicationContext(), PlacesActivity.class);
                intent.putExtra("Trip", this.ctrl.getTrip());
                this.startActivityForResult(intent, 1);
                return true;

            default:
                return false;
        }

    }

    @Override
    public void onBackPressed(){
        this.setResultToPreviousActivity();
        super.onBackPressed();
    }

    private void setResultToPreviousActivity(){
        Intent intent = new Intent();
        intent.putExtra("Trip", ctrl.getTrip());
        this.setResult(1, intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == 1){
            super.onActivityResult(requestCode,resultCode,data);
            Trip trip = (Trip) data.getSerializableExtra("Trip");
            this.ctrl.getTrip().setLayers(trip.getLayers()); //probar
        }
    }

}
