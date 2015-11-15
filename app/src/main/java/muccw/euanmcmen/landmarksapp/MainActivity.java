package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//Euan McMenemin
//S1125095

public class MainActivity extends Activity implements View.OnClickListener
{
    //Views
    Button btnViewScreen;
    Button btnManage;
    RadioButton rbList;
    RadioButton rbMap;
    Spinner spinner;

    //Create the landmark list.
    ArrayList<Landmark> landmarks = null;

    //Create the database manager object.
    DatabaseManager manager = null;

    //Update flag.
    boolean shouldUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the database manager to read the city information from.
        manager = new DatabaseManager(this, "CourseworkDB.s3db", null, 1);
        try
        {
            manager.dbCreate();
            Log.d("Main.OnCreate", "Database created successfully.\r\nDatabase name: " + manager.getDatabaseName());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Log.d("Main.OnCreate", "Database didn't work.");
        }

        //Set up the buttons.
        btnViewScreen = (Button) findViewById(R.id.btnView);
        btnViewScreen.setOnClickListener(this);
        btnManage = (Button) findViewById(R.id.btnManage);
        btnManage.setOnClickListener(this);

        //Set up the radio buttons.
        rbList = (RadioButton) findViewById(R.id.rbList);
        rbMap = (RadioButton) findViewById(R.id.rbMap);

        //Set up cities array with database manager class.
        String[] citiesArray = manager.getCities();

        //Set up the array adapter to use the cities string array.
        spinner = (Spinner) findViewById(R.id.spSubreddits);
        ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, citiesArray);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                shouldUpdate = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                return;
            }

        });
        //Run the update method.
        //updateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == btnViewScreen.getId())
        {
            //Back out if, somehow, neither radio button is checked.
            if (!rbMap.isChecked() && !rbList.isChecked())
            {
                Log.d("Main.onClick", "No Radio Button Selected.");
                return;
            }

            //Update the landmarks list if shouldUpdate is true.
            //Updating the list also calls openViewScreen which displays the new intent.
            //If we don't need to update the list, simply display the new intent with OVS call.
            if (shouldUpdate)
            {
                updateData();
            }
            else
            {
                openViewScreen();
            }
        }
    }

    public void updateData()
    {
        Log.d("Main.update", "Running update method.");
        //The parser classes would also be called here.
        //This screen should also not "repeat" the parsing when the user navigates back.
        //Updating the screen should be done by the update button in the menu.
        //      Pressing that button takes the user back to this screen and calls this method.
        //      User prefs could be used to hold the value for the update flag, but not reveal it to the user.


        //Show a friendly toast to show what's happening.
        Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();

        //Initialise the landmarks list
        landmarks = new ArrayList<Landmark>();

        //Retrieve city URL from database depending on selected value in the spinner.
        //This can also be used to set the population canvas thing.
        CityInfo city = manager.getCity(spinner.getSelectedItem().toString());

        //Run the updater
        new Updater().execute(city.getUrl());

        //Set the update flag to false.
        shouldUpdate = false;
    }

    public void openViewScreen()
    {
        //Create the intent
        Intent newIntent = null;

        //Decide which intent to use by checking the radio buttons.
        if (rbList.isChecked())
        {
            //Open the list view screen.
            newIntent = new Intent(getApplicationContext(), ListActivity.class);
        }
        else if (rbMap.isChecked())
        {
            //Open the map view screen.
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
                //Get the descriptions of each landmark on the subreddit.
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
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
            catch (XmlPullParserException e)
            {
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result)
        {
            //Show another friendly toast.
            Toast.makeText(getApplicationContext(), "Complete!", Toast.LENGTH_SHORT).show();

            //Start new intent.
            openViewScreen();
        }
    }
}
