package com.wikitude.sdksamples.activities.trip_activity;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.place_activity.PlacesActivity;
import com.wikitude.sdksamples.entities.LayerTrip;
import com.wikitude.sdksamples.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripActivity extends AppCompatActivity {
    TripControl ctrl;
    TripView viewTrip;
    String[] permisos = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] != -1 || grantResults[1]!= -1) {
            Intent intent = new Intent(this.getApplicationContext(), PlacesActivity.class);
            intent.putExtra("Trip", this.ctrl.getTrip());
            this.startActivityForResult(intent, 1);
        } else {
            Toast toast = Toast.makeText(this, this.getResources().getString(com.wikitude.sdksamples.R.string.permissions_denied), Toast.LENGTH_LONG);
            toast.show();
        }
    }
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

            LayerView layerView = new LayerView(control,this,layerTrip, model.getTrip(), indexLayer);
            adapterList.add(layerView);
            control.setAdapterList(adapterList);
            indexLayer++;
        }




        ctrl = control;
        viewTrip = view;

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

                LocationManager lm = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);

                if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                    if (ctrl.getLayers().size() == 0){
                        Toast toast = Toast.makeText(this, R.string.noLayersMap, Toast.LENGTH_LONG);
                        toast.show();
                    } else {

                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                0 );
                        //Intent intent = new Intent(this.getApplicationContext(), PlacesActivity.class);
                        //intent.putExtra("Trip", this.ctrl.getTrip());
                        //this.startActivityForResult(intent, 1);
                    }
                } else {
                    Toast toast = Toast.makeText(this, R.string.gpsMap, Toast.LENGTH_LONG);
                    toast.show();
                }
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
            //this.ctrl.getTrip().setLayers(trip.getLayers()); //probar
            this.ctrl.setTrip(trip);
            this.viewTrip.notifyDataSetChanged();

            int i = 0;

            for (LayerView adapter: ctrl.getAdapterList()){
                adapter.setTrip(trip);
                if (trip.getLayers().size() > i){
                    adapter.setLayer(trip.getLayers().get(i));
                    adapter.setPlaces(trip.getLayers().get(i).getPlaces());
                    adapter.notifyDataSetChanged();
                    i++;
                }

            }
/*
            LayerView adapter = new LayerView();
            for (int i = 0; i < ctrl.getAdapterList().size()-1; i++){
                adapter.setTrip(trip);
                adapter.setLayer(trip.getLayers().get(i));
                adapter.setPlaces(trip.getLayers().get(i).getPlaces());
                adapter.notifyDataSetChanged();
            }*/

        }
    }

}
