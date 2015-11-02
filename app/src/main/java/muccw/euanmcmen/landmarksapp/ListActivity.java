package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Euan on 31/10/2015.
 */
public class ListActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_screen);

        //Find the list view and set its adapter to read from the list_view_test resource file
        ListView list = (ListView) findViewById(R.id.lvItems);
        ArrayAdapter<CharSequence> lvAdapter = ArrayAdapter.createFromResource(this, R.array.list_of_items, android.R.layout.simple_list_item_1);
        lvAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        list.setAdapter(lvAdapter);
    }
}
