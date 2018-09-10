package com.wikitude.sdksamples.activities.home_activity;

import com.wikitude.sdksamples.entities.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class HomeModel {

    private List<Trip> trips;

    public HomeModel()
    {
        this.trips= new ArrayList<Trip>();
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
