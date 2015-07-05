package services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

import crawingspackage.models.WebImage;


/**
 * class that represents a service that we can use
 * to connect to a cooking web service
 * @author Guillaume Boell
 * @version 10th December 2015
 */
public abstract class AService {


    /**
     * method to return an image at a specified url
     * @param url the url in question
     * @param imageId the unique identification number of the image
     * @return    the image in question
     * @throws MalformedURLException  if the url is not properly formed
     * @throws IOException   if this operation fails for whateve reason
     */
	static public WebImage getImageAtUrl(String url, Integer imageId) throws MalformedURLException, IOException
	{
        URLFetchService fetchService = URLFetchServiceFactory.getURLFetchService();
        WebImage image = new WebImage();

        // Fetch the image at the location given by the url query string parameter
        HTTPResponse fetchResponse = fetchService.fetch(new URL(url));

        for (HTTPHeader header : fetchResponse.getHeaders()) {
            // For each request header, check whether the name equals
            // "Content-Type"; if so, store the value of this header
            // in a member variable
            if (header.getName().equalsIgnoreCase("content-type")) {
                image.setImageContentType(header.getValue());
                break;
            }
        }
        Blob imageData = new Blob(fetchResponse.getContent());
        image.setImageId(imageId);
        image.setImageData(imageData);
        image.setImageUrl(url);
        return (image.getImageBytes() == null ? null : image);
	}
}
