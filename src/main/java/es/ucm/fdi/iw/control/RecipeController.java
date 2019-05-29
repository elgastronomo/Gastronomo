package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Comment;
import es.ucm.fdi.iw.model.CommentReport;
import es.ucm.fdi.iw.model.Ingredient;
import es.ucm.fdi.iw.model.Nutrient;
import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.RecipeIngredient;
import es.ucm.fdi.iw.model.RecipeNutrient;
import es.ucm.fdi.iw.model.RecipeReport;
import es.ucm.fdi.iw.model.Tag;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Views;

@Controller
@RequestMapping("receta")
public class RecipeController {

	private static final Logger log = LogManager.getLogger(RecipeController.class);

	@Autowired
	private Environment env;

	@Autowired
	private LocalData localData;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private IwSocketHandler iwSocketHandler;

	@GetMapping("/{id}")
	public String getRecipe(@PathVariable long id, Model model, HttpSession session) {

		Recipe recipe = entityManager.find(Recipe.class, id);
		List<Long> favRecipesId = new ArrayList<>();

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

			User user = (User) session.getAttribute("user");
			if (user != null) {
				user = entityManager.find(User.class, user.getId());
				if (!user.getFavRecipes().isEmpty()) {
					favRecipesId = user.getFavRecipes().stream().map(r -> r.getId()).collect(Collectors.toList());
				}
			}

			model.addAttribute("user", user);
			model.addAttribute("recipe", recipe);
			model.addAttribute("firstIngredients", firstIngredients);
			model.addAttribute("secondIngredients", secondIngredients);
			model.addAttribute("steps", recipe.parseSteps());
			model.addAttribute("favRecipes", favRecipesId);

			model.addAttribute("siteName", recipe.getName() + " - " + env.getProperty("es.ucm.fdi.site-title-short"));
		}

		else {
			log.error("No such recipe: {}", id);
		}

