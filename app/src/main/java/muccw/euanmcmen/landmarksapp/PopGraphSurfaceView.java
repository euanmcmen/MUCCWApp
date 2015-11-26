package muccw.euanmcmen.landmarksapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

public class PopGraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    //The integer array of population.
    private ArrayList<Integer> populations;

    //The string array of cities
    private ArrayList<String> cities;

    //The canvas object.
    private Canvas canvas;

    //The scale to which the population is drawn onto the graph.
    //Currently 1 pixel = 1000 population units.  (scale 1000)
    //e.g. Glasgow 600,000 population units.  Bar is 600 pixels.
    private final int scale = 1000;

    //This is origin for the axes.
    private final int originX = 100;
    private int originY;

    //Dimensions for the canvas.
    private int width;
    private int height;

    //Handles the context and holder operations.
    public PopGraphSurfaceView(Context context)
    {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    //Sets the arrays.
    public void Initialise(ArrayList<String> citiesArray, ArrayList<Integer> popArray)
    {
        //Set the arrays
        cities = citiesArray;
        populations = popArray;
    }

    //Initialise the surface and draws elements on screen.
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //Initialise canvas
        //Lock the canvas so that we may edit it.
        canvas = holder.lockCanvas();

        //Set canvas values.
        width = canvas.getWidth();
        height = canvas.getHeight();

        //Set the origin location.
        originY = (height - 200);

        //Initialise the canvas to white.
        canvas.drawColor(Color.WHITE);

        //Draw components.
        drawAxes();
        drawPopulationGraphs();

        //Unlock canvas and display.
        holder.unlockCanvasAndPost(canvas);
    }

    //Draws the graph axes on screen.
    public void drawAxes()
    {
        //Initialise the paint.
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);

        //Draw X axis
        canvas.drawLine(originX, originY, width, originY, linePaint);

        //Draw X axis.
        canvas.drawLine(originX, originY, originX, 0, linePaint);
    }

    //Draws the population graphs on screen.
    public void drawPopulationGraphs()
    {
        //This is added onto the lineLocation to draw additional lines.
        int lineLocationXIncrement = 400;

        //This is the half-width of the bar
        int lineHalfWidth = 100;

        //This is the x coordinate of each line.
        int lineLocationX = originX + 200;

        //This is the Y axis point for the text to be written on.
        int textLocationY = height - 100;

        //Create paints to use.
        Paint graphPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint titleTextPaint = new Paint(Paint.UNDERLINE_TEXT_FLAG);
        Paint labelTextPaint = new Paint(Paint.LINEAR_TEXT_FLAG);

        //Set the paint colour to red to draw the lines.
        graphPaint.setColor(Color.RED);

        //Set the text size in the text paint.
        titleTextPaint.setTextSize(72);
        labelTextPaint.setTextSize(50);

        for (int i = 0; i < populations.size(); i++)
        {
            int barHeight = originY - (populations.get(i) / scale);
            //Draw the rectangle bar to represent population.
            canvas.drawRect(lineLocationX - lineHalfWidth, barHeight, lineLocationX + lineHalfWidth, originY, graphPaint);

            //Draw the text below the bar.  The text will use the city retrieved from the cities list and will be drawn under the bar.
            canvas.drawText(cities.get(i), lineLocationX - (lineHalfWidth * 1.5f), textLocationY, titleTextPaint);

            //Draw the text on the bar to show the value.
            canvas.drawText(String.valueOf(populations.get(i)), lineLocationX - (lineHalfWidth), barHeight - 100, labelTextPaint);

            //Add the incremenet to the line location.
            lineLocationX += lineLocationXIncrement;
        }

    }

    //Auto generated methods.  These are unused and unchanged.
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {

    }
}
