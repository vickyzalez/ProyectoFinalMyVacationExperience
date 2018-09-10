package com.wikitude.sdksamples.activities.home_activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.wikitude.sdksamples.R;
import com.wikitude.sdksamples.activities.trip_list_activity.TripListActivity;
import com.wikitude.common.permission.PermissionManager;
import com.wikitude.samples.SimpleArActivity;
import com.wikitude.samples.util.PermissionUtil;
import com.wikitude.samples.util.SampleData;

import java.util.Arrays;

/**
 * Created by Vicky on 10/2/2018.
 */

public class HomeControl implements View.OnClickListener{

    private HomeActivity activity;
    private HomeView view;
    private HomeModel model;

    public HomeControl(HomeActivity activity, HomeModel model){
        this.activity = activity;
        this.model = model;

    }



    public void setView(HomeView view) {
        this.view = view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGoogle:

                Intent intent = new Intent(activity.getApplicationContext(), TripListActivity.class);
                activity.startActivity(intent);

                break;

            case R.id.btnWikitude:

                //TODO start wikitude activity
                final SampleData sampleData = activity.categories.get(0).getSamples().get(0);
                final String[] permissions = PermissionUtil.getPermissionsForArFeatures(sampleData.getArFeatures());
                activity.permissionManager.checkPermissions(activity, permissions, PermissionManager.WIKITUDE_PERMISSION_REQUEST, new PermissionManager.PermissionManagerCallback() {
                    @Override
                    public void permissionsGranted(int requestCode) {
                        final Intent intent = new Intent(activity, sampleData.getActivityClass());
                        intent.putExtra(SimpleArActivity.INTENT_EXTRAS_KEY_SAMPLE, sampleData);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }

                    @Override
                    public void permissionsDenied(@NonNull String[] deniedPermissions) {
                        Toast.makeText(activity, activity.getResources().getString(com.wikitude.sdksamples.R.string.permissions_denied) + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void showPermissionRationale(final int requestCode, @NonNull String[] strings) {
                        final android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(activity);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle(com.wikitude.sdksamples.R.string.permission_rationale_title);
                        alertBuilder.setMessage(activity.getResources().getString(com.wikitude.sdksamples.R.string.permission_rationale_text) + Arrays.toString(permissions));
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.permissionManager.positiveRationaleResult(requestCode, permissions);
                            }
                        });

                        android.app.AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }
                });


                break;

            default:

                break;
        }

    }



}
