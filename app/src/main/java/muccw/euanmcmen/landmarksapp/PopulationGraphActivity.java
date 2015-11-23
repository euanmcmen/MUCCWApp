package muccw.euanmcmen.landmarksapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Created by Euan on 21/11/2015.
 */
public class PopulationGraphActivity extends AppCompatActivity
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

        //Set the content view.
        setContentView(popGraphSurfaceView);

        //Retrieve arrays from the intent bundle.
        Intent intent = getIntent();
        String[] cities = intent.getStringArrayExtra("cities");
        int[] populations = intent.getIntArrayExtra("populations");

        //Initialise the surface view.
        popGraphSurfaceView.Initialise(cities, populations);

        //Set the saved preferences stuff
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_graph, menu);
        return true;
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
                AboutDialogFactory.ShowAlertDialog(this, "This app displays the landmarks of various Scottish cities.\r\n\r\nThis screen displays a graph of the city populations.", "About", true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
