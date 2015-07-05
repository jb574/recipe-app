package crawingspackage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;

import crawingspackage.models.Recipe;
import crawingspackage.models.WebImage;

/**
 * Class that allows to create recipes from datastore
 * 
 * @author Guillaume Boell
 *
 */
public abstract class ARecipeFactory implements IRecipeFactory {

	/**
	 * Generic function to store document in our datastore
	 * 
	 * @param indexName
	 * @param document
	 */
	static public void IndexDocument(String indexName, Document document) {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
		com.google.appengine.api.search.Index index = SearchServiceFactory
				.getSearchService().getIndex(indexSpec);

		try {
			index.put(document);
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult()
					.getCode())) {
				System.err.println("Error during document indexing"
						+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Return an image from searching with the index from our datastore
	 * 
	 * @param index
	 * @return
	 */
	static public WebImage getImageFromIndex(int index) {
		try {
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("WebImage", index);
			Entity imageResult = datastore.get(key);

			if (imageResult != null) {
				WebImage img = new WebImage();

				img.setImageUrl((String) imageResult.getProperty("imageUrl"));
				img.setImageContentType((String) imageResult
						.getProperty("imageContentType"));
				img.setImageData((Blob) imageResult.getProperty("imageData"));
				img.setImageId(index);
				return (img);
			}
		} catch (Exception error) {
			System.err.println("Error occured on WebImage retrieval : "
					+ error.getLocalizedMessage());
		}
		return (null);
	}

	static public Index getRecipeDocumentIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("RecipeDocument")
				.build();
		Index index = SearchServiceFactory.getSearchService().getIndex(
				indexSpec);
		return (index);
	}

	/**
	 * Returns document storage index for the RecipeDocument type
	 * 
	 * @return index
	 */
	static public Recipe getRecipeFromDocument(Document doc, boolean needsImage) {
		Recipe recipe = new Recipe(Long.parseLong(doc.getId()));
		String recipeTitle = doc.getFields("recipeTitle").iterator().next()
				.getText();
		String recipeDescription = doc.getFields("recipeDescription")
				.iterator().next().getText();
		String recipeUrl = doc.getFields("recipeUrl").iterator().next()
				.getText();
		List<String> recipeImageUrlList = new ArrayList<String>();

		Iterable<Field> imageField = doc.getFields("recipeImageList");
		if (imageField != null) {
			Iterator<Field> images = imageField.iterator();
			while (images.hasNext()) {
				String imgId = images.next().getText();

				recipeImageUrlList.add("getImg?id=" + imgId);
			}
		}
		if (needsImage == false || recipeImageUrlList.size() > 0) {
			if (recipeImageUrlList.isEmpty())
				recipeImageUrlList.add("img/noimage.jpg");
			recipe.setTitle(recipeTitle);
			recipe.setInstructions(recipeDescription);
			recipe.setRecipeUrl(recipeUrl);
			recipe.setRecipeImageList(recipeImageUrlList);
			return (recipe);
		}
		return (null);
	}
}