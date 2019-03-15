package es.ucm.fdi.iw.control;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.RecipeIngredient;

@Controller
@RequestMapping("receta")
public class RecipeController {

	private static final Logger log = LogManager.getLogger(RecipeController.class);

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/{id}")
	public String getRecipe(@PathVariable long id, Model model, HttpSession session) {

		Recipe recipe = entityManager.find(Recipe.class, id);

		if (recipe != null) {
			// Because it's easier to split the ingredient list in two lists here than
			// inside the template
			List<RecipeIngredient> firstIngredients = new ArrayList<>();
			List<RecipeIngredient> secondIngredients = new ArrayList<>();

			int size = recipe.getRecipeIngredients().size();
			int idx = 1;
			for (RecipeIngredient i : recipe.getRecipeIngredients()) {
				if (idx <= size / 2) {
					firstIngredients.add(i);
				} else {
					secondIngredients.add(i);
				}
				idx++;
			}

			model.addAttribute("recipe", recipe);
			model.addAttribute("firstIngredients", firstIngredients);
			model.addAttribute("secondIngredients", secondIngredients);
			model.addAttribute("steps", recipe.parseSteps());

			model.addAttribute("siteName", recipe.getName() + " - " + env.getProperty("es.ucm.fdi.site-title-short"));
		}

		else {
			log.error("No such recipe: {}", id);
		}

		return "receta";
	}

}
