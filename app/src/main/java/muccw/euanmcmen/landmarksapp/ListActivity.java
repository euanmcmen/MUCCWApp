package muccw.euanmcmen.landmarksapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;

//Euan McMenemin
//S1125095

public class ListActivity extends AppCompatActivity implements View.OnClickListener
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

        //Find the list view and set its adapter to read from the list_view_test resource file
        list = (ListView) findViewById(R.id.lvItems);
        LandmarkAdapter adapter = new LandmarkAdapter(this, R.layout.custom_list_item, landmarksList);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //If I end up adding list element click events to show the location on map,
                //here's where to put it.
            }
        });
    }

        @Override
    public void onClick(View v)
    {
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id)
        {
            case R.id.About:
                DialogFragment aboutDialog = new AboutDialog();
                aboutDialog.show(getFragmentManager(), "About_Dialog");
                return true;
            case R.id.Preferences:
                //Display the playerprefs screen.
                //Later, changing the spinner will change the "preferred city" of the user.
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
