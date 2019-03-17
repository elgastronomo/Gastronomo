package es.ucm.fdi.iw.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.model.Recipe;

@Controller
public class SearchController {

	private static final Logger log = LogManager.getLogger(SearchController.class);

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/buscar")
	public String buscar(Model model) {

		return "buscar";
	}

	@PostMapping("/buscar")
	public String getRecipesSearchAdvanced(@RequestParam(required = false) Integer tiempo, @RequestParam(required = false) String ingredientes,
			@RequestParam(required = false) String[] difficulty, @RequestParam(required = false) String[] cuissine,
			@RequestParam(required = false) String[] tag, Model model) {

		List<String> ingredients = Arrays.asList(ingredientes.split(" "));
		
		model.addAttribute("recipes", getRecipes(tiempo, ingredients, difficulty, cuissine, tag));
		model.addAttribute("difficulty", difficulty);
		model.addAttribute("cuissine", cuissine);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("tag", tag);

		return "buscar";
	}

	@SuppressWarnings("unchecked")
	public List<Recipe> getRecipes(Integer tiempo, List<String> ingredients, String[] difficulty, String[] cuissine, String[] tag) {
		Set<Recipe> recipes = new TreeSet<Recipe>();

		if (ingredients == null) {
			
			if (difficulty != null && cuissine != null) {
				
				recipes = (Set<Recipe>) entityManager.createNamedQuery("Recipe.ByDifficultyAndCuissine")
						.setParameter("difficulty", Arrays.asList(difficulty))
						.setParameter("cuissine", Arrays.asList(cuissine)).getResultList();
				
			} else if (cuissine != null) {
				
				recipes = (Set<Recipe>) entityManager.createNamedQuery("Recipe.ByCuissine")
						.setParameter("cuissine", Arrays.asList(cuissine)).getResultList();
				
			} else if (difficulty != null) {
				
				recipes = (Set<Recipe>) entityManager.createNamedQuery("Recipe.ByDifficulty")
						.setParameter("difficulty", Arrays.asList(difficulty)).getResultList();
				
			} else {
				
				recipes = (Set<Recipe>) entityManager.createNamedQuery("Recipe.AllRecipes").getResultList();
				
			}
			
		
		} else {
			for (String ingredientName : ingredients) {
				
				if (difficulty != null && cuissine != null) {
					
					recipes.addAll(entityManager.createNamedQuery("RecipeIngredient.Filter.IngredientAndDifficultyAndCuisine")
							.setParameter("difficulty", Arrays.asList(difficulty))
							.setParameter("cuisine", Arrays.asList(cuissine))
							.setParameter("name", "%" + ingredientName + "%").getResultList());
					
				} else if (cuissine != null) {
					
					recipes.addAll(entityManager.createNamedQuery("RecipeIngredient.Filter.IngredientAndCuisine")
							.setParameter("cuisine", Arrays.asList(cuissine))
							.setParameter("name", "%" + ingredientName + "%").getResultList());
					
				} else if (difficulty != null) {
					
					recipes.addAll(entityManager.createNamedQuery("RecipeIngredient.Filter.IngredientAndDifficulty")
							.setParameter("difficulty", Arrays.asList(difficulty))
							.setParameter("name", "%" + ingredientName + "%").getResultList());
					
				} else {
					
					recipes.addAll(entityManager.createNamedQuery("RecipeIngredient.Filter.Ingredient")
							.setParameter("name", "%" + ingredientName + "%").getResultList());				
					
				}				
				
			}
		}
		
		if(recipes.size() == 0) {
			recipes = (Set<Recipe>) entityManager.createNamedQuery("Recipe.AllRecipes").getResultList();
		}
		
		if (tag != null) {
			return getFilterRecipesTags(new ArrayList<Recipe>(recipes), Arrays.asList(tag));
		}
		
		return new ArrayList<Recipe>(recipes);
	}

	public List<Recipe> getFilterRecipesTags(List<Recipe> recipes, List<String> tags) {
		List<Recipe> recipesWithSpecificTags = new ArrayList<Recipe>();

		for (Recipe recipe : recipes) {
			if (recipe.tagsNames().containsAll(tags))
				recipesWithSpecificTags.add(recipe);

		}
		return recipesWithSpecificTags;
	}

}
