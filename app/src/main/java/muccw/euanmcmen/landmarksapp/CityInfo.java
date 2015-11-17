package muccw.euanmcmen.landmarksapp;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by Euan on 15/11/2015.
 */
public class CityInfo implements Serializable
{
    private String city;
    private String url;
    private int population;
    private double latitude;
    private double longitude;

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public int getPopulation()
    {
        return population;
    }

    public void setPopulation(int population)
    {
        this.population = population;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public LatLng getCoordinates()
    {
        return new LatLng(latitude, longitude);
    }
}
