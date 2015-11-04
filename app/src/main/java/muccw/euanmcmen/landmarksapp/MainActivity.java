package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

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

    //Update flag.
    boolean shouldUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the buttons.
        btnViewScreen = (Button) findViewById(R.id.btnView);
        btnViewScreen.setOnClickListener(this);
        btnManage = (Button) findViewById(R.id.btnManage);
        btnManage.setOnClickListener(this);

        //Set up the radio buttons.
        rbList = (RadioButton) findViewById(R.id.rbList);
        rbMap = (RadioButton) findViewById(R.id.rbMap);

        //Find the spinner and set its adapter to read from the spinner_test resource file.
        spinner = (Spinner) findViewById(R.id.spSubreddits);
        ArrayAdapter<CharSequence> spAdapter = ArrayAdapter.createFromResource(this, R.array.subreddit_array, android.R.layout.simple_spinner_dropdown_item);
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spAdapter);

        //Run the update method.
        updateData();
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
        //Log.d("Tag", "Clicked: " + v.getId() + "\r\nView ID: " + btnViewScreen.getId());

        if (v.getId() == btnViewScreen.getId())
        {
            //Back out if, somehow, neither radio button is checked.
            if (!rbMap.isChecked() && !rbList.isChecked())
            {
                Log.d("Main.onClick", "Breaking out early.");
                return;
            }

            //Update the landmarks.
            //Normally this would be controlled with an if statement and a flag, but not yet.
            updateData();

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
    }

    public void updateData()
    {
        if (shouldUpdate)
        {
            Log.d("Main.update", "Running update method.");
            //The parser classes would also be called here.
            //This screen should also not "repeat" the parsing when the user navigates back.
            //Updating the screen should be done by the update button in the menu.
            //      Pressing that button takes the user back to this screen and calls this method.
            //      User prefs could be used to hold the value for the update flag, but not reveal it to the user.

            //Initialise the landmarks list
            landmarks = new ArrayList<Landmark>();

            //Add test landmarks.
            try
            {
                Landmark test = new Landmark("Kelvingrove Art Gallery and Museum",
                        "Kelvingrove museum has been the most popular free-to-enter visitor attraction in Glasgow, and the most visited museum in the United Kingdom outside London.",
                        new GetImageFromURL().execute("https://i.imgur.com/mtIYILo.jpg").get(),
                        55.868301,
                        -4.291835);

                Landmark test2 = new Landmark("Glasgow Science Centre",
                        "Glasgow Science Centre is a visitor attraction located in the Clyde Waterfront Regeneration area on the south bank of the River Clyde in Glasgow.",
                        new GetImageFromURL().execute("https://i.imgur.com/FkItbIo.jpg").get(),
                        55.858542,
                        -4.293803);

                landmarks.add(test);
                landmarks.add(test2);
            } catch (Exception e)
            {
                Log.e("Error", "Error thrown from getImageFromURL call.\r\n" + e.getMessage());
            }


            //Finally, set the update flag to false.
            shouldUpdate = false;
        }
    }

    class GetImageFromURL extends AsyncTask<String, Void, Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params)
        {
            try
            {
                URL imageURL = new URL(params[0]);  //Params is a collection of strings.
                Bitmap image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                return image;
            }
            catch (NetworkOnMainThreadException e)
            {
                Log.e("Error", "Network exception thrown.\r\n"+e.getMessage());
                return null;
            }
            catch (MalformedURLException e)
            {
                Log.e("Error", "MalformedURL exception thrown.\r\n"+e.getMessage());
                return null;
            }
            catch (IOException e)
            {
                Log.e("Error", "IO exception thrown.\r\n"+e.getMessage());
                return null;
            }
        }
    }
}
