package com.wikitude.sdksamples.activities.place_activity;

import com.wikitude.sdksamples.entities.Place;
import com.wikitude.sdksamples.entities.Trip;

/**
 * Created by Vicky on 10/2/2018.
 */

public class PlacesModel {

    private Trip trip;
    private Place place;
    private Place placeSelected;

    public Place getPlaceSelected() {
        return placeSelected;
    }

    public void setPlaceSelected(Place placeSelected) {
        this.placeSelected = placeSelected;
    }

    public PlacesModel()
    {

    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
