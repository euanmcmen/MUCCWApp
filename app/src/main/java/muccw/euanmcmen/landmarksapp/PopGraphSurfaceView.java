package muccw.euanmcmen.landmarksapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Euan on 21/11/2015.
 */
public class PopGraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    //The surface holder object.
    private SurfaceHolder holder;

    //The integer array of population.
    private int[] populations;

    //The string array of cities
    private String[] cities;

    //Handles the context and holder operations.
    public PopGraphSurfaceView(Context context)
    {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    //Sets the population array.
    public void Initialise(int[] popArray, String[] citiesArray)
    {
        populations = popArray;
        cities = citiesArray;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        drawPopulationGraphs();
    }

    public void drawPopulationGraphs()
    {
        //Lock the canvas so that we may edit it.
        Canvas canvas = holder.lockCanvas();

        //Initialise the canvas to white.
        canvas.drawColor(Color.WHITE);

        //The scale to which the population is drawn onto the graph.
        //Currently 1 pixel = 1000 population units.
        //e.g. Glasgow 600,000 population units.  Bar is 600 pixels.
        int scale = 1000;

        //This is added onto the lineLocation to draw additional lines.
        int lineLocationXIncrement = 400;

        //This is the half-width of the bar
        int lineHalfWidth = 100;

        //This is the x coordinate of each line.
        int lineLocationX = 200;

        //This is the Y axis starting location for the line.
        int lineLocationY = canvas.getHeight() - 200;

        //This is the Y axis point for the text to be written on.
        int textLocationY = canvas.getHeight() - 100;

        //Create paints to use.
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint textPaint = new Paint(Paint.UNDERLINE_TEXT_FLAG);

        //Set the paint colour to red to draw the lines.
        linePaint.setColor(Color.RED);

        //Set the text size in the text paint.
        textPaint.setTextSize(72);

        for (int i = 0; i < populations.length; i++)
        {
            //XStart, YStart, XFinish, YFinish, Paint.
            //left, top, right, bottom, paint.
            canvas.drawRect(lineLocationX - lineHalfWidth, populations[i] / scale, lineLocationX + lineHalfWidth, lineLocationY, linePaint);

            //Draw the text below the bar.  The text will use the city retrieved from the cities list and will be drawn under the bar.
            canvas.drawText(cities[i], lineLocationX - (lineHalfWidth * 1.5f), textLocationY , textPaint);

            //Add the incremenet to the line location.
            lineLocationX += lineLocationXIncrement;
        }

        //Unlock canvas and display.
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }
}
