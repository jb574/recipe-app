package services;

import crawingspackage.ARecipeFactory;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchException;

/**
 * class that handles the search service for the application
 * @author: Jack Davey & Guillaume Boell
 * @version: 29th November 2014
 */
public class DocumentSearchService
{
	/**
	 * private constructor for this class
	 */
    private DocumentSearchService()
    {

    }

    /**
     * method to search with one parameter
     * @param fieldName  the name of the field to search in
     * @param argument   the argument to look for
     * @return the list of recipes that were found, or null otherwise
     */
    public static Results<ScoredDocument> simpleSearch(String searchTerm)
    {
		return (executeQuery(searchTerm));
    }

    /**
     * method to run the built query
     * @param newQuery  the query to run
     * @return the list of recipes to return to the user
     */
    private static Results<ScoredDocument> executeQuery(String query)
    {
		com.google.appengine.api.search.Index index = ARecipeFactory.getRecipeDocumentIndex();

		try {
		    Results<ScoredDocument> results = index.search(query);
		    // Iterate over the documents in the results
		    for (ScoredDocument document : results) {
		        System.out.println("Resultat :" + document.toString());
		    }
		    return (results);
		} catch (SearchException e) {
			//if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {}
	        System.err.println("Error: " + e.getLocalizedMessage());
		}
		return (null);
    }

}
