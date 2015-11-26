package muccw.euanmcmen.landmarksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class LandmarkAdapter extends ArrayAdapter<Landmark>
{
    //Constructor for adapter object.
    public LandmarkAdapter(Context context, int resource, ArrayList<Landmark> landmarks)
    {
        //Call the super constructor
        super(context, resource, landmarks);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.display_list_item, parent, false);
        }

        //Get the landmark at this position
        Landmark landmark = getItem(position);

        //Find views on custom list item.
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

        //Write the title of the landmark into the textview
        tvTitle.setText(landmark.getTitle());

        //Write the description of the landmark to the textview
        tvDescription.setText(landmark.getDescriptionText());

        //and the image into the imageview.
        ivImage.setImageBitmap(landmark.getImage());

        //Return the completed view to render on screen
        return convertView;
    }
}
