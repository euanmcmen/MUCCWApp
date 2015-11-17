package muccw.euanmcmen.landmarksapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
public class MapActivity extends FragmentActivity
{
    //The map.
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_screen);

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

            //Get marker description.
            String mkrDesc = lm.getDescriptionText();

            //Get marker location.
            LatLng mkrPosition = lm.getCoordinates();

            MarkerOptions markerOptions = setMarkerOptions(mkrTitle, mkrDesc, mkrPosition, 120f, true);
            map.addMarker(markerOptions);
        }
    }

    public MarkerOptions setMarkerOptions(String title, String desc, LatLng position, float colour, boolean shouldCentreAnchor)
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
        MarkerOptions marker = new MarkerOptions().title(title).snippet(desc).icon(BitmapDescriptorFactory.defaultMarker()).anchor(AnchorX, AnchorY).position(position);

        return marker;
    }
}
