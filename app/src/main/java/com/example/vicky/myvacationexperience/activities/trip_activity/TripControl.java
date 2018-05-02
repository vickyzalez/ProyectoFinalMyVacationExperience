package com.example.vicky.myvacationexperience.activities.trip_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.vicky.myvacationexperience.R;
import com.example.vicky.myvacationexperience.dialogs.NewLayerDialogFragment;
import com.example.vicky.myvacationexperience.entities.LayerTrip;
import com.example.vicky.myvacationexperience.entities.Trip;
import com.example.vicky.myvacationexperience.utilities.FileHandler;
import com.example.vicky.myvacationexperience.viewholders.ItemLayerViewHolder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 18/2/2018.
 */

public class TripControl implements View.OnClickListener{

    private TripActivity activity;
    private TripModel model;
    private TripView view;
    private LayerTrip layerView; //para ver cual seleccionó
    private LayerTrip layerExp; //para ver cual seleccionó en el expandable
    private List<LayerView> adapterList;

    public List<LayerView> getAdapterList() {
        return adapterList;
    }

    public void setAdapterList(List<LayerView> adapterList) {
        this.adapterList = adapterList;
    }

    public TripControl(TripActivity activity, TripModel model)  {
        this.activity = activity;
        this.model = model;


    }

    public void setView(TripView view) {
        this.view = view;
    }

    public void setTrip(Trip trip){
        this.model.setTrip(trip);
    }

    public void loadList(){

        Trip trip = (Trip) activity.getIntent().getSerializableExtra("Trip");
        model.setTrip(trip);

        if (trip.getLayers().isEmpty()){
            view.showMessage();
        }
    }

    public List<LayerTrip> getLayers() {
        return getTrip().getLayers();
    }

    public Trip getTrip(){
        return model.getTrip();
    }

    public void updateList(LayerTrip layerTrip, Integer position){
        if(position == null){
            view.hideMessage();
            this.view.notifyItemInserted(this.getLayers().size()-1);
        } else if (layerTrip == null) {
            this.view.notifyItemRemoved(position);

            if (getLayers().isEmpty()){
                view.showMessage();
            }

        } else {
            this.getLayers().set(position, layerTrip);
            this.view.notifyItemChanged(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addLayer:
                //Para poder mostrar el popup
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
                Fragment prev = activity.getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                DialogFragment newFragment = NewLayerDialogFragment.newInstance(this.activity, this, null, null, null);
                newFragment.show(ft, "prueba");
                break;

            case R.id.chkVisible:
                ItemLayerViewHolder viewHolderCheck = new ItemLayerViewHolder((View)v.getParent());
                final Integer positionLayer = Integer.valueOf(viewHolderCheck.getTxtPosition().getText().toString());
                layerView = getSelectedLayer((View)v.getParent());
                layerView.setVisible(viewHolderCheck.getChkVisible().isChecked());

                for(LayerTrip layer: model.getTrip().getLayers()){
                    if(layer.getName().equals(layerView.getName())){
                        layer = layerView;

                        //graba trip modificado
                        try {
                            FileHandler.saveTrip(model.getTrip(), activity);
                            updateList(layer, positionLayer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;



            case R.id.txtExpandable:
                //se fija si se clickea para abrir el listado
                ItemLayerViewHolder viewHolderExp = new ItemLayerViewHolder((View)v.getParent());

                this.activateExpandable(viewHolderExp.getListView());

                layerExp = getSelectedLayer((View)v.getParent());

                break;

            case R.id.btnMoreOptions2:
                //se fija por las opciones del trip (editar/borrar)
                ItemLayerViewHolder viewHolder = new ItemLayerViewHolder((View)v.getParent());
                final Integer positionLayerSelected = Integer.valueOf(viewHolder.getTxtPosition().getText().toString());

                final Activity act = this.activity;
                final View view = v;
                final TripControl ctrl = this;
                final Trip tripModified = model.getTrip();

                PopupMenu popup = new PopupMenu(act, v);
                popup.getMenuInflater().inflate(R.menu.menu_option, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        layerView = getSelectedLayer((View)view.getParent());

                        if (item.getItemId() == R.id.menu_delete){

                            //mensaje de alerta
                            new AlertDialog.Builder(act)
                                    .setTitle(R.string.popupConfirmTitle)
                                    .setMessage(R.string.popupConfirmLayer)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {

                                            //lo elimina del trip actual
                                            tripModified.getLayers().remove(layerView);

                                            try {
                                                FileHandler.saveTrip(tripModified, activity);
                                                updateList(null, positionLayerSelected);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }})
                                    .setNegativeButton(R.string.btnCancel, null).show();

                        } else {
                            //es edit, llama de nuevo al Dialog
                            FragmentTransaction ft = act.getFragmentManager().beginTransaction();
                            Fragment prev = act.getFragmentManager().findFragmentByTag("dialog");
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            ft.addToBackStack(null);

                            // Create and show the dialog.
                            DialogFragment newFragment = NewLayerDialogFragment.newInstance(act, ctrl, layerView, model.getTrip(), positionLayerSelected);
                            newFragment.show(ft, "prueba");

                        }
                        return true;
                    }
                });

                popup.show();


                break;

            default:

                break;
        }

    }


    private LayerTrip getSelectedLayer(View v){
        ItemLayerViewHolder item = new ItemLayerViewHolder(v);

        String nameTrip = item.getTxtLayerName().getText().toString();
        LayerTrip layer = new LayerTrip();

        //buscar el trip en el listado
        for (LayerTrip currentLayer: this.model.getTrip().getLayers()){
            if (currentLayer.getName().toString().equals(nameTrip)){
                layer = currentLayer;
                break;
            }
        }

        return layer;
    }

    private void activateExpandable(ListView listView) {
        if (listView.getVisibility() == View.GONE){
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }
    }

    public TripView getView() {
        return view;
    }
}
