package es.ucm.fdi.iw.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.cache.AlwaysValidCacheEntryValidity;

import es.ucm.fdi.iw.model.Ingredient;
import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.RecipeIngredient;
import es.ucm.fdi.iw.model.Tag;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.UserIngredient;

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
		
		model.addAttribute("siteName", "Buscador - " + env.getProperty("es.ucm.fdi.site-title-short"));
		
	

		return "buscar";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/buscar")
	@Transactional
	public String getRecipesSearchAdvanced(@RequestParam(required = false) Integer tiempo,
			@RequestParam(required = false) String ingredientes, @RequestParam(required = false) String[] difficulty,
			@RequestParam(required = false) String[] cuisine, @RequestParam(required = false) String[] tag,
			Model model, HttpSession session) {

		List<String> ingredients = ingredientes != "" ? Arrays.asList(ingredientes.split(" ")) : null;
		Boolean found = true;
		
		List<Recipe> allRecipes = entityManager.createNamedQuery("Recipe.AllRecipes").getResultList();
		Predicate<Recipe> recipePredicate = getPredicate(tiempo,ingredients,difficulty,cuisine,tag); 
		List<Recipe> filtredRecipes = allRecipes.stream().filter(recipePredicate).collect(Collectors.toList());
			
	
		if (filtredRecipes.isEmpty()) {
			filtredRecipes.addAll(entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(6).getResultList());
			found = false;
		}
		

		model.addAttribute("recipes", filtredRecipes);
		model.addAttribute("found", found);
		model.addAttribute("difficulty", difficulty);
		model.addAttribute("cuisine", cuisine);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("tiempo", tiempo);
		model.addAttribute("tag", tag);
		
		model.addAttribute("siteName", "Buscador - " + env.getProperty("es.ucm.fdi.site-title-short"));
		
		// devuelve las recetas añadidas a fav para habilitar o deshabilitar el botton ese del corazon.
		List<Long> favRecipesId = new ArrayList<>();
		User userAux = (User) session.getAttribute("user");	
		
		if(null != userAux) {
			User user = entityManager.find(User.class, userAux.getId());// esta query es por que el objeto viene en modo lazy
			if(!user.getFavRecipes().isEmpty()) {
				favRecipesId = user .getFavRecipes().stream().map(recipe -> recipe.getId()).collect(Collectors.toList());
			}
		}
		
		model.addAttribute("favRecipes", favRecipesId);

		return "buscar";
	}

	private Predicate<Recipe> getPredicate(Integer tiempo, List<String> ingredients, String[] difficulty, String[] cuisine,String[] tag) {
		
		//convertir a set los arrays de String para poder hacer contains
		Set<String> difficultySet 	= (difficulty != null) 	?  new HashSet<>(Arrays.asList(difficulty)) 	: null;
		Set<String> cuisineSet 		= (cuisine!= null) 		?  new HashSet<>(Arrays.asList(cuisine)) 	: null;
		Set<String> tagList 		= (tag != null) 		?  new HashSet<>(Arrays.asList(tag)) : null;
	
		Predicate<Recipe> predicateByTiempo 		= r	->  true;
		Predicate<Recipe> predicateByIngredientes 	= r ->  true; 
		Predicate<Recipe> predicateByDifficulty		= r ->  true;
		Predicate<Recipe> predicateByCuisine 		= r ->  true;
		Predicate<Recipe> predicateByTag			= r	->  true;
		
		//predicate by time
		if(tiempo != null) {
			predicateByTiempo = r-> r.durationInt() < tiempo;
		}
		
		//predicate by ingredients
		if(ingredients != null && !ingredients.isEmpty()) {
			predicateByIngredientes =  new Predicate<Recipe>() {

				@Override
				public boolean test(Recipe recipe) {
					Set<RecipeIngredient> recipeIngredientes = recipe.getRecipeIngredients();
					
					for(RecipeIngredient  recipeIngrediente : recipeIngredientes) {
						
						for(String ingrediente : ingredients) {
							if(recipeIngrediente.getIngredient().getName().contains(ingrediente)) return true;
						}
						
					}
					return false;
				}
			
			};
		}
		
		//predicate by difficulty
		if(difficultySet != null && !difficultySet.isEmpty()) {
			predicateByDifficulty = r -> difficultySet.contains(r.getDifficulty());
		}
		
		//predicate by cuisine
		if(cuisineSet!= null && !cuisineSet.isEmpty()) {
			predicateByCuisine= r -> cuisineSet.contains(r.getCuisine());
		}				
		//predicate by tag
		if(tagList!= null && !tagList.isEmpty()) {
			predicateByTag = r -> r.tagsNames().containsAll(tagList);
		}		
		return predicateByTiempo.and(predicateByIngredientes).and(predicateByDifficulty).and(predicateByCuisine).and(predicateByTag);
	}
/*
	@SuppressWarnings("unchecked")
	public List<Recipe> getRecipes(Integer tiempo, List<String> ingredients, String[] difficulty, String[] cuisine,
			String[] tag) {
		Set<Recipe> recipes = new TreeSet<Recipe>();
		
		boolean filterYet = false;

		if (ingredients == null) {

			if (difficulty != null && cuisine != null) {

				recipes = convertListToSet(entityManager.createNamedQuery("Recipe.ByDifficultyAndCuisine")
								.setParameter("difficulty", Arrays.asList(difficulty))
								.setParameter("cuisine", Arrays.asList(cuisine)).getResultList());
				filterYet = true;
			} else if (cuisine != null) {

				recipes = convertListToSet(entityManager.createNamedQuery("Recipe.ByCuisine")
								.setParameter("cuisine", Arrays.asList(cuisine)).getResultList());
				filterYet = true;
			} else if (difficulty != null) {

				recipes = convertListToSet(entityManager.createNamedQuery("Recipe.ByDifficulty")
								.setParameter("difficulty", Arrays.asList(difficulty)).getResultList());
				filterYet = true;
			}

		} else {
			filterYet = true;
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
		
		if ((recipes.size() != 0 || !filterYet) && (tiempo != null || tag != null)) {
			
			if(!filterYet) {
				recipes.addAll(entityManager.createNamedQuery("Recipe.AllRecipes").getResultList());
			}
			
			if(tag != null && tiempo != null) {
				return getFilterRecipesExtra(new ArrayList<Recipe>(recipes), Arrays.asList(tag), tiempo);
			}
			else {
				return getFilterRecipesExtra(new ArrayList<Recipe>(recipes), null, tiempo);
			}
			

		}

		return new ArrayList<Recipe>(recipes);
	}

	public List<Recipe> getFilterRecipesExtra(List<Recipe> recipes, List<String> tags, Integer tiempo) {
		List<Recipe> recipesWithSpecificTagsAndTime = new ArrayList<Recipe>();

		for (Recipe recipe : recipes) {
			
			if (tags != null && tiempo != null) {
				
				if(recipe.tagsNames().containsAll(tags) && recipe.durationInt() <= tiempo) {
					
					recipesWithSpecificTagsAndTime.add(recipe);
					
				}
			}
			else if(tiempo != null && recipe.durationInt() <= tiempo) {
				
				recipesWithSpecificTagsAndTime.add(recipe);
				
			}
			else if(tags != null && recipe.tagsNames().containsAll(tags)) {
				
				recipesWithSpecificTagsAndTime.add(recipe);
				
			}	

		}
		return recipesWithSpecificTagsAndTime;
	}
	
	// Generic function to convert list to set
	public static <T> Set<T> convertListToSet(List<T> list) {
		// create a set from the List
		return list.stream().collect(Collectors.toSet());
	}
*/
}
