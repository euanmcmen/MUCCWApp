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

    //The canvas object.
    private Canvas canvas;

    //The scale to which the population is drawn onto the graph.
    //Currently 1 pixel = 1000 population units.
    //e.g. Glasgow 600,000 population units.  Bar is 600 pixels.
    int scale;

    //This is origin for the axes.
    int originX;
    int originY;

    //Dimensions for the canvas.
    int width;
    int height;

    //Handles the context and holder operations.
    public PopGraphSurfaceView(Context context)
    {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
    }

    //Sets the population array.
    public void Initialise(String[] citiesArray, int[] popArray)
    {
        //Set the arrays
        cities = citiesArray;
        populations = popArray;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //Set scale.
        scale = 1000;

        //Initialise canvas
        //Lock the canvas so that we may edit it.
        canvas = holder.lockCanvas();

        //Set canvas values.
        width = canvas.getWidth();
        height = canvas.getHeight();

        //Set the origin location.
        originX = 100;
        originY = (height - 200);

        //Initialise the canvas to white.
        canvas.drawColor(Color.WHITE);

        //Draw components.
        drawAxes();
        drawPopulationGraphs();

        //Unlock canvas and display.
        holder.unlockCanvasAndPost(canvas);
    }

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

        for (int i = 0; i < populations.length; i++)
        {
            int barHeight = originY - (populations[i] / scale);
            //Draw the rectangle bar to represent population.
            canvas.drawRect(lineLocationX - lineHalfWidth, barHeight, lineLocationX + lineHalfWidth, originY, graphPaint);

            //Draw the text below the bar.  The text will use the city retrieved from the cities list and will be drawn under the bar.
            canvas.drawText(cities[i], lineLocationX - (lineHalfWidth * 1.5f), textLocationY, titleTextPaint);

            //Draw the text on the bar to show the value.
            canvas.drawText(String.valueOf(populations[i]), lineLocationX - (lineHalfWidth), barHeight - 100, labelTextPaint);

            //Add the incremenet to the line location.
            lineLocationX += lineLocationXIncrement;
        }

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
