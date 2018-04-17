package com.example.vicky.myvacationexperience.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vicky.myvacationexperience.R;

/**
 * Created by Vicky on 10/2/2018.
 */

public class ItemLayerViewHolder extends RecyclerView.ViewHolder {
    private TextView txtLayerName;
    private ImageView iconLayer;
    private CheckBox chkVisible;
    private ImageButton btnMoreOptions;
    private TextView txtPosition;
    private TextView expandable;
    private ListView listView;
    //TODO falta la lista de places

    public ItemLayerViewHolder(View itemView) {
        super(itemView);

        btnMoreOptions = (ImageButton) itemView.findViewById(R.id.btnMoreOptions2);
        txtLayerName = (TextView) itemView.findViewById(R.id.txtLayerName);
        chkVisible = (CheckBox) itemView.findViewById(R.id.chkVisible);
        iconLayer = (ImageView) itemView.findViewById(R.id.iconLayer);
        txtPosition = (TextView) itemView.findViewById(R.id.txtPosition);
        expandable = (TextView) itemView.findViewById(R.id.txtExpandable);
        listView = (ListView) itemView.findViewById(R.id.listExpandable);
    }

    public TextView getTxtLayerName() {
        return txtLayerName;
    }

    public void setTxtLayerName(TextView txtLayerName) {
        this.txtLayerName = txtLayerName;
    }

    public ImageView getIconLayer() {
        return iconLayer;
    }

    public void setIconLayer(ImageView iconLayer) {
        this.iconLayer = iconLayer;
    }

    public CheckBox getChkVisible() {
        return chkVisible;
    }

    public void setChkVisible(CheckBox chkVisible) {
        this.chkVisible = chkVisible;
    }

    public ImageButton getBtnMoreOptions() {
        return btnMoreOptions;
    }

    public void setBtnMoreOptions(ImageButton btnMoreOptions) {
        this.btnMoreOptions = btnMoreOptions;
    }

    public TextView getTxtPosition() {
        return txtPosition;
    }

    public void setTxtPosition(TextView txtPosition) {
        this.txtPosition = txtPosition;
    }

    public TextView getExpandable() {
        return expandable;
    }

    public void setExpandable(CheckedTextView expandable) {
        this.expandable = expandable;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
