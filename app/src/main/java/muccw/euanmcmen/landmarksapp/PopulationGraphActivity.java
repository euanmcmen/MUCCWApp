package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by Euan on 21/11/2015.
 */
public class PopulationGraphActivity extends Activity
{
    //Shared preferences.
    SharedPreferences sharedPrefs = null;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.population_graph_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Create the surface view.
        PopGraphSurfaceView popGraphSurfaceView = new PopGraphSurfaceView(this);

        //Retrieve arrays from the intent bundle.
        Intent intent = getIntent();
        int[] populations = intent.getIntArrayExtra("populations");
        String[] cities = intent.getStringArrayExtra("cities");

        //Initialise the surface view.
        popGraphSurfaceView.Initialise(populations, cities);

        //Set the content view.
        setContentView(popGraphSurfaceView);

        //Set the saved preferences stuff
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.About:
                //Show the about dialog.
                AboutDialogFactory.ShowAlertDialog(this, "This app displays the landmarks of various Scottish cities.\r\n\r\nThis screen allows you to change cities, and display landmarks " +
                        "of that city on a map or list.", "About", true);
                return true;
            case R.id.Preferences:
                //Show the user preferences dialog.
                AboutDialogFactory.ShowAlertDialog(this, "Preferred City: " + sharedPrefs.getString("city", "None."), "Preferences", false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
