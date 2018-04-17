package com.example.vicky.myvacationexperience.entities;

import android.media.Rating;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by Vicky on 10/2/2018.
 */

public class Place implements Serializable{
    private String id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

    public Place(){

    }

    public Place(String id, String name, String address, LatLng latLng) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
