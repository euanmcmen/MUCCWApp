package muccw.euanmcmen.landmarksapp;

import java.io.Serializable;

/**
 * Created by Euan on 15/11/2015.
 */
public class CityInfo implements Serializable
{
    private String city;
    private String url;
    private int population;

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
}
