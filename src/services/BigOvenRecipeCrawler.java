package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import crawingspackage.ARecipeFactory;
import crawingspackage.IRecipeFactory;
import crawingspackage.PMF;
import crawingspackage.models.Recipe;
import crawingspackage.models.WebImage;

import com.google.appengine.api.search.Field;


/**
 * class that is called to actually scrape the data
 * @author Guillaume Boell
 * @version 10th December 2014
 */
public class BigOvenRecipeCrawler implements IRecipeFactory {


	/**
	 * method to get a recipe form an index
	 * @param index  the index were looking for
	 * @return  whether it was successful or not
	 */
	@Override
	public boolean getRecipeFromIndex(int index) {
		Document recipeData = BigOvenService.retrieveRecipeData(index);
		Document recipeImages = BigOvenService.retrieveRecipeImages(index);
				
		//basic recipe data parsing
		try {
		if (recipeData != null && recipeData.getElementsByTagName("RecipeID").getLength() > 0)
		{
//			Long recipeId = new Long(recipeData.getElementsByTagName("RecipeID").item(0).getTextContent());

			String recipeTitle = recipeData.getElementsByTagName("Title").item(0).getTextContent();
			String recipeDescription = recipeData.getElementsByTagName("Description").item(0).getTextContent();
			String recipeUrl = recipeData.getElementsByTagName("WebURL").item(0).getTextContent();

			NodeList ingredients = recipeData.getElementsByTagName("IngredientInfo");
			ArrayList<String> recipeList = new ArrayList<String>();
			for (int i = 0 ; i < ingredients.getLength() ; ++i)
			{
				Node ingredient = ingredients.item(i);
				
				String ingredientName = ingredient.getChildNodes().item(1).getTextContent();
				String ingredientCategory = ingredient.getChildNodes().item(3).getTextContent();
				recipeList.add(ingredientName + " (" + ingredientCategory + ")");
			}
			
			//image retrieval
			ArrayList<Integer> imageIds = new ArrayList<Integer>();
			if (recipeImages != null)
			{
				NodeList imageNodeList = recipeImages.getElementsByTagName("Image");
				PersistenceManager pm = PMF.get().getPersistenceManager();

				for (int i = 0 ; i < imageNodeList.getLength() ; ++i)
				{
					String imgUrl = imageNodeList.item(i).getChildNodes().item(3).getTextContent();
					Integer imgId = new Integer(imageNodeList.item(i).getChildNodes().item(1).getTextContent());
					try {
						WebImage img = AService.getImageAtUrl(imgUrl, imgId);
						imageIds.add(imgId);
						pm.makePersistent(img);
					} catch (IOException e) {
						System.err.println("Image retrieval failed. Image URL used was " + imgUrl);
					}
				}
				pm.close();
			}
			// using com.google.appengine.api.search.Document because importing it conflicts with the xml document class
			com.google.appengine.api.search.Document.Builder finalRecipeBuilder = com.google.appengine.api.search.Document.newBuilder()
					.setId(Integer.toString(index))
					.addField(Field.newBuilder().setName("recipeTitle").setText(recipeTitle))
					.addField(Field.newBuilder().setName("recipeDescription").setText(recipeDescription))
					.addField(Field.newBuilder().setName("recipeUrl").setText(recipeUrl));
			//adding ingredients
			for (String ingredient : recipeList)
			{
				finalRecipeBuilder.addField(Field.newBuilder().setName("recipeIngredientList").setText(ingredient));
			}
			//adding image IDs
			for (Integer imageId : imageIds)
			{
				finalRecipeBuilder.addField(Field.newBuilder().setName("recipeImageList").setText(imageId.toString()));
			}
			com.google.appengine.api.search.Document recipeDocument = finalRecipeBuilder.build();
			ARecipeFactory.IndexDocument("RecipeDocument", recipeDocument);
			return (true);
		}
		} catch (NullPointerException e) {}
		System.err.println("Recipe retrieval failed. Recipe index was " + index);
		return (false);
	}


	/**
	 * return a list of recipes
	 * that match the search term
	 * currently not implemented for this interface
	 * @param searchTerm the search term in question
	 * @return   null as this is not used
	 */
	@Override
	public List<Recipe> getRecipeFromSearch(String searchTerm) {
		return null;
	}

}