		return "receta";
	}

	@PostMapping("/{id}/comentarios")
	@Transactional
	public String comentar(@PathVariable long id, @RequestParam String tituloComentario,
			@RequestParam String comentario, Model model, HttpSession session) throws JsonProcessingException {

		// check permissions
		User requester = (User) session.getAttribute("user");
		if (requester == null) {
			return "login";
		}

		User user = (User) session.getAttribute("user");
		user = entityManager.find(User.class, user.getId());
		Recipe recipe = entityManager.find(Recipe.class, id);

		Comment comment = new Comment(recipe, tituloComentario, comentario, user);
		entityManager.persist(comment);

		// Send notification when comment author is not the recipe creator
		if (user.getId() != recipe.getUser().getId()) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			String commentAsJson = mapper.writerWithView(Views.Public.class).writeValueAsString(comment);
			String message = "{\"comment\": " + commentAsJson + "}";

			this.iwSocketHandler.sendText(recipe.getUser().getLogin(), message);
		}

		return "redirect:/receta/" + id;
	}

	@GetMapping("/{idRecipe}/comentarios/{idComentario}/borrar")
	@Transactional
	public String borrarComentario(@PathVariable long idRecipe, @PathVariable long idComentario, Model model,
			HttpSession session) {

		// check permissions
		User requester = (User) session.getAttribute("user");
		if (requester == null) {
			return "login";
		}

		User user = (User) session.getAttribute("user");
		user = entityManager.find(User.class, user.getId());

		Comment comment = entityManager.find(Comment.class, idComentario);

		// check if the user owns the comment
		if (user.getComments().contains(comment)) {
			entityManager.remove(comment);
		}

		return "redirect:/receta/" + idRecipe;
	}

	@GetMapping("/nueva")
	public String nuevaForm(Model model, HttpSession session) {
		// check permissions

		User requester = (User) session.getAttribute("user");
		if (requester == null) {
			return "login";
		}

		model.addAttribute("siteName", "Crear nueva receta - " + env.getProperty("es.ucm.fdi.site-title-short"));

		return "nueva";
	}

	@PostMapping("/nueva")
	@Transactional
	public String nueva(@RequestParam(required = false) String[] nutriente, @RequestParam String nombre_receta,
			@RequestParam String cocina, @RequestParam String dificultad, @RequestParam String duracion,
			@RequestParam int raciones, @RequestParam Float calorias, @RequestParam(required = false) String[] tag,
			@RequestParam String[] ingrediente, @RequestParam Float[] pesoIngrediente, @RequestParam String[] paso,
			@RequestParam String photo, HttpSession session) {

		// check permissions
		User requester = (User) session.getAttribute("user");
		if (requester == null) {
			return "login";
		}

		User user = (User) session.getAttribute("user");

		Recipe recipe = new Recipe();
		recipe.setName(nombre_receta);
		recipe.setAttribution(user.getName());
		recipe.setUser(user);
		recipe.setCalories(calorias);
		recipe.setCuisine(cocina);
		recipe.setRations(raciones);
		recipe.setDuration(duracion);
		recipe.setDifficulty(dificultad);
		recipe.setCreated(new Timestamp(new Date().getTime()));

		JSONObject stepsJSON = new JSONObject();
		for (int i = 0; i < paso.length; i++) {
			stepsJSON.put(Integer.toString(i + 1), paso[i]);
		}

		recipe.setSteps(stepsJSON.toString());

		if (tag != null) {
			for (String t : tag) {
				Tag _t = entityManager.createNamedQuery("Tag.ByName", Tag.class).setParameter("tagName", t)
						.getSingleResult();

				if (_t != null) {
					_t.addRecipe(recipe);
					recipe.addTag(_t);
				}
			}
		}

		entityManager.persist(recipe);

		// This will decode the String which is encoded by using Base64 class
		// String base64Image =
		// 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPAAAADwCAYAAAA+VemSAAAgAEl...=='
		String base64Image = photo.split(",")[1];
		byte[] imageByte = Base64.decodeBase64(base64Image);
		File f = localData.getFile("recipe", Long.toString(recipe.getId()));

		try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
			stream.write(imageByte);
		} catch (Exception e) {
			log.info("Error uploading " + Long.toString(recipe.getId()) + " ", e);
		}

		if (nutriente != null) {
			for (int i = 0; i < nutriente.length; i++) {
				RecipeNutrient recipeNutrient = new RecipeNutrient(getNutrientByPos(i));
				recipeNutrient.setCuantity(Float.parseFloat(nutriente[i]));
				recipeNutrient.setRecipe(recipe);
				entityManager.persist(recipeNutrient);
				recipe.addRecipeNutrient(recipeNutrient);
			}
		}

		for (int i = 0; i < ingrediente.length; i++) {
			Ingredient ing = new Ingredient();
			ing.setName(ingrediente[i]);
			entityManager.persist(ing);
			RecipeIngredient ingredient = new RecipeIngredient();
			ingredient.setIngredient(ing);
			ingredient.setRecipe(recipe);
			ingredient.setWeight(pesoIngrediente[i]);
			entityManager.persist(ingredient);
			recipe.addRecipeIngredient(ingredient);
		}

		return "redirect:/receta/" + recipe.getId();
	}

	@GetMapping(value = "/{id}/photo")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException {
		File f = localData.getFile("recipe", "" + id);
		InputStream in;
		if (f.exists()) {
			in = new BufferedInputStream(new FileInputStream(f));
		} else {
			in = new BufferedInputStream(
					getClass().getClassLoader().getResourceAsStream("static/img/recetas/" + "" + id + ".jpg"));
		}
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream os) throws IOException {
				FileCopyUtils.copy(in, os);
			}
		};
	}

	@GetMapping(value = "/{id}/delete")
	@Transactional
	public String deleteRecipe(@PathVariable long id, HttpSession session) {
		Recipe r = entityManager.find(Recipe.class, id);

		// check permissions
		User requester = (User) session.getAttribute("user");
		if (requester == null || (r.getUser().getId() != requester.getId() && !requester.hasRole("ADMIN"))) {
			return "login";
		}

		entityManager.remove(r);

		return "redirect:/buscar";
	}

	@PostMapping(value = "/{id}/reportar")
	@Transactional
	public String reportRecipe(@PathVariable long id, @RequestParam String reason,
			@RequestParam(required = false) String additionalMessage, HttpSession session) {

		// check permissions
		User requester = (User) session.getAttribute("user");
		if (requester == null) {
			return "login";
		}

		Recipe r = entityManager.find(Recipe.class, id);
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());
		RecipeReport rr = new RecipeReport();

		rr.setRecipe(r);
		rr.setUser(u);
		rr.setReason(reason);
		if (additionalMessage != null)
			rr.setAdditionalMessage(additionalMessage);
		rr.setCreated(new Timestamp(new Date().getTime()));

		entityManager.persist(rr);

		return "redirect:/receta/" + id;
	}

	@PostMapping(value = "/{id}/reportar-comentario/{idComentario}")
	@Transactional
	public String reportComment(@PathVariable long id, @PathVariable long idComentario, @RequestParam String reason,
			@RequestParam(required = false) String additionalMessage, HttpSession session) {

		// check permissions
		User requester = (User) session.getAttribute("user");
		if (requester == null) {
			return "login";
		}

		Comment comment = entityManager.find(Comment.class, idComentario);
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());
		CommentReport cr = new CommentReport();

		cr.setComment(comment);
		cr.setUser(u);
		cr.setReason(reason);
		if (additionalMessage != null)
			cr.setAdditionalMessage(additionalMessage);
		cr.setCreated(new Timestamp(new Date().getTime()));

		entityManager.persist(cr);

		return "redirect:/receta/" + id;
	}

	private Nutrient getNutrientByPos(int pos) {
		String nutrient;

		switch (pos) {
		case 0:
			nutrient = "Grasa";
			break;
		case 1:
			nutrient = "Colesterol";
			break;
		case 2:
			nutrient = "Sodio";
			break;
		case 3:
			nutrient = "Potasio";
			break;
		case 4:
			nutrient = "Carbohidratos";
			break;
		case 5:
			nutrient = "Fibra";
			break;
		case 6:
			nutrient = "Calcio";
			break;
		case 7:
			nutrient = "Hierro";
			break;
		case 8:
			nutrient = "Vitamina B6";
			break;
		case 9:
			nutrient = "Magnesio";
			break;
		default:
			nutrient = null;
		}
		Nutrient n = entityManager.createNamedQuery("Nutrient.ByName", Nutrient.class)
				.setParameter("nutrient", nutrient).getSingleResult();
		return n;
	}

}
