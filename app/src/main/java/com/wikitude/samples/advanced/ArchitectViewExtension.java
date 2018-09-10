package com.wikitude.samples.advanced;

import com.wikitude.architect.ArchitectView;

import android.app.Activity;

public abstract class ArchitectViewExtension {
    //TODO LLEVAR

    protected final Activity activity;
    protected final ArchitectView architectView;

    public ArchitectViewExtension(Activity activity, ArchitectView architectView) {
        this.activity = activity;
        this.architectView = architectView;
    }

    public void onCreate(){}

    public void onPostCreate(){}

    public void onResume(){}

    public void onPause(){}

    public void onDestroy(){}
}
