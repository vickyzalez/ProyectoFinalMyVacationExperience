package com.example.vicky.myvacationexperience.activities.place_activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.dialogs.NewPlaceDialogFragment;
import com.example.vicky.myvacationexperience.utilities.CustomMarkerInfo;
import com.example.vicky.myvacationexperience.utilities.MarkerInfoData;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.ArrayList;
import java.util.Locale;

public class PlacesActivity extends AppCompatActivity implements OnConnectionFailedListener, OnMapReadyCallback{

    private GoogleApiClient mGoogleApiClient;
    private PlacesControl ctrl;
    private GoogleMap mMap;
    PlaceAutocompleteFragment placeAutoComplete;

    private Marker lastMarker;
    private Marker lastPoi;
    private Marker lastSearched;

    public PlacesControl getCtrl() {
        return ctrl;
    }

    public void setCtrl(PlacesControl ctrl) {
        this.ctrl = ctrl;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

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

                clearMarkers();

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(place.getLatLng())      // Sets the center of the map to Mountain View
                        .zoom(16)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                //se marca el punto seleccionado en el mapa
                Marker selectedMarker = mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getName().toString()));
                lastSearched = selectedMarker;
                selectedMarker.setTag(0);
                selectedMarker.setSnippet(place.getAddress().toString());

                drawMarker(selectedMarker, place.getId());

                selectedMarker.showInfoWindow();

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
                this.setResultToPreviousActivity();
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
        intent.putExtra("Trip", ctrl.getModel().getTrip());
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

        //TODO ACA SE DEBERIAN CARGAR LOS PLACES DEL TRIP, ver con HTML
        ctrl.loadPlaces(markers, mMap);
        //Zoom
        ctrl.markersZoom(markers, mMap);

        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        //Mostrar seleccion en mapa poi
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearMarkers();
                // Return false to indicate that we have not consumed the event and that we wish
                // for the default behavior to occur (which is for the camera to move such that the
                // marker is centered and for the marker's info window to open, if it has one).
                //return false;
                drawMarker(marker, null);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                marker.showInfoWindow();
                return true;
            }
        });

        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {

                clearMarkers();

                Marker poi = mMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng));
                lastPoi = poi;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(pointOfInterest.latLng));
                poi.setTitle(pointOfInterest.name);

                String address = ctrl.getAddressFromLatLng(pointOfInterest.latLng, geocoder);
                if (address == null){
                    address = "";
                }
                poi.setSnippet(address);

                drawMarker(poi, pointOfInterest.placeId);

                poi.showInfoWindow();
            }
        });

        //Mostrar seleccion en mapa random
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                clearMarkers();

                Marker mark = mMap.addMarker(new MarkerOptions().position(latLng));
                lastMarker = mark;

                String address = ctrl.getAddressFromLatLng(latLng, geocoder);
                mark.setTitle(getResources().getString(R.string.markerSelected));
                if (address == null){
                    address = "";
                }
                mark.setSnippet(address);

                drawMarker(mark, "-1");

                mark.showInfoWindow();
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });

        final PlacesActivity activity = this;
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = NewPlaceDialogFragment.newInstance(activity, activity.getCtrl(), activity.getCtrl().getModel().getTrip(), activity.getCtrl().getModel().getPlace(), marker);
                newFragment.show(ft, "prueba");
            }
        });
    }



    public void drawMarker(Marker mark, String id) {
        MarkerInfoData info = new MarkerInfoData();
        CustomMarkerInfo customMarkerInfo = new CustomMarkerInfo(this, ctrl);
        mMap.setInfoWindowAdapter(customMarkerInfo);

        info.setMarkerName(mark.getTitle());
        info.setMarkerAddress(mark.getSnippet());
        mark.setTag(info);

        if (id != null && id != "-1"){
            ctrl.getModel().setPlace(new com.example.vicky.myvacationexperience.entities.Place(id,mark.getTitle(), mark.getSnippet(),mark.getPosition()));
        } else {
            if (id == "-1"){
                ctrl.getModel().setPlace(new com.example.vicky.myvacationexperience.entities.Place(mark.getId(),mark.getTitle(), mark.getSnippet(),mark.getPosition()));
            }
        }
    }

    private void clearMarkers() {
        if (lastPoi != null){
            lastPoi.remove();
        }
        if (lastMarker != null){
            lastMarker.remove();
        }
        if (lastSearched != null){
            lastSearched.remove();
        }
    }


}
