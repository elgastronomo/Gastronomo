package es.ucm.fdi.iw.control;

import java.security.Principal;

import javax.persistence.EntityManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
	public String index(Model model) {

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
