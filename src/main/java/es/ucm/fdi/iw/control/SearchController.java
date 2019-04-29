package es.ucm.fdi.iw.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.RecipeIngredient;
import es.ucm.fdi.iw.model.User;

@Controller
public class SearchController {

	private static final Logger log = LogManager.getLogger(SearchController.class);

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/buscar")
	public String buscar(Model model, HttpSession session) {
		// devuelve las recetas añadidas a fav para habilitar o deshabilitar el botón de
		// favoritos.
		List<Long> favRecipesId = new ArrayList<>();
		User userAux = (User) session.getAttribute("user");

		if (null != userAux) {
			User user = entityManager.find(User.class, userAux.getId()); // esta query es por que el objeto viene en
																			// modo lazy

			if (!user.getFavRecipes().isEmpty()) {
				favRecipesId = user.getFavRecipes().stream().map(recipe -> recipe.getId()).collect(Collectors.toList());
			}
		}

		model.addAttribute("favRecipes", favRecipesId);

		model.addAttribute("recipes",
				entityManager.createNamedQuery("Recipe.AllRecipes", Recipe.class).setMaxResults(6).getResultList());
		model.addAttribute("found", true);

		model.addAttribute("siteName", "Buscador - " + env.getProperty("es.ucm.fdi.site-title-short"));

		return "buscar";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/buscar")
	@Transactional
	public String getRecipesSearchAdvanced(@RequestParam(required = false) String recipeName,
			@RequestParam(required = false) Integer tiempo, @RequestParam(required = false) String ingredientes,
			@RequestParam(required = false) String[] difficulty, @RequestParam(required = false) String[] cuisine,
			@RequestParam(required = false) String[] tag, Model model, HttpSession session) {

		List<String> ingredients = ingredientes != "" ? Arrays.asList(ingredientes.split(" ")) : null;
		Boolean found = true;

		List<Recipe> allRecipes = entityManager.createNamedQuery("Recipe.AllRecipes", Recipe.class).getResultList();
		Predicate<Recipe> recipePredicate = getPredicate(recipeName, tiempo, ingredients, difficulty, cuisine, tag);
		List<Recipe> filtredRecipes = allRecipes.stream().filter(recipePredicate).collect(Collectors.toList());

		if (filtredRecipes.isEmpty()) {
			filtredRecipes.addAll(entityManager.createNamedQuery("Recipe.AllRecipes", Recipe.class).setMaxResults(6).getResultList());
			found = false;
		}

		model.addAttribute("recipes", filtredRecipes);
		model.addAttribute("found", found);
		model.addAttribute("difficulty", difficulty);
		model.addAttribute("cuisine", cuisine);
		model.addAttribute("ingredients", ingredients);
		model.addAttribute("tiempo", tiempo);
		model.addAttribute("tag", tag);
		model.addAttribute("recipeName", recipeName);

		model.addAttribute("siteName", "Buscador - " + env.getProperty("es.ucm.fdi.site-title-short"));

		// devuelve las recetas añadidas a fav para habilitar o deshabilitar el botón de
		// favoritos.
		List<Long> favRecipesId = new ArrayList<>();
		User userAux = (User) session.getAttribute("user");

		if (null != userAux) {
			User user = entityManager.find(User.class, userAux.getId()); // esta query es por que el objeto viene en
																			// modo lazy

			if (!user.getFavRecipes().isEmpty()) {
				favRecipesId = user.getFavRecipes().stream().map(recipe -> recipe.getId()).collect(Collectors.toList());
			}
		}

		model.addAttribute("favRecipes", favRecipesId);

		return "buscar";
	}

	private Predicate<Recipe> getPredicate(String recipeName, Integer tiempo, List<String> ingredients,
			String[] difficulty, String[] cuisine, String[] tag) {

		// convertir a set los arrays de String para poder hacer contains
		Set<String> difficultySet = (difficulty != null) ? new HashSet<>(Arrays.asList(difficulty)) : null;
		Set<String> cuisineSet = (cuisine != null) ? new HashSet<>(Arrays.asList(cuisine)) : null;
		Set<String> tagList = (tag != null) ? new HashSet<>(Arrays.asList(tag)) : null;

		Predicate<Recipe> predicateByRecipeName = r -> true;
		Predicate<Recipe> predicateByTiempo = r -> true;
		Predicate<Recipe> predicateByIngredientes = r -> true;
		Predicate<Recipe> predicateByDifficulty = r -> true;
		Predicate<Recipe> predicateByCuisine = r -> true;
		Predicate<Recipe> predicateByTag = r -> true;

		// predicate by recipeName
		if (recipeName != null && !recipeName.isEmpty()) {
			String recipeNameLower = recipeName.toLowerCase();
			predicateByRecipeName = r -> r.getName().toLowerCase().contains(recipeNameLower);
		}

		// predicate by time
		if (tiempo != null) {
			predicateByTiempo = r -> r.durationInt() < tiempo;
		}

		// predicate by ingredients
		if (ingredients != null && !ingredients.isEmpty()) {
			predicateByIngredientes = new Predicate<Recipe>() {

				@Override
				public boolean test(Recipe recipe) {
					Set<RecipeIngredient> recipeIngredientes = recipe.getRecipeIngredients();

					for (RecipeIngredient recipeIngrediente : recipeIngredientes) {

						for (String ingrediente : ingredients) {
							if (recipeIngrediente.getIngredient().getName().contains(ingrediente))
								return true;
						}

					}
					return false;
				}

			};
		}

		// predicate by difficulty
		if (difficultySet != null && !difficultySet.isEmpty()) {
			predicateByDifficulty = r -> difficultySet.contains(r.getDifficulty());
		}

		// predicate by cuisine
		if (cuisineSet != null && !cuisineSet.isEmpty()) {
			predicateByCuisine = r -> cuisineSet.contains(r.getCuisine());
		}
		// predicate by tag
		if (tagList != null && !tagList.isEmpty()) {
			predicateByTag = r -> r.tagsNames().containsAll(tagList);
		}
		return predicateByTiempo.and(predicateByIngredientes).and(predicateByDifficulty).and(predicateByCuisine)
				.and(predicateByTag).and(predicateByRecipeName);
	}

}
