package muccw.euanmcmen.landmarksapp;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

//Euan McMenemin
//S1125095

//This class handles the interation with the XML string after the feed read.
public class XMLParser 
{
	//XML string fed in through the constructor.
	private String xmlString;
	
	//Constructor which takes in the string containing the XML to be parsed, and the context of the activity class.
	public XMLParser(String xmlString)
	{
		this.xmlString = xmlString;
	}

	//Declare that this method throws just about every exception in the book.  Throw all exceptions to UI class like an irritated child in a pram with toys nearby.
	public ArrayList<Landmark> CreateCollection() throws XmlPullParserException, IOException, ExecutionException, InterruptedException, NullPointerException
	{		
		//Initialise collection
		ArrayList<Landmark> landmarks = new ArrayList<>();

		//Set up factory and parser
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(xmlString));

		//Set up the landmark field.
		Landmark landmark = null;

		//Set up the field to read text content from the document.
		String tagContent = "";

		//Set up field for tag name.
		String tagName;

		//Read to the end of the document.
		int eventType = xpp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT)
		{
			//Get tag name tag name.
			tagName = xpp.getName();

			//Found a start tag
			switch (eventType)
			{
				//If the tag is a start tag.
				case XmlPullParser.START_TAG:
				{
					if (tagName.equals("ItemTitle"))
					{
						//Create a new landmark object to be filled with data.
						landmark = new Landmark();
						Log.d("XMLParser.CreateCollec.", "Created a landmark object.");
					}
					break;
				}

				//If the tag is a text tag
				case XmlPullParser.TEXT:
				{
					//Get associated text.
					tagContent = xpp.getText();
					break;
				}

				//If the tag is an end tag.
				case XmlPullParser.END_TAG:
				{
					//These code blocks depend on a landmark object being created in the start tag block.
					//This assertion statement simple allows that assumption.
					assert landmark != null;

					if (tagName.equals("ItemTitle"))
					{
						//Write the title.
						landmark.setTitle(tagContent);
						Log.d("XMLParser.CreateCollec.", "Added a title.");
					}

					if (tagName.equals("ItemText"))
					{
						//Write the Description.
						landmark.setDescriptionText(tagContent);
						Log.d("XMLParser.CreateCollec.", "Added some description.");
					}

					if (tagName.equals("ItemImage"))
					{
						//Write the Image
						URL imageURL = new URL(tagContent);
						Bitmap image = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
						landmark.setImage(image);

						Log.d("XMLParser.CreateCollec.", "Added an image.");
					}

					if (tagName.equals("ItemLat"))
					{
						//Write the Latitude.
						landmark.setLatitude(Double.parseDouble(tagContent));
						Log.d("XMLParser.CreateCollec.", "Added lat.");
					}

					if (tagName.equals("ItemLong"))
					{
						//Write the Longitude.
						landmark.setLongitude(Double.parseDouble(tagContent));
						Log.d("XMLParser.CreateCollec.", "Added long.");

						//As this is the last desired value, we can complete our landmark entry.
						landmarks.add(landmark);
						Log.d("XMLParser.CreateCollec.", "Added the landmark to the collection.\r\nCollection length: " + landmarks.size());
					}
					break;
				}

				default:
					break;
			}

			//get next tag.
			eventType = xpp.next();
		}

		return landmarks;
	}
}
