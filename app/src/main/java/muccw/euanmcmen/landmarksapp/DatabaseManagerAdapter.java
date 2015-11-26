package muccw.euanmcmen.landmarksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */


public class DatabaseManagerAdapter extends ArrayAdapter<String>
{
    //Constructor for adapter object.
    public DatabaseManagerAdapter(Context context, int resource, ArrayList<String> cities)
    {
        //Call the super constructor
        super(context, resource, cities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.manager_list_item, parent, false);
        }

        //Get the city at this position
        String city = getItem(position);

        //Find view on custom list item.
        final TextView tvCity = (TextView) convertView.findViewById(R.id.tvCity);

        //Write the title of the landmark into the textview
        tvCity.setText(city);

        //Return the completed view to render on screen
        return convertView;
    }
}
