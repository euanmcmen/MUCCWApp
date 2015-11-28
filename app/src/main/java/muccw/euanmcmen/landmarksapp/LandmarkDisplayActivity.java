package muccw.euanmcmen.landmarksapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class LandmarkDisplayActivity extends AppCompatActivity
{
    //The map.
    GoogleMap mapLandmarks;

    //The list.
    ListView listLandmarks;

    //The view flipper.
    ViewSwitcher switcher;

    //The initial screen to display.
    int initialDisplay;

    //This landmarks list.
    ArrayList<Landmark> landmarks;

    //Coordinates of the city.
    LatLng coords;

    //Shared preferences.
    SharedPreferences sharedPrefs = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.landmark_display_screen);

        //Find the views.
        listLandmarks = (ListView) findViewById(R.id.lvItems);
        mapLandmarks = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragMap)).getMap();
        switcher = (ViewSwitcher) findViewById(R.id.vsSwitcher);

        //Set the saved preferences stuff
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the intent to retrieve the data sent from the main activity.
        //The landmarksList collection at this point is incomplete.
        Intent intent = getIntent();
        landmarks = intent.getParcelableArrayListExtra("list");
        coords = intent.getParcelableExtra("coords");
        initialDisplay = intent.getIntExtra("initial",-1);

        //Complete the landmarks list by filling it with images.
        //Map code was moved into this method so that the landmark image can be fed into the markeroptions.
        new CompleteLandmarksTask().execute();
    }

    //Switches views and updates preferences.
    private void switchViews()
    {
        //If one screen is display, flip to the other.
        if (switcher.getDisplayedChild() == 0)
        {
            switcher.showNext();
        }
        else if (switcher.getDisplayedChild() == 1)
        {
            switcher.showPrevious();
        }

        //Write the new value to player preferences.
        int newDisplayedChild = switcher.getDisplayedChild();
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("initial", newDisplayedChild);
        editor.putString("prefScreen", getViewName(newDisplayedChild));
        editor.apply();
    }

    //Returns a string representation of the displayed view.
    //Used in preferences.
    private String getViewName(int displayedChild)
    {
        if (displayedChild == 0)
            return "List";
        else if (displayedChild == 1)
            return "Map";
        else return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
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
                DialogFactory.ShowAlertDialog(this, "This app displays the landmarks of various Scottish cities.\r\n\r\nThis screen displays the landmarks.\r\n" +
                        "Use the Cycle button on the menu to swap displays.", "About");
                return true;
            case R.id.Switch:
                //Switch views.
                switchViews();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //This class fills the images of the incomplete landmarks list.
    class CompleteLandmarksTask extends AsyncTask<Void, Void, Void>
    {
        protected void onPreExecute()
        {
            //Show a friendly toast to show what's happening.
            Toast.makeText(getApplicationContext(), "Rendering.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void ... Params)
        {
            //For each landmark...
            for (int i = 0; i < landmarks.size(); i++)
            {
                try
                {
                    //Get images from location
                    URL imageURL  = new URL(landmarks.get(i).getImageUrl());
                    Bitmap image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                    //Put image in landmark list.
                    landmarks.get(i).setImage(image);

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void result)
        {
            //Set up the list view with custom adapter and layout.
            LandmarkAdapter adapter = new LandmarkAdapter(getApplicationContext(), R.layout.display_list_item, landmarks);
            listLandmarks.setAdapter(adapter);

            //Move the camera to the centre of the city.
            mapLandmarks.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 10));
            mapLandmarks.setMyLocationEnabled(true);
            mapLandmarks.getUiSettings().setCompassEnabled(true);
            mapLandmarks.getUiSettings().setMyLocationButtonEnabled(true);
            mapLandmarks.getUiSettings().setZoomControlsEnabled(true);

            //Place markers by iterating through each landmark.
            for (Landmark lm : landmarks)
            {
                //Place marker on map.
                MarkerOptions markerOptions = new MarkerOptions().title(lm.getTitle()).icon(BitmapDescriptorFactory.fromBitmap(lm.getImage())).anchor(0.5f, 0.5f).position(lm.getCoordinates());
                mapLandmarks.addMarker(markerOptions);
            }

            //Set the initial view.
            switcher.setDisplayedChild(initialDisplay);
        }
    }
}
