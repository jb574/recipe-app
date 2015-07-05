package crawingspackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.DocumentSearchService;

import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.gson.Gson;

import crawingspackage.models.Recipe;

/**
 * A servlet for communicating with our data store
 * 
 * @author Guillaume Boell
 *
 */
@SuppressWarnings("serial")
public class ApiServlet extends HttpServlet {
	/**
	 * Get recipes from the the datastore
	 */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String searchRequest = "";
		boolean needsImage = false;

		@SuppressWarnings("unchecked")
		Iterator<Entry<String, String>> it = req.getParameterMap().entrySet().iterator();

		if (req.getParameter("needsImage") != null)
			needsImage = new Boolean(req.getParameter("needsImage"));
		while (it.hasNext())
		{
			Map.Entry<String, String> pairs = it.next();
			searchRequest += "~" + pairs.getKey();
			if (it.hasNext())
				searchRequest += " OR ";
		}
		System.out.println("Request : " + searchRequest);
		Results<ScoredDocument> results = DocumentSearchService.simpleSearch(searchRequest);
		ArrayList<Recipe> fieldObject = new ArrayList<Recipe>();
		for (ScoredDocument doc : results)
		{
			Recipe recipe = ARecipeFactory.getRecipeFromDocument(doc, needsImage);
			if (recipe != null)
				fieldObject.add(recipe);
		}
		resp.getWriter().print(getJSONFromObject(fieldObject));
	}

	/**
	 * Use gson library to construct a JSON object we can work with from
	 * incoming Object
	 * 
	 * @param o
	 *            generic Object
	 * @return returns parameter in JSON format
	 */
	private String getJSONFromObject(Object o) {
		Gson gson = new Gson();
		return (gson.toJson(o));
	}
}
