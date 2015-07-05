package services;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * class to handle accesses to the BigOven web service
 * @authors Jack Davey & Guillaume Boëll
 * @version 23rd November 2014
 */
public class BigOvenService extends AService
{
    /**
     * method to search for recipes that contain a
     * search term
     * @param searchTerm the term to search for
     * @return  a list containing all the strings in the arraylist
     */
      public static Document searchRecipes(String searchTerm)
      {
  		ArrayList<String> path = new ArrayList<String>();
		HashMap<String, String> args= new HashMap<String, String>();
		
		path.add("recipes");
		args.put("title_kw", searchTerm);
		args.put("pg", "1");
		args.put("rpp", "20");
		return (makeApiCall(path, args));
      }

    /**
     * a method to access the data for a recipe
     * based on an id
     * @param id    the id of the recipe
     * @return  the list of all the strings in the returned xml file.
     */
    public static Document retrieveRecipeData(Integer id)
    {
    	ArrayList<String> path = new ArrayList<String>();

    	path.add("recipe");
    	path.add(id.toString());
    	return (makeApiCall(path, null));
    }

    /**
     * a method to access the images of a recipe
     * based on an id
     * @param id    the id of the recipe
     * @return  the list of all the strings in the returned xml file.
     */
    public static Document retrieveRecipeImages(Integer id)
    {
    	ArrayList<String> path = new ArrayList<String>();
		HashMap<String, String> args = new HashMap<String, String>();

    	path.add("images");
    	args.put("rid", id.toString());
    	return (makeApiCall(path, args));
    }

    // example API url calls :
    //  http://api.bigoven.com/recipe/562077?api_key=dvxw8vy03Hql3iJwHBtS4p2K2afmgz2f
    //	http://api.bigoven.com/images?rid=562077&api_key=dvxw8vy03Hql3iJwHBtS4p2K2afmgz2f
    /**
     * a generic method to build and call the API to avoid code duplication
     * @param method    the method API
     * @param path		a list describing of the url path
     * @param arguments	a list describing the URL arguments after the '?' symbol
     * @return the list of all the strings in the returned xml file.
     */
    private static Document makeApiCall(List<String> path, HashMap<String, String> arguments)
    {
        String apiKey = "dvxw8vy03Hql3iJwHBtS4p2K2afmgz2f";
    	String baseUrl = "http://api.bigoven.com/";

        try
        {
        	String apiUrl = baseUrl;

    		// building url path
        	if (path != null)
        		for (String p : path)
        			apiUrl += p + "/";
        	// removes last '/'
        	apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
    		apiUrl += "?";

    		// add the URL parameters after the '?'
    		if (arguments == null)
    			arguments = new HashMap<String, String>();
    		arguments.put("api_key", apiKey);
        	Iterator<Entry<String, String>> it = arguments.entrySet().iterator();
    		while (it.hasNext()) {
                Map.Entry<String, String> pairs = it.next();
                apiUrl += pairs.getKey() + "=" + pairs.getValue();
                it.remove(); // avoids a ConcurrentModificationException
                if (it.hasNext())
                	apiUrl += "&";
            }
        	URL url = new URL(apiUrl);
            System.out.println("Launching request on : " + apiUrl);
            return (getXMLDoc(url));
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        return (null);
    }

    /**
     * generates an XML DOM Document from URL result
     * @param method    the method API
     * @param path		a list describing of the url path
     * @param arguments	a list describing the URL arguments after the '?' symbol
     * @return the list of all the strings in the returned xml file.
     */
    private static Document getXMLDoc(URL url) throws IOException
    {
        try
        {
        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
            return (db.parse(url.openStream()));
        }
        catch (IOException | SAXException | ParserConfigurationException error)
        {
            error.printStackTrace();
            return (null);
        }
    }
}

