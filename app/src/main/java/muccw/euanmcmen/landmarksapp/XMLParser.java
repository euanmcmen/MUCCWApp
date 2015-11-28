package muccw.euanmcmen.landmarksapp;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/*
 * Euan McMenemin
 * S1125095
 * Mobile Ubiquitous Computing Coursework
 */

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

	//Declare that this method throws just about every exception in the book.  Throw all exceptions to main activity like they were pram toys to an irritated child.
	public ArrayList<Landmark> createCollection() throws XmlPullParserException, IOException, ExecutionException, InterruptedException, NullPointerException
	{
		//Initialise collections
		ArrayList<Landmark> landmarks = new ArrayList<>();

		//Set up factory and parser
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(xmlString));

		//Set up the fields to parse to.
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
					}

					if (tagName.equals("ItemText"))
					{
						//Write the Description.
						landmark.setDescriptionText(tagContent);
					}

					if (tagName.equals("ItemImage"))
					{
						//Write the image location.  We'll get the image from this location later.
						landmark.setImageUrl(tagContent);
					}

					if (tagName.equals("ItemLat"))
					{
						//Write the Latitude.
						landmark.setLatitude(Double.parseDouble(tagContent));
					}

					if (tagName.equals("ItemLong"))
					{
						//Write the Longitude.
						landmark.setLongitude(Double.parseDouble(tagContent));

						//As this is the last desired value, we can complete our landmark entry.
						landmarks.add(landmark);
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
