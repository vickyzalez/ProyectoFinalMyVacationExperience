package com.wikitude.sdksamples.activities.home_activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.wikitude.sdksamples.R;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;
import com.wikitude.common.util.SDKBuildInformation;
import com.wikitude.samples.util.SampleCategory;
import com.wikitude.samples.util.SampleData;
import com.wikitude.samples.util.SampleJsonParser;
import com.wikitude.samples.util.adapters.SamplesExpendableListAdapter;
import com.wikitude.samples.util.urllauncher.UrlLauncherStorageActivity;

import java.util.Iterator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private HomeControl control;
    private static final String sampleDefinitionsPath = "samples/samples.json";

    public final PermissionManager permissionManager = ArchitectView.getPermissionManager();
    public List<SampleCategory> categories;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_layout);

        ActionBar bar = this.getSupportActionBar();
        bar.setTitle("My Vacation Experience");

        HomeModel model = new HomeModel();
        HomeControl ctrl = new HomeControl(this, model);
        HomeView view = new HomeView(ctrl, this);

        ctrl.setView(view); //se le inserta la view porque no se instancia en el mismo momento

        control = ctrl;

        //Es full wikitude
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


        //final Toolbar toolbar = findViewById(com.wikitude.sdksamples.R.id.toolbar);
        //setSupportActionBar(toolbar);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.wikitude.sdksamples.R.menu.main_menu, menu);
        return true;
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArchitectView.getPermissionManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
/*

    public void launchCustomUrl(MenuItem item) {
        final Intent intent = new Intent(this, UrlLauncherStorageActivity.class);
        startActivity(intent);
    }

    public void showSdkBuildInformation(MenuItem item) {
        final SDKBuildInformation sdkBuildInformation = ArchitectView.getSDKBuildInformation();
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle(com.wikitude.sdksamples.R.string.build_information_title)
                .setMessage(
                        getString(com.wikitude.sdksamples.R.string.build_information_config) + sdkBuildInformation.getBuildConfiguration() + "\n" +
                                getString(com.wikitude.sdksamples.R.string.build_information_date) + sdkBuildInformation.getBuildDate() + "\n" +
                                getString(com.wikitude.sdksamples.R.string.build_information_number) + sdkBuildInformation.getBuildNumber() + "\n" +
                                getString(com.wikitude.sdksamples.R.string.build_information_version) + ArchitectView.getSDKVersion()
                )
                .show();
    }
*/
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == 1){
            super.onActivityResult(requestCode,resultCode,data);
            Trip trip = (Trip) data.getSerializableExtra("Trip");
            this.control.updateList(trip, trip.getId());
        }
    }
    */
}
