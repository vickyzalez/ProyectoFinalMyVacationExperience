package com.wikitude.sdksamples.dialogs;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.trip_activity.LayerView;
import com.wikitude.sdksamples.activities.trip_activity.TripControl;
import com.wikitude.sdksamples.entities.LayerTrip;
import com.wikitude.sdksamples.entities.Trip;
import com.wikitude.sdksamples.utilities.FileHandler;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicky on 11/2/2018.
 */

public class NewLayerDialogFragment extends DialogFragment implements View.OnClickListener, DialogInterface.OnClickListener{

    private EditText layerName;

    private Button btnLayerCancel;
    private Button btnLayerOk;

    private int icon = R.drawable.location;

    private static Activity activity;
    private static TripControl control;

    private static LayerTrip layerModify;
    private static Trip tripModify;
    private static Integer positionLayer;

    private RelativeLayout relative;

    /**
     * Create a new instance of MyDialogFragment, providing "num"
     * as an argument.
     */
    public static NewLayerDialogFragment newInstance(Activity act, TripControl tripControl, LayerTrip layer, Trip trip, Integer position) {
        NewLayerDialogFragment f = new NewLayerDialogFragment();
        activity = act;
        control = tripControl;
        layerModify = layer;
        tripModify = trip;
        positionLayer = position;

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.newlayer_dialog, container, false);

        relative = v.findViewById(R.id.viewIdRelative);
        for (int i = 0; i < relative.getChildCount(); i++){
            if (relative.getChildAt(i) instanceof ImageButton){
                relative.getChildAt(i).setOnClickListener(this);
            }
        }

        this.layerName = v.findViewById(R.id.editNameLayer);

        this.btnLayerOk = v.findViewById(R.id.btnNewLayerOk);
        this.btnLayerCancel = v.findViewById(R.id.btnNewLayerCancel);

        this.btnLayerOk.setOnClickListener(this);
        this.btnLayerCancel.setOnClickListener(this);

        if (layerModify != null){
            this.layerName.setText(layerModify.getName());
            this.icon = layerModify.getIcon();
        }

        return v;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    @Override //click normal
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iconBakery:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.bakery);
                break;

            case R.id.iconBar:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.bar);
                break;

            case R.id.iconBuilding:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.building);
                break;

            case R.id.iconCamera:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.camera);
                break;

            case R.id.iconCoffee:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.coffee);
                break;

            case R.id.iconFastFood:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.fastfood);
                break;

            case R.id.iconFun:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.fun);
                break;

            case R.id.iconHome:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.home);
                break;

            case R.id.iconIceCream:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.icecream);
                break;

            case R.id.iconLocation:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.location);
                break;

            case R.id.iconMarket:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.market);
                break;

            case R.id.iconMusic:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.music);
                break;

            case R.id.iconRestaurant:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.restaurant);
                break;

            case R.id.iconShopping:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.shopping);
                break;

            case R.id.iconStar:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.star);
                break;

            case R.id.iconTransit:
                changeBackground();
                view.setBackgroundColor(getResources().getColor(R.color.buttonSelected));
                this.icon = (R.drawable.transit);
                break;

            case R.id.btnNewLayerCancel:
                this.dismiss();
                break;

            case R.id.btnNewLayerOk:

                if(this.layerName.getText().toString().equals("")){
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.errorTitle)
                            .setMessage(R.string.emptyFieldsMsg)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {}})
                            .show();
                } else {

                    if(duplicatedLayer()){
                        new AlertDialog.Builder(activity)
                                .setTitle(R.string.errorTitle)
                                .setMessage(R.string.duplicatedLayer)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(R.string.btnOk, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {}})
                                .show();

                    } else {

                        if (layerModify == null){

                                LayerTrip layer = new LayerTrip(this.layerName.getText().toString(),this.icon,true);
                                int index = (control.getLayers().size());
                                control.getLayers().add(layer);
                                LayerView layerViewAdapter = new LayerView(control, activity, layer, tripModify, index);
                                if(control.getAdapterList() == null || control.getAdapterList().isEmpty()){
                                    List<LayerView> arrayList = new ArrayList<>();
                                    arrayList.add(layerViewAdapter);
                                    control.setAdapterList(arrayList);
                                } else {
                                    control.getAdapterList().add(layerViewAdapter);
                                }

                                //se guarda en el celu
                                try {
                                    FileHandler.saveTrip(control.getTrip(), activity);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();

                                }

                                //Actualiza activity
                                control.updateList(layer, null);

                                } else {



                                layerModify.setName(this.layerName.getText().toString());
                                layerModify.setIcon(this.icon);

                                for (LayerTrip layerTrip : tripModify.getLayers()) {
                                    if (layerTrip.getName().toString().equals(layerModify.getName().toString())) {
                                        layerTrip = layerModify;
                                    }
                                }
                                //se guarda en el celu
                                try {
                                    FileHandler.saveTrip(tripModify, activity);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //Actualiza activity
                                control.updateList(layerModify, positionLayer);
                                }
                            }

                this.dismiss();
                break;
            }
        }
    }

    private boolean duplicatedLayer() {
        for (LayerTrip layer: control.getTrip().getLayers()){
            if (layer.getName().equals(this.layerName.getText().toString())){
                return true;
            }
        }
        return false;
    }

    private void changeBackground(){
        for (int i = 0; i < relative.getChildCount(); i++){
            if (relative.getChildAt(i) instanceof ImageButton){
                relative.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

}
