package muccw.euanmcmen.landmarksapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Error;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Locale;

public class DatabaseManager extends SQLiteOpenHelper
{
    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/muccw.euanmcmen.landmarksapp/databases/";
    private static final String DB_NAME = "CourseworkDB.s3db";
    private static final String TBL_SUBREDDITS = "subreddits";

    public static final String COL_CITY = "city";
    public static final String COL_URL = "url";
    public static final String COL_POPULATION = "population";
    public static final String COL_LATITUDE = "latitude";
    public static final String COL_LONGITUDE = "longitude";

    private final Context appContext;

    public DatabaseManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
            String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
                    + TBL_SUBREDDITS
                    + "("
                    + COL_CITY + " TEXT PRIMARY KEY,"
                    + COL_URL + " TEXT,"
                    + COL_POPULATION + " INTEGER,"
                    + COL_LATITUDE + " FLOAT,"
                    + COL_LONGITUDE + " FLOAT"
                    + ")";
            db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_SUBREDDITS);
            onCreate(db);
        }
    }

    // ================================================================================
    // Creates a empty database on the system and rewrites it with your own database.
    // ================================================================================
    public void dbCreate() throws IOException
    {
        if(!dbCheck())
        {
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            this.getReadableDatabase();

            try
            {
                copyDBFromAssets();
            }
            catch (IOException e)
            {
                throw new Error("Error copying database");
            }
        }
    }

    // ============================================================================================
    // Check if the database already exist to avoid re-copying the file each time you open the application.
    // @return true if it exists, false if it doesn't
    // ============================================================================================
    private boolean dbCheck()
    {
        SQLiteDatabase db = null;

        try
        {
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);

        }
        catch(SQLiteException e)
        {
            Log.e("SQLHelper","Database not Found!");
        }

        if(db != null)
        {
            db.close();
        }

        return db != null;
    }

    // ============================================================================================
    // Copies your database from your local assets-folder to the just created empty database in the
    // system folder, from where it can be accessed and handled.
    // This is done by transferring bytestream.
    // ============================================================================================
    private void copyDBFromAssets() throws IOException
    {
        InputStream dbInput;
        OutputStream dbOutput;
        String dbFileName = DB_PATH + DB_NAME;

        try {

            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0)
            {
                dbOutput.write(buffer, 0, length);
            }

            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }

    public void addCity(CityInfo dbEntry)
    {
        ContentValues values = new ContentValues();

        //Add values to the columns in the database.
        //Don't need to add the ID value because it increments automatically.
        values.put(COL_CITY, dbEntry.getCity());
        values.put(COL_URL, dbEntry.getUrl());
        values.put(COL_POPULATION, dbEntry.getPopulation());
        values.put(COL_LATITUDE, dbEntry.getLatitude());
        values.put(COL_LONGITUDE, dbEntry.getLongitude());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_SUBREDDITS, null, values);
        db.close();
    }

    public boolean removeCity(String cityName)
    {
        boolean result = false;

        String query = "SELECT * FROM " + TBL_SUBREDDITS + " WHERE " + COL_CITY + " =  \"" + cityName + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CityInfo city = new CityInfo();

        if (cursor.moveToFirst())
        {
            city.setCity(cursor.getString(0));
            db.delete(TBL_SUBREDDITS, COL_CITY + " = ?",
                    new String[] { city.getCity() });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public CityInfo getCity(String cityName)
    {
        String query = "SELECT * FROM " + TBL_SUBREDDITS + " WHERE " + COL_CITY + " =  \"" + cityName + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        CityInfo result = new CityInfo();

        if (cursor.moveToFirst())
        {
            cursor.moveToFirst();
            result.setCity(cursor.getString(0));
            result.setUrl(cursor.getString(1));
            result.setPopulation(Integer.parseInt(cursor.getString(2)));
            result.setLatitude(Double.parseDouble(cursor.getString(3)));
            result.setLongitude(Double.parseDouble(cursor.getString(4)));
            cursor.close();
        }
        else
        {
            result = null;
        }
        db.close();
        return result;
    }

    public Bundle getGraphData()
    {
        //Select all cities from the subreddits table.
        String query = "SELECT " + COL_CITY + "," + COL_POPULATION + " FROM " + TBL_SUBREDDITS;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        //Initialise result string array with row count.
        Bundle result = new Bundle();

        //initialise arraylists for each row.
        ArrayList<String> cities = new ArrayList<>();
        ArrayList<Integer> populations = new ArrayList<>();

        if (cursor.moveToFirst())
        {
            //Read the first position.
            cursor.moveToFirst();

            //Iterate over the other rows in the database.
            for (int i = 0; i < cursor.getCount(); i++)
            {
                cursor.moveToPosition(i);
                cities.add(cursor.getString(0));
                populations.add(Integer.parseInt(cursor.getString(1)));
            }
            cursor.close();
        }
        else
        {
            cities = null;
            populations = null;
        }

        result.putStringArrayList("cities", cities);
        result.putIntegerArrayList("populations", populations);

        db.close();
        return result;
    }
}
