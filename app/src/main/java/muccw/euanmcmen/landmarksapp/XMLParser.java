package muccw.euanmcmen.landmarksapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

//This class handles the interation with the XML Feed and post-parsing.
public class XMLParser 
{
	//Declare that this method throws just about every exception in the book.  Throw all exceptions to main activity like they were pram toys to an irritated child.
	public static ArrayList<Landmark> createCollection(String urlString) throws XmlPullParserException, IOException, ExecutionException, InterruptedException, NullPointerException
	{
		//Initialise collections
		ArrayList<Landmark> landmarks = new ArrayList<>();

		//Read the RSS feed into a string.
		String XMLString = readRSSFeed(urlString);

		//Set up factory and parser
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(new StringReader(XMLString));

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

	//Declare that this throws the IO exception, so it can be handled in the calling class.
	private static String readRSSFeed(String urlString) throws IOException
	{
		String result = "";
		InputStream inStream;
		int response;

		URL url = new URL(urlString);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setInstanceFollowRedirects(true);
		httpCon.setRequestMethod("GET");
		httpCon.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		httpCon.connect();
		response = httpCon.getResponseCode();
		if (response == HttpURLConnection.HTTP_OK)
		{
			// Connection is Ok so open a reader
			inStream = httpCon.getInputStream();
			InputStreamReader inStreamReader= new InputStreamReader(inStream);
			BufferedReader inReader= new BufferedReader(inStreamReader);

			// Read in the data from the XML stream
			String line;
			while (( (line = inReader.readLine())) != null)
			{
				//The program seems to treat the entire xml feed as a single line.
				result = result + " " + line;
			}
		}
		httpCon.disconnect();

		//Perform some operations on the result string.
		//A lot of these replacements are attributed to the "plaintext" XML that we read from the datasouces' description tag.

		//Open tag replacement.
		result = result.replace("&amp;lt;", "<");

		//Close tag replacement.
		result = result.replace("&amp;gt;", ">");

		//Delete the lines which trip up the xml reader.
		result = result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		result = result.replace("!-- SC_OFF --><div class=\"md\"", "");

		//Clean up the image link.
		result = result.replace("~", "");

		//Clean up the apostrophe
		result = result.replace("&amp;#39;", "\'");

		// Return the result string.
		return result;
	}
}
