package muccw.euanmcmen.landmarksapp;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //Views
    Button btnDisplay;
    Button btnManage;
    Button btnPopulation;
    Spinner spCities;
    CheckBox cbRefresh;

    //Create the landmark list
    ArrayList<Landmark> landmarks = null;

    //Create the cities array
    ArrayList<String> cities = null;

    //Create the database manager object
    DatabaseManager manager = null;

    //Create array bundle object.
    //This holds the arrays from the manager, and is passed into the graph screen.
    Bundle arrayBundle = null;

    //Update flags.
    //These control when certain parts of the screen should be updated.
    boolean shouldUpdateData = true;
    boolean shouldUpdateCities = true;

    //Integer to hold the child id for the view switcher.
    //Changed through the player prefs.
    //0 is list, 1 is map.
    int initialDisplay = 0;

    //This holds the city retrieved from the database.
    //This city is used for the parsing url and gps coords.
    CityInfo city = null;

    //Shared preferences.
    SharedPreferences sharedPrefs = null;

    //Default selection for the spinner.
    //This also represents the user's "preference".
    int spinnerDefaultPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        //Set up the database manager to read the city information from.
        manager = new DatabaseManager(this, "CourseworkDB.s3db", null, 1);
        try
        {
            manager.dbCreate();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //Set the saved preferences stuff
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Update the initial display value
        initialDisplay = sharedPrefs.getInt("initial", 0);

        //Set up the buttons.
        btnManage = (Button) findViewById(R.id.btnManage);
        btnManage.setOnClickListener(this);
        btnDisplay = (Button) findViewById(R.id.btnDisplay);
        btnDisplay.setOnClickListener(this);
        btnPopulation = (Button) findViewById(R.id.btnPopulation);
        btnPopulation.setOnClickListener(this);

        //Set up the check box.
        cbRefresh = (CheckBox) findViewById(R.id.cbRefresh);

        //Set up the spinner.
        spCities = (Spinner) findViewById(R.id.spSubreddits);

        //Update cities spinner.
        updateCities();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume()
    {
        //Uncheck the checkbox in case the user unintentionally leaves this checked and reloads.
        cbRefresh.setChecked(false);

        //Update the initial display value
        initialDisplay = sharedPrefs.getInt("initial", 0);

        //Run city updater.
        updateCities();

        super.onResume();
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
                DialogFactory.showAlertDialog(this, "This app displays the landmarks of various Scottish cities.\r\n\r\nThis screen allows you to display landmarks " +
                        "of that city.\r\n\r\nPress the Settings menu button to view preferences.", "About");
                return true;
            case R.id.Preferences:
                //Show the user preferences dialog.
                DialogFactory.showPreferencesDialog(this, "Preferred City: " + sharedPrefs.getString("prefCity", "Invalid.") + "\r\nPreferred layout: " +
                        sharedPrefs.getString("prefScreen", "Invalid"), "Preferences");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == btnDisplay.getId())
        {
            //If the program should update, or the refresh button is checked, update then open a view screen.
            //The display screen will be opened after the update process in the async method's postexecute method.
            if (shouldUpdateData || cbRefresh.isChecked())
            {
                updateData();
            }
            else
            {
                openDisplayScreen();
            }
        }

        if (v.getId() == btnPopulation.getId())
        {
            //Show friendly toast to tell the user what's happening.
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();

            //Create a new intent for the population screen, and load the array bundle on.
            openIntent(PopulationGraphActivity.class, arrayBundle);
        }

        if (v.getId() == btnManage.getId())
        {
            //Show friendly toast to tell the user what's happening.
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();

            //Send the cities as a bundle to the new intent.
            Bundle citiesBundle = new Bundle();
            citiesBundle.putStringArrayList("cities", cities);

            //Mark that we want to update the cities when we return to this screen.
            shouldUpdateCities = true;

            //Show the manager screen.
            openIntent(DatabaseManagerActivity.class, citiesBundle);
        }
    }

    //Wrapper method for intent handling.
    private void openIntent(Class intentClass, Bundle bundle)
    {
        //Create the intent.
        Intent intent = new Intent(getApplicationContext(), intentClass);

        //Put each bundle in the intent.
        intent.putExtras(bundle);

        //Start intent.
        startActivity(intent);
    }

    public void updateCities()
    {
        if (shouldUpdateCities)
        {
            //Get the graph data bundle and read the cities list from it.
            arrayBundle = manager.getGraphData();
            cities = arrayBundle.getStringArrayList("cities");

            //Set up the spinner to use the updated cities array.
            //The assert statement ensures that cities isn't null, and that code-analysis is happy.
            assert cities != null;
            ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cities);
            spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCities.setAdapter(spAdapter);

            //Set up the spinner's default selection using value from playerprefs.
            spinnerDefaultPos = sharedPrefs.getInt("spinnerVal", 0);
            spCities.setSelection(spinnerDefaultPos);

            //Set up the spinner item changed listener.
            spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
                {
                    //When the spinner value changes, set the update flag to true.
                    //This way, the application will parse the new city.
                    shouldUpdateData = true;

                    //Set the player preferences with the spinner value.
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt("spinnerVal", position);
                    editor.putString("prefCity", cities.get(position));
                    editor.apply();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView)
                {

                }
            });

            //Set update flag to false
            shouldUpdateCities = false;
        }
    }

    //This method handles the execution of the updater.
    public void updateData()
    {
        //Initialise the landmarks list
        landmarks = new ArrayList<>();

        //Retrieve city from database.
        //This city object will be used for the URL and the coords for the map intent.
        city = manager.getCity(spCities.getSelectedItem().toString());

        //Run the updater
        new Updater().execute(city.getUrl());

        //Set the update flag to false to avoid unnecessary updating.
        shouldUpdateData = false;
    }

    public void openDisplayScreen()
    {
        //Create bundle and place data on.
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", landmarks);
        bundle.putParcelable("coords", city.getCoordinates());
        bundle.putInt("initial", initialDisplay);

        //Pass the bundle and class into the intent open method.
        openIntent(LandmarkDisplayActivity.class, bundle);
    }

    class Updater extends AsyncTask<String, Void, Void>
    {
        protected void onPreExecute()
        {
            //Show a friendly toast to show what's happening.
            Toast.makeText(getApplicationContext(), "Updating...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String ... Params) //This is the city url.
        {
            try
            {
                //Create arraylist collections from parser.
                landmarks = XMLParser.createCollection(Params[0]);
            }
            catch (InterruptedException | ExecutionException | XmlPullParserException | NullPointerException | IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result)
        {
            //Open the view screen after the updater has completed the background process.
            openDisplayScreen();
        }
    }
}
