package es.ucm.fdi.iw.control;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.ucm.fdi.iw.model.User;

@Controller
public class RootController {

	private static final Logger log = LogManager.getLogger(RootController.class);

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private IwSocketHandler iwSocketHandler;

	@ModelAttribute
	public void addAttributes(Model model) {
		model.addAttribute("siteName", env.getProperty("es.ucm.fdi.site-title"));
	}

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
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
				entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(6).getResultList());

		return "index";
	}

	@GetMapping("/admin")
	public String admin(Model model, Principal principal) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));

		log.info("let us all welcome this admin, {}", principal.getName());

		return "index";
	}

}
