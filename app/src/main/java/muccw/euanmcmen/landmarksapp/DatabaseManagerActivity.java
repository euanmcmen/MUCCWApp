package muccw.euanmcmen.landmarksapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class DatabaseManagerActivity extends AppCompatActivity
{
    //The views.
    ListView listCities;
    Button btnAdd;

    //The cities list.
    ArrayList<String> cities;

    //The context;
    Context context;

    //Database manager.
    DatabaseManager manager;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.database_manager_screen);

        //Find views.
        listCities = (ListView) findViewById(R.id.lvCities);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        //Get the intent to retrieve the data sent from the main activity.
        Intent intent = getIntent();
        cities = intent.getStringArrayListExtra("cities");

        //Set the context.
        context = this;

        //Set up the list view.
        setupListView();

        //Set up the button click event and context menu.
        btnAdd.setClickable(true);
        registerForContextMenu(btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.showContextMenu();
            }
        });
    }

    //Set up list view with custom adapter.
    //This is it's own method so the list can be updated after collection changes.
    private void setupListView()
    {
        DatabaseManagerAdapter adapter = new DatabaseManagerAdapter(this, R.layout.display_list_item, cities);
        listCities.setAdapter(adapter);
        listCities.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                //Create dialog interface for custom adapter.
                //http://stackoverflow.com/questions/2478517/how-to-display-a-yes-no-dialog-box-in-android
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int button)
                    {
                        switch (button){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Removal code.
                                deleteEntry(position);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                //Display the confirmation box passing in the listener created.
                AboutDialogFactory.ShowDeleteConfirmationDialog(context, "Are you sure you want to delete this entry?", "Warning", listener);

                return true;
            }
        });
    }

    //Deletes a city from the database and list collection.
    private void deleteEntry(int position)
    {
        String removedCity = cities.get(position);

        //Remove from database.
        manager = new DatabaseManager(this, "CourseworkDB.s3db", null, 1);
        manager.removeCity(removedCity);
        manager.close();

        //Remove from array.
        cities.remove(removedCity);

        //update the list view
        setupListView();

        Log.d("DBA.DeleteEntry", removedCity + " has been deleted.");
    }

    //Adds a city to the database and list collection.
    private void addEntry(String city, String url, int population, double lat, double lng)
    {
        //Create cityinfo object.
        CityInfo info = new CityInfo(city, url, population, lat, lng);

        //Add to database.
        manager = new DatabaseManager(this, "CourseworkDB.s3db", null, 1);
        manager.addCity(info);
        manager.close();

        //Add to array.
        cities.add(info.getCity());

        //Update list view.
        setupListView();

        Log.d("DBA.AddEntry", info.getCity() + " has been added.");
    }

    //Creates the context menu which lists the possible cities to add.
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info)
    {
        super.onCreateContextMenu(menu, view, info);
        menu.setHeaderTitle("Add City");
        menu.add(0, view.getId(), 0, "Glasgow");
        menu.add(0, view.getId(), 1, "Edinburgh");
        menu.add(0, view.getId(), 2, "Dundee");
    }

    //Event method for context menu item selection.
    public boolean onContextItemSelected(MenuItem item)
    {
        //Check which button is pressed and add that city if it isn't present.
        //Display a toast to the user after.
        if (item.getTitle() == "Glasgow")
        {
            if (!cities.contains("Glasgow"))
            {
                //Add Glasgow.
                addEntry("Glasgow", "https://www.reddit.com/r/MUCCW_Glasgow/.rss", 600000, 55.8636084, -4.2617585);
                Toast.makeText(this, "Glasgow added.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Glasgow already exists.", Toast.LENGTH_SHORT).show();
        }

        if (item.getTitle() == "Edinburgh")
        {
            if (!cities.contains("Edinburgh"))
            {
                //Add Edinburgh
                addEntry("Edinburgh", "https://www.reddit.com/r/MUCCW_Edinburgh/.rss", 500000, 55.9411418, -3.2754228);
                Toast.makeText(this, "Edinburgh added.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Edinburgh already exists.", Toast.LENGTH_SHORT).show();
        }

        if (item.getTitle() == "Dundee")
        {
            if (!cities.contains("Dundee"))
            {
                //Add Dundee.
                addEntry("Dundee", "https://www.reddit.com/r/MUCCW_Dundee/.rss", 141870, 56.4781805, -3.1069149);
                Toast.makeText(this, "Dundee added.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Dundee already exists.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage, menu);
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
                //Show the about dialog.
                AboutDialogFactory.ShowAlertDialog(this, "This app displays the landmarks of various Scottish cities.\r\n\r\nThis screen allows you to edit the city collection.\r\n\r\nPress and hold a city to remove it." +
                        "\r\nClick Add to add an available city.", "About");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}