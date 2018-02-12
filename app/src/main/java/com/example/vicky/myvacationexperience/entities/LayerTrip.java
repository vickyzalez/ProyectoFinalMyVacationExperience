package com.example.vicky.myvacationexperience.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 10/2/2018.
 */

class LayerTrip {
    private String name;
    private String icon;
    private List<Place> places;
    private Boolean visible;

    public LayerTrip(String name, String icon, Boolean visible) {
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
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

