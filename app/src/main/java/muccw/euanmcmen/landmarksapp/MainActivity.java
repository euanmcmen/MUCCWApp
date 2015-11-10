package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


            //Show a friendly toast to show what's happening.
            Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();

            //Initialise the landmarks list
            landmarks = new ArrayList<Landmark>();

            //Run the updater
            //In future, this will get the url from the database.
            new Updater().execute("https://www.reddit.com/r/MUCCW_Glasgow/.rss");

            //Set the update flag to false.
            shouldUpdate = false;
        }
    }

    class Updater extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String ... Params) //This is the city subreddit url.
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
    }
}
