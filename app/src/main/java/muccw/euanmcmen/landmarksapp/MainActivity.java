package muccw.euanmcmen.landmarksapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

//Euan McMenemin
//S1125095

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //Views
    Button btnList;
    Button btnMap;
    Button btnManage;
    Spinner spCities;
    CheckBox cbRefresh;

    //Create the landmark list.
    ArrayList<Landmark> landmarks = null;

    //Create the database manager object.
    DatabaseManager manager = null;

    //Update flag.
    //This is changed within the update method.
    boolean shouldUpdate = true;

    //Flag to determine if the list view or map view is displayed depending on which button the user pressed.
    //Needs to be public scope to allow the async thread to access it, plus it's used in multiple methods.
    boolean displayAsList = true;

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

        //Set up the buttons.
        btnList = (Button) findViewById(R.id.btnList);
        btnList.setOnClickListener(this);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnMap.setOnClickListener(this);
        btnManage = (Button) findViewById(R.id.btnManage);
        btnManage.setOnClickListener(this);

        //Set up the check box.
        cbRefresh = (CheckBox) findViewById(R.id.cbRefresh);

        //Set up the spinner.
        spCities = (Spinner) findViewById(R.id.spSubreddits);

        //Set up cities array with database manager class.
        //Set as final so it may be used in the setOnItemSelectedListener inner method.
        final String[] citiesArray = manager.getCities();

        //Set up the spinner adapter to use the cities string array.
        ArrayAdapter<String> spAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, citiesArray);
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
                shouldUpdate = true;

                //Set the player preferences with the spinner value.
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("spinnerVal", position);
                editor.putString("city", citiesArray[position]);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }

        });
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

    @Override
    public void onClick(View v)
    {
        if (v.getId() == btnList.getId())
        {
            //Launch map screen by setting displayAsList parameter to true.
            HandleUpdateAndDisplay(true);
        }

        if (v.getId() == btnMap.getId())
        {
            //Launch map screen by setting displayAsList parameter to false.
            HandleUpdateAndDisplay(false);
        }
    }

    private void HandleUpdateAndDisplay(boolean setDisplayAsList)
    {
        //This method "wraps" the update and display methods.

        //Set the displayAsList value to the passed setDisplayAsList value.
        displayAsList = setDisplayAsList;

        //If the program should update, or the refresh button is checked, update then open a view screen.
        //The view screen will be opened after the update process in the async method's postexecute method.
        //Otherwise, just open a view screen.
        if (shouldUpdate || cbRefresh.isChecked())
        {
            updateData();
        }
        else
        {
            openViewScreen();
        }
    }

    public void updateData()
    {
        //Show a friendly toast to show what's happening.
        Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();

        //Initialise the landmarks list
        landmarks = new ArrayList<>();

        //Retrieve city from database.
        //This city object will be used for the URL and the coords for the map intent.
        city = manager.getCity(spCities.getSelectedItem().toString());

        //Run the updater
        new Updater().execute(city.getUrl());

        //Set the update flag to false to avoid unnecessary updating.
        shouldUpdate = false;
    }

    public void openViewScreen()
    {
        //Create the intent with a null value.
        Intent newIntent;

        //Decide which intent to use by checking the radio buttons.
        if (displayAsList)
        {
            //Open the list view screen.
            newIntent = new Intent(getApplicationContext(), ListActivity.class);
        }
        else
        {
            //Open the map view screen.
            newIntent = new Intent(getApplicationContext(), MapActivity.class);
            newIntent.putExtra("coords", city.getCoordinates());
        }

        //Load the list onto the intent.
        newIntent.putParcelableArrayListExtra("list", landmarks);

        //Launch the intent.
        startActivity(newIntent);
    }

    class Updater extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String ... Params) //This is the city url.
        {
            try
            {
                Log.d("Main.Updater.Backgrnd.", "In the background method.");
                //Get the descriptions of each landmark on the datasource.
                String result = HTTPFeedReader.ReadXMLFeed(Params[0]);

                Log.d("Main.Updater.Backgrnd.", "Result string:\r\n" + result);

                //If the result string isn't null, do some stuff with it.  It shouldn't be null.
                if (!result.equals(""))
                {
                    //Create arraylist collection of landmarks.
                    XMLParser parser = new XMLParser(result);
                    landmarks = parser.CreateCollection();
                }
            }
            catch (InterruptedException | ExecutionException | XmlPullParserException | NullPointerException | IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result)
        {
            //Show another friendly toast.
            Toast.makeText(getApplicationContext(), "Complete!", Toast.LENGTH_SHORT).show();

            //Open the view screen after the updater has completed the background process.
            openViewScreen();
        }
    }
}
