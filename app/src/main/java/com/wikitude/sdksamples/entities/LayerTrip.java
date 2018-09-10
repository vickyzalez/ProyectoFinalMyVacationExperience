package com.wikitude.sdksamples.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

public class LayerTrip implements Serializable{
    private String name;
    private Integer icon;
    private List<Place> places;
    private Boolean visible;

    public LayerTrip(){

    }

    public LayerTrip(String name, Integer icon, Boolean visible) {
        this.name = name;
        this.icon = icon;
        this.places = new ArrayList<Place>();
        this.visible = visible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}

