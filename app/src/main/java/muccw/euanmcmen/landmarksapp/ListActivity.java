package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by Euan on 31/10/2015.
 */
public class ListActivity extends Activity implements View.OnClickListener
{
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_screen);

        //Get the intent to retrieve the landmark arraylist
        Intent intent = getIntent();
        ArrayList<Landmark> landmarksList = intent.getParcelableArrayListExtra("list");

        //Find the return button and set it to be clickable.
        Button btnReturn = (Button) findViewById(R.id.btnBack);
        btnReturn.setOnClickListener(this);

        //Find the list view and set its adapter to read from the list_view_test resource file
        list = (ListView) findViewById(R.id.lvItems);
        LandmarkAdapter adapter = new LandmarkAdapter(this, R.layout.custom_list_item, landmarksList);
        list.setAdapter(adapter);
    }

    @Override
    public void onClick(View v)
    {
        setResult(Activity.RESULT_OK);
        this.finish();
    }
}
