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

@Controller
public class RootController {
	
	private static final Logger log = LogManager.getLogger(RootController.class);
	
	@Autowired
	private Environment env;
	
	@Autowired 
	private EntityManager entityManager;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;
	
	@GetMapping("/")
	public String index(Model model) {
		// esto es de prueba
		model.addAttribute("recetas", entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(9).getResultList());
		
		return "index";
	}
	
	@GetMapping("/buscar")
	public String buscar(Model model) {
		return "buscar";
	}
	
	@GetMapping("/receta")
	public String receta(Model model) {
		return "receta";
	}
	
	@GetMapping("/admin")
	public String admin(Model model, Principal principal) {
		model.addAttribute("activeProfiles", env.getActiveProfiles());
		model.addAttribute("basePath", env.getProperty("es.ucm.fdi.base-path"));
		
		log.info("let us all welcome this admin, {}", principal.getName());
		
		return "index";
	}
	
}
