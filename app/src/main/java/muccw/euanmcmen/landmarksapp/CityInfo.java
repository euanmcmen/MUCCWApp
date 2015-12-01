package muccw.euanmcmen.landmarksapp;

import com.google.android.gms.maps.model.LatLng;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class CityInfo
{
    private String city;
    private String url;
    private int population;
    private double latitude;
    private double longitude;

    //Standard constructor for a city object.
    public CityInfo(String city, String url, int population, double latitude, double longitude)
    {
        this.city = city;
        this.url = url;
        this.population = population;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Blank constructor for city object.
    public CityInfo() { }

    //Getters and setters for instance variables.
    //Auto-generated.

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

    //Returns a latlng object using latitude and longitude.
    public LatLng getCoordinates()
    {
        return new LatLng(latitude, longitude);
    }
}
