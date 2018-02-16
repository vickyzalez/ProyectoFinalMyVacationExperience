package com.example.vicky.myvacationexperience.activities.trip_list_activity;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.activities.trip_list_activity.viewholders.ItemTripViewHolder;
import com.example.vicky.myvacationexperience.entities.Trip;

/**
 * Created by Vicky on 10/2/2018.
 */

public class TripListView extends RecyclerView.Adapter<ItemTripViewHolder> {

    private FloatingActionButton addTrip;
    private RecyclerView recyclerView;
    private TripListControl ctrl;

    public TripListView(TripListControl ctrl,Activity activity) {
        //boton nuevo trip
        this.addTrip = (FloatingActionButton) activity.findViewById(R.id.addTrip);
        addTrip.setOnClickListener(ctrl); //Es el controlador quien implementa Listener

        this.recyclerView = (RecyclerView) activity.findViewById(R.id.trips);
        LinearLayoutManager layoutManager = new LinearLayoutManager(activity); //manejador de lista
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(this);

        this.ctrl = ctrl;


    }

    @Override
    public ItemTripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        v.setOnClickListener(ctrl);

        ItemTripViewHolder itemTripViewHolder = new ItemTripViewHolder(v);
        return itemTripViewHolder;
    }

    @Override
    public void onBindViewHolder(ItemTripViewHolder holder, int position) {
        Trip trip = ctrl.getTrips().get(position);
        holder.getTxtTripName().setText(trip.getName());
        holder.getTxtDateFrom().setText(trip.getFromDate());
        holder.getTxtDateTo().setText(trip.getToDate());
        holder.getTxtId().setText(trip.getId().toString());

    }
    @Override
    public int getItemCount() {
        return this.ctrl.getTrips().size();
    }


}
