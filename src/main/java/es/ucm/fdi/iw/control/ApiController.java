package es.ucm.fdi.iw.control;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;

import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.Tag;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.UserIngredient;
import es.ucm.fdi.iw.model.Views;

@RestController
@RequestMapping("api")
public class ApiController {

	private static final Logger log = LogManager.getLogger(ApiController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private IwSocketHandler iwSocketHandler;

	@Autowired
	private HttpSession session;

	@SuppressWarnings("unchecked")
	@GetMapping("/users/{id}/ingredients")
	@JsonView(Views.Public.class)
	public Set<UserIngredient> getFavouriteIngredients(Model model, @PathVariable long id) {
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());

		return u.getFavIngredients();
	}

	@PostMapping("/users/{id}/ingredients")
	@JsonView(Views.Public.class)
	@Transactional
	public String addFavouriteIngredient(Model model, @PathVariable long id, @RequestBody UserIngredient ingredient)
			throws JsonProcessingException {
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());

		try {
			entityManager.createNamedQuery("UserIngredient.ByName")
					.setParameter("name", ingredient.getCustomName())
					.setParameter("user", u.getId())
					.getSingleResult();
			
			// Do nothing if exists
			
		} catch (NoResultException e) {
			
			ingredient.setUser(u);
			u.getFavIngredients().add(ingredient);
			entityManager.persist(ingredient);
		}

		return "Ingrediente: " + ingredient.getCustomName() + " añadadido a favoritos";

	}

	@SuppressWarnings("unchecked")
	@GetMapping("/users/{id}/tags")
	@JsonView(Views.Public.class)
	public List<Tag> getFavouriteTags(Model model, @PathVariable long id) {
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());

		return (List<Tag>) entityManager.createNamedQuery("Tag.ByUser").setParameter("userId", u.getId())
				.getResultList();
	}

	@PostMapping("/users/{id}/tags")
	@JsonView(Views.Public.class)
	@Transactional
	public String addFavouriteTag(Model model, @PathVariable long id, @RequestBody Tag tag)
			throws JsonProcessingException {
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());

		Tag t = (Tag) entityManager.createNamedQuery("Tag.ByName").setParameter("tagName", tag.getTag())
				.getSingleResult();

		if (u != null && t != null) {
			u.addFavTag(t);
			return "Tag: " + t.getTag() + " añadadida a favoritos";
		} else {
			return "Error añadiendo tag a favoritos";
		}
	}

	@DeleteMapping("/users/{id}/tags")
	@JsonView(Views.Public.class)
	@Transactional
	public String removeFavouriteTag(Model model, @PathVariable long id, @RequestBody Tag tag)
			throws JsonProcessingException {
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());

		Tag t = (Tag) entityManager.createNamedQuery("Tag.ByName").setParameter("tagName", tag.getTag())
				.getSingleResult();

		if (u != null && t != null) {
			u.removeFavTag(t);
			return "Tag: " + t.getTag() + " eliminada de favoritos";
		} else {
			return "Error eliminando tag de favoritos";
		}
	}
	
	@DeleteMapping("/users/{id}/ingredients")
	@JsonView(Views.Public.class)
	@Transactional
	public String removeFavouriteIngredient(Model model, @PathVariable long id, @RequestBody UserIngredient ingredient)
			throws JsonProcessingException {
		User u = (User) session.getAttribute("user");
		u = entityManager.find(User.class, u.getId());

		try {
			UserIngredient ingre = (UserIngredient) entityManager.createNamedQuery("UserIngredient.ByName")
					.setParameter("name", ingredient.getCustomName()).setParameter("user", u.getId()).getSingleResult();
			
			u.getFavIngredients().remove(ingre);
			entityManager.remove(ingre);
			
		} catch (NoResultException e) {

			return "Error eliminando ingrediente de favoritos";
		}

		return "Ingrediente: " + ingredient.getCustomName() + " eliminado de favoritos";
	}
	
	@PostMapping("/users/{id}/addRecipe")
	@JsonView(Views.Public.class)
	@Transactional
	public void addFavouriteRecipe(Model model, @PathVariable Long id) throws JsonProcessingException {
		
		
 		User user = (User) session.getAttribute("user");
		user = entityManager.find(User.class, user.getId());
		
		Recipe recipe = entityManager.find(Recipe.class, id);
		user.getFavRecipes().add(recipe);
	}
		
}
