package com.example.vicky.myvacationexperience.activities.trip_list_activity;

import com.example.vicky.myvacationexperience.entities.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class TripListModel  {

    private List<Trip> trips;

    public TripListModel()
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
