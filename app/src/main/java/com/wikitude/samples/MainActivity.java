package com.wikitude.samples;

import com.wikitude.samples.util.PermissionUtil;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;
import com.wikitude.common.util.SDKBuildInformation;
import com.wikitude.samples.util.SampleCategory;
import com.wikitude.samples.util.SampleData;
import com.wikitude.samples.util.SampleJsonParser;
import com.wikitude.samples.util.adapters.SamplesExpendableListAdapter;
import com.wikitude.samples.util.urllauncher.UrlLauncherStorageActivity;
import com.wikitude.sdksamples.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The MainActivity is used to display the list of samples and handles the runtime
 * permissions for the sample activities.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //TODO LLEVAR
    private static final String sampleDefinitionsPath = "samples/samples.json";

    private final PermissionManager permissionManager = ArchitectView.getPermissionManager();
    private List<SampleCategory> categories;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Te levanta las categorias para poner en pantalla
        final String json = SampleJsonParser.loadStringFromAssets(this, sampleDefinitionsPath);
        categories = SampleJsonParser.getCategoriesFromJsonString(json);

        int supportedFeaturesForDevice = ArchitectView.getSupportedFeaturesForDevice(this);
        for (Iterator<SampleCategory> catIt = categories.iterator(); catIt.hasNext(); ) {
            final SampleCategory category = catIt.next();

            for (Iterator<SampleData> dataIt = category.getSamples().iterator(); dataIt.hasNext(); ) {
                final SampleData data = dataIt.next();

                int arFeatures = data.getArFeatures();
                if ((arFeatures & supportedFeaturesForDevice) != arFeatures) {
                    dataIt.remove();
                }
            }
            if (category.getSamples().size() == 0) {
                catIt.remove();
            }
        }

        final SamplesExpendableListAdapter adapter = new SamplesExpendableListAdapter(this, categories);

        button = findViewById(R.id.btnWikitude);
        button.setOnClickListener(this);

        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("My Vacation Experience");
        actionBar.setDisplayHomeAsUpEnabled(true);

        /*final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArchitectView.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /*public void launchCustomUrl(MenuItem item) {
        final Intent intent = new Intent(this, UrlLauncherStorageActivity.class);
        startActivity(intent);
    }

    public void showSdkBuildInformation(MenuItem item) {
        final SDKBuildInformation sdkBuildInformation = ArchitectView.getSDKBuildInformation();
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.build_information_title)
                .setMessage(
                        getString(R.string.build_information_config) + sdkBuildInformation.getBuildConfiguration() + "\n" +
                        getString(R.string.build_information_date) + sdkBuildInformation.getBuildDate() + "\n" +
                        getString(R.string.build_information_number) + sdkBuildInformation.getBuildNumber() + "\n" +
                        getString(R.string.build_information_version) + ArchitectView.getSDKVersion()
                )
                .show();
    }*/

    @Override
    public void onClick(View v) {

        //TODO esto va a ser un onClickListener
        final SampleData sampleData = categories.get(0).getSamples().get(0);
        final String[] permissions = PermissionUtil.getPermissionsForArFeatures(sampleData.getArFeatures());
        permissionManager.checkPermissions(MainActivity.this, permissions, PermissionManager.WIKITUDE_PERMISSION_REQUEST, new PermissionManager.PermissionManagerCallback() {
            @Override
            public void permissionsGranted(int requestCode) {
                final Intent intent = new Intent(MainActivity.this, sampleData.getActivityClass());
                intent.putExtra(SimpleArActivity.INTENT_EXTRAS_KEY_SAMPLE, sampleData);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            @Override
            public void permissionsDenied(@NonNull String[] deniedPermissions) {
                Toast.makeText(MainActivity.this, getString(R.string.permissions_denied) + Arrays.toString(deniedPermissions), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showPermissionRationale(final int requestCode, @NonNull String[] strings) {
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle(R.string.permission_rationale_title);
                alertBuilder.setMessage(getString(R.string.permission_rationale_text) + Arrays.toString(permissions));
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionManager.positiveRationaleResult(requestCode, permissions);
                    }
                });

                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });

    }
}
