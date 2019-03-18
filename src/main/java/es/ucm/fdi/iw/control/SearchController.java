package es.ucm.fdi.iw.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
		model.addAttribute("recipes",
				entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(6).getResultList());
		model.addAttribute("found", true);

		return "buscar";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/buscar")
	public String getRecipesSearchAdvanced(@RequestParam(required = false) Integer tiempo,
			@RequestParam(required = false) String ingredientes, @RequestParam(required = false) String[] difficulty,
			@RequestParam(required = false) String[] cuisine, @RequestParam(required = false) String[] tag,
			Model model) {

		List<String> ingredients = ingredientes != "" ? Arrays.asList(ingredientes.split(" ")) : null;

		List<Recipe> recipes = getRecipes(tiempo, ingredients, difficulty, cuisine, tag);
		// Because we want to inform about the non matching query
		boolean found = true;

		if (recipes.size() == 0) {
			recipes.addAll(entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(6).getResultList());
			found = false;
		}

		model.addAttribute("recipes", recipes);
		model.addAttribute("found", found);
		model.addAttribute("difficulty", difficulty);
		model.addAttribute("cuisine", cuisine);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("tag", tag);

		return "buscar";
	}

	@SuppressWarnings("unchecked")
	public List<Recipe> getRecipes(Integer tiempo, List<String> ingredients, String[] difficulty, String[] cuisine,
			String[] tag) {
		Set<Recipe> recipes = new TreeSet<Recipe>();

		if (ingredients == null) {

			if (difficulty != null && cuisine != null) {

				recipes = convertListToSet(entityManager.createNamedQuery("Recipe.ByDifficultyAndCuisine")
						.setParameter("difficulty", Arrays.asList(difficulty))
						.setParameter("cuisine", Arrays.asList(cuisine)).getResultList());

			} else if (cuisine != null) {

				recipes = convertListToSet(entityManager.createNamedQuery("Recipe.ByCuisine")
						.setParameter("cuisine", Arrays.asList(cuisine)).getResultList());

			} else if (difficulty != null) {

				recipes = convertListToSet(entityManager.createNamedQuery("Recipe.ByDifficulty")
						.setParameter("difficulty", Arrays.asList(difficulty)).getResultList());

			}

		} else {
			for (String ingredientName : ingredients) {

				if (difficulty != null && cuisine != null) {

					recipes.addAll(
							entityManager.createNamedQuery("RecipeIngredient.Filter.IngredientAndDifficultyAndCuisine")
									.setParameter("difficulty", Arrays.asList(difficulty))
									.setParameter("cuisine", Arrays.asList(cuisine))
									.setParameter("name", "%" + ingredientName + "%").getResultList());

				} else if (cuisine != null) {

					recipes.addAll(entityManager.createNamedQuery("RecipeIngredient.Filter.IngredientAndCuisine")
							.setParameter("cuisine", Arrays.asList(cuisine))
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

		if (recipes.size() != 0 && recipes.size() != 0 && tag != null) {

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

	// Generic function to convert list to set
	public static <T> Set<T> convertListToSet(List<T> list) {
		// create a set from the List
		return list.stream().collect(Collectors.toSet());
	}

}
