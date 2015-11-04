package muccw.euanmcmen.landmarksapp;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Euan on 03/11/2015.
 */
public class Landmark implements Parcelable
{
    private String titleText;
    private String descriptionText;
    private Bitmap image;
    private double latitude;
    private double longitude;

    public Landmark(String title, String desc, Bitmap img, double lat, double lon)
    {
        //Set fields
        titleText = title;
        descriptionText = desc;
        image = img;
        latitude = lat;
        longitude = lon;
    }

    public Landmark(Parcel in)
    {
        titleText = in.readString();
        descriptionText = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

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

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(int latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(int longitude)
    {
        this.longitude = longitude;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(titleText);
        dest.writeString(descriptionText);
        dest.writeParcelable(image, 0);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

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
