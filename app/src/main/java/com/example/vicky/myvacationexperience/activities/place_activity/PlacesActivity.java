package com.example.vicky.myvacationexperience.activities.place_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vicky.myvacationexperience.R;

import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PlacesActivity extends AppCompatActivity implements OnConnectionFailedListener, OnMapReadyCallback{

    private GoogleApiClient mGoogleApiClient;
    private PlacesControl ctrl;
    private GoogleMap mMap;
    PlaceAutocompleteFragment placeAutoComplete;

    final Activity currentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        PlacesModel model = new PlacesModel();
        PlacesControl control = new PlacesControl(this,model);
        PlacesView view = new PlacesView(control, this);
        control.setView(view);
        control.loadTrip();

        ctrl = control;

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle(model.getTrip().getName());
        actionBar.setDisplayHomeAsUpEnabled(true);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();


        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //se marca el punto seleccionado en el mapa
                Marker selectedMarker = mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName().toString()));
                selectedMarker.setTag(0);
                selectedMarker.setSnippet("Population: 776733");
                selectedMarker.showInfoWindow();

                // Set a listener for marker click.
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                            // Return false to indicate that we have not consumed the event and that we wish
                            // for the default behavior to occur (which is for the camera to move such that the
                            // marker is centered and for the marker's info window to open, if it has one).
                            return false;
                    }
                });

                Log.d("Maps", "Place selected: " + place.getName() + place.getId() + place.getAddress() + place.getRating() + place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                Log.d("Maps", "An error occurred: " + status);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        //TODO ACA SE DEBERIAN CARGAR LOS PLACES DEL TRIP
        for(LayerTrip layer: ctrl.getTrip().getLayers()){
            if (layer.getVisible()){
                for (com.example.vicky.myvacationexperience.entities.Place placeLayer: layer.getPlaces()){
                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(placeLayer.getLatitude(),placeLayer.getLongitude()))
                            .title(placeLayer.getName().toString())
                            .icon(BitmapDescriptorFactory.fromResource(layer.getIcon()));

                    mMap.addMarker(marker);
                    markers.add(marker);

                }
            }
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions mark : markers) {
            builder.include(mark.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }

}
