package com.example.vicky.myvacationexperience.activities.trip_list_activity.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;

/**
 * Created by Vicky on 10/2/2018.
 */

public class ItemTripViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTripName;
    private TextView txtDateTo;
    private TextView txtDateFrom;
    private TextView txtId;

    public ItemTripViewHolder(View itemView) {
        super(itemView);
        txtTripName = (TextView) itemView.findViewById(R.id.txtLayerName);
        txtDateTo = (TextView) itemView.findViewById(R.id.txtDateTo);
        txtId = (TextView) itemView.findViewById(R.id.txtId);
        txtDateFrom = (TextView) itemView.findViewById(R.id.txtDateFrom);
    }

    public TextView getTxtId() {
        return txtId;
    }

    public void setTxtId(TextView txtId) {
        this.txtId = txtId;
    }

    public TextView getTxtTripName() {
        return txtTripName;
    }

    public void setTxtTripName(TextView txtTripName) {
        this.txtTripName = txtTripName;
    }

    public TextView getTxtDateTo() {
        return txtDateTo;
    }

    public void setTxtDateTo(TextView txtDateTo) {
        this.txtDateTo = txtDateTo;
    }

    public TextView getTxtDateFrom() {
        return txtDateFrom;
    }

    public void setTxtDateFrom(TextView txtDateFrom) {
        this.txtDateFrom = txtDateFrom;
    }
}
