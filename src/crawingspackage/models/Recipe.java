package crawingspackage.models;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Model we use to store textual data from recipes
 * @author Guillaume Boëll
 * @version 23rd November 2014
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Recipe {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private long recipeId;

	@Persistent
    private String title;

	@Persistent
    private java.util.List<String> ingredients;

    @Persistent
    private String recipeUrl;

	@Persistent
    private String instructions;
	
	private java.util.List<String> recipeImageList;

    public String getRecipeUrl() {
		return recipeUrl;
	}

	public void setRecipeUrl(String recipeUrl) {
		this.recipeUrl = recipeUrl;
	}

    public Recipe(Long recipeId)
    {
    	this.setRecipeId(recipeId);
    }
    
    public Long getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Long recipeId) {
		this.recipeId = recipeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public java.util.List<String> getIngredients() {
		return ingredients;
	}

	public void setIngredients(java.util.List<String> ingredients) {
		this.ingredients = ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	@Override
	public String toString() {
		return this.title + " : " + this.ingredients + "\nIngredients : " + this.getInstructions().length();
	}

	public java.util.List<String> getRecipeImageList() {
		return recipeImageList;
	}

	public void setRecipeImageList(java.util.List<String> recipeImageList) {
		this.recipeImageList = recipeImageList;
	}

}
