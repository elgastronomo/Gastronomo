package es.ucm.fdi.iw.control;

import javax.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

	private static final Logger log = LogManager.getLogger(SearchController.class);

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;

	@PostMapping("/buscar")
	public String getRecipesSearch(@RequestParam String ingredientes, Model model) {				
		
		model.addAttribute("recipes",entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(6).getResultList());
		
		return "buscar";
	}	
	
	@PostMapping("/buscar_avanzado")
	public String getRecipesSearchAdvanced(@RequestParam int tiempo, @RequestParam(required = false) String facil, @RequestParam(required = false) String Medio, @RequestParam(required = false) String Difícil,
			@RequestParam(required = false) String Española, @RequestParam(required = false) String Italiana, @RequestParam(required = false) String Griega, @RequestParam(required = false) String Mejicana, 
			@RequestParam(required = false) String LowCH, @RequestParam(required = false) String LowSodium, @RequestParam(required = false) String HighFibre, @RequestParam(required = false) String HighProtein, 
			@RequestParam(required = false) String LowFat, @RequestParam(required = false) String NoGluten,	@RequestParam(required = false) String NoEgg, @RequestParam(required = false) String NoSugar, 
			@RequestParam(required = false) String NoPenauts, Model model) {		
		
		model.addAttribute("recipes",entityManager.createNamedQuery("Recipe.AllRecipes").setMaxResults(6).getResultList());
		
		return "buscar";
	}

}
