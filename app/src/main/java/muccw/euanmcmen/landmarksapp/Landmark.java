package muccw.euanmcmen.landmarksapp;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class Landmark implements Parcelable
{
    private String titleText;
    private String descriptionText;
    private String imageUrl;
    private Bitmap image;
    private double latitude;
    private double longitude;

    //Default constructor.
    public Landmark()
    {
        titleText = null;
        descriptionText = null;
        imageUrl = null;
        image = null;
        latitude = 0.0;
        longitude = 0.0;
    }

    //Constructor which takes in a parcel.  This is necessary for the class to implement parcelable.
    public Landmark(Parcel in)
    {
        titleText = in.readString();
        descriptionText = in.readString();
        imageUrl = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    //Getters and setters for instance variables.
    //Auto-generated.
    public String getTitle()
    {
        return titleText;
    }

    public void setTitle(String title)
    {
        this.titleText = title;
    }

    public String getDescriptionText()
    {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText)
    {
        this.descriptionText = descriptionText;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    //Returns a latlng object consisting of the latitude and longitude of the landmark.
    public LatLng getCoordinates()
    {
        return new LatLng(latitude, longitude);
    }

    //Parcelable method.  Unchanged from auto-generation.
    @Override
    public int describeContents()
    {
        return 0;
    }

    //Writes instance fields to a parcel.
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(titleText);
        dest.writeString(descriptionText);
        dest.writeString(imageUrl);
        dest.writeParcelable(image, 0);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    //Parcel CREATOR object.  Again, unchanged from auto-generation.
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator()
    {
        public Landmark createFromParcel(Parcel in)
        {
            return new Landmark(in);
        }

        public Landmark[] newArray(int size)
        {
            return new Landmark[size];
        }
    };
}
