package com.example.vicky.myvacationexperience.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class Trip implements Serializable {

    private Integer id;
    private String name;
    private String fromDate;
    private String toDate;
    private String photo;
    private List<LayerTrip> layers;

    public Trip(){

    }

    public Trip(Integer id, String name, String fromDate, String toDate, String photo) {
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.photo = photo;
        this.layers = new ArrayList<LayerTrip>();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<LayerTrip> getLayers() {
        return layers;
    }

    public void setLayers(List<LayerTrip> layers) {
        this.layers = layers;
    }
}
