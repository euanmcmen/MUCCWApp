package muccw.euanmcmen.landmarksapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Euan on 15/11/2015.
 */

//This class should retrieve the landmark list from a bundle, then create latlng objects using the lat and long values from each landmark object.
//It should then display the points on the map.
//The camera should also focus in on the "general" area of the town.
public class MapActivity extends AppCompatActivity
{
    //The map.
    GoogleMap map;

    //Shared preferences.
    SharedPreferences sharedPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_screen);

        //Set the saved preferences stuff
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the intent to retrieve the landmark arraylist
        Intent intent = getIntent();
        ArrayList<Landmark> landmarksList = intent.getParcelableArrayListExtra("list");
        LatLng coords = intent.getParcelableExtra("coords");

        //Set up the map.
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.fragMap)).getMap();
        if (map != null)
        {
            //Move the camera to the centre of the city.
            //Use a temp value for now, and read from database later.
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coords, 12));
            map.setMyLocationEnabled(true);
            map.getUiSettings().setCompassEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            placeMarkers(landmarksList);
        }
    }

    public void placeMarkers(ArrayList<Landmark> lmList)
    {
        //Place markers by iterating through each landmark.
        for (Landmark lm : lmList)
        {
            //Get marker title.
            String mkrTitle = lm.getTitle();

            //Get marker location.
            LatLng mkrPosition = lm.getCoordinates();

            MarkerOptions markerOptions = setMarkerOptions(mkrTitle, mkrPosition, 120f, true);
            map.addMarker(markerOptions);
        }
    }

    public MarkerOptions setMarkerOptions(String title, LatLng position, float colour, boolean shouldCentreAnchor)
    {
        float AnchorX;
        float AnchorY;

        //If anchors are to be centred.
        if (shouldCentreAnchor)
        {
            //Anchor X and Y are centred.
            AnchorX = 0.5f;
            AnchorY = 0.5f;
        }
        else
        {
            //Anchor X is centred, Y is set to the bottom.
            AnchorX = 0.5f;
            AnchorY = 1f;
        }

        //Create marker from parameters
        MarkerOptions marker = new MarkerOptions().title(title).icon(BitmapDescriptorFactory.defaultMarker()).anchor(AnchorX, AnchorY).position(position);

        return marker;
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

        switch (id)
        {
            case R.id.About:
                //Show the about dialog.
                AboutDialogFactory.ShowAlertDialog(this, "This app displays the landmarks of various Scottish cities.\r\n\r\nThis screen displays them as a map.", "About", true);
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
