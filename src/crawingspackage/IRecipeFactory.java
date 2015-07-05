package crawingspackage;
 
import java.util.List;
 
import crawingspackage.models.Recipe;
 
/**
 * Class that allows to create recipes from datastore
 * @author Guillume Boell
 * @version 8th December 2014
 */
public interface IRecipeFactory {
 
        /**
         * mehtod to get a recipe form an index
         * @param index  the index were looking for
         * @return  whether it was sucsessful or not
         */
        boolean getRecipeFromIndex(int index);
 
        /**
         * returna  list of recipes
         * that match the search term
         * currently not implmeneted for this interface
         * @param searchTerm the search term in question
         * @return   null as this is not used
         */
        List<Recipe> getRecipeFromSearch(String searchTerm);
}