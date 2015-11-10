package muccw.euanmcmen.landmarksapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//Euan McMenemin
//S1125095

//This class handles the interaction with the online feed, and populates the result string in the activity class.
public class HTTPFeedReader 
{
	//Declare that this throws the IO exception, so it can be handled in the calling class.
	public static String ReadXMLFeed(String urlString) throws IOException
	{
	 	String result = "";
    	InputStream inStream = null;
    	int response = -1;
    	
    	//Try was moved to encapsulate the url initialisation and throw block. 
    	//Also the HTTPUrlConnection line is simpler.
    	try
    	{	    	
    		// Open connection
			URL url = new URL(urlString);   		
    		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
    		httpCon.setInstanceFollowRedirects(true);
    		httpCon.setRequestMethod("GET");
    		httpCon.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
    		httpCon.connect();   		
    		response = httpCon.getResponseCode();

    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			inStream = httpCon.getInputStream();
    			InputStreamReader inStreamReader= new InputStreamReader(inStream);
    			BufferedReader inReader= new BufferedReader(inStreamReader);
    			
    			// Read in the data from the XML stream
    			String line = new String();
    			while (( (line = inReader.readLine())) != null)
				{
					//The program seems to treat the entire xml feed as a single line.
					result = result + "\n" + line;
    			}
    		}
    		
    		//Close the connection after the read.
    		httpCon.disconnect();
    	}
    	catch (IOException ioe)
    	{
    		throw ioe;    		
    	}

		//Perform some operations on the result string.
		//A lot of these replacements are attributed to the "plaintext" XML that we read from
		//the datasouces' description tag.

		//Open tag replacement.
		result = result.replace("&amp;lt;", "<");

		//Close tag replacement.
		result = result.replace("&amp;gt;", ">");

		//Delete the lines which trip up the xml reader.
		result = result.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
		result = result.replace("!-- SC_OFF --><div class=\"md\"", "");

		//Clean up the image link.
		result = result.replace("~", "");

		// Return the result string.
    	return result;
	}
}