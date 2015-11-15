package muccw.euanmcmen.landmarksapp;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Euan on 15/11/2015.
 */
public class MapActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_screen);

        //This class should retrieve the landmark list from a bundle, then create latlng objects using the lat and long values from each landmark object.
        //It should then display the points on the map.
        //The camera should also focus in on the "general" area of the town.
    }

}
