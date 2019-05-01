package es.ucm.fdi.iw.control;

import java.io.File;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Comment;
import es.ucm.fdi.iw.model.CommentReport;
import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.RecipeReport;
import es.ucm.fdi.iw.model.User;

@Controller
@RequestMapping("admin")
public class AdminController {
	
	private static final Logger log = LogManager.getLogger(AdminController.class);

	@Autowired
	private Environment env;

	@Autowired
	private LocalData localData;

	@Autowired
	private EntityManager entityManager;
	
	@GetMapping("/moderar-recetas")
	public String moderarRecetasList(Model model, HttpSession session) {
		List<RecipeReport> reports = entityManager.createNamedQuery("RecipeReport.AllReports", RecipeReport.class)
				.getResultList();

		model.addAttribute("reports", reports);
		model.addAttribute("siteName", "Moderar recetas" + " - " + env.getProperty("es.ucm.fdi.site-title-short"));

		return "admin_recetas";
	}
	
	@GetMapping("/moderar-comentarios")
	public String moderarComentariosList(Model model, HttpSession session) {
		List<CommentReport> reports = entityManager.createNamedQuery("CommentReport.AllReports", CommentReport.class)
				.getResultList();

		model.addAttribute("reports", reports);
		model.addAttribute("siteName", "Moderar comentarios" + " - " + env.getProperty("es.ucm.fdi.site-title-short"));

		return "admin_comentarios";
	}

	@PostMapping("/moderar-recetas/eliminar")
	@Transactional
	public String moderarRecetasDelete(@RequestParam(required = false) long[] idReceta) {

		if (idReceta != null) {
			for (long id : idReceta) {
				Recipe r = entityManager.find(Recipe.class, id);
				entityManager.remove(r);
			}
		}

		return "redirect:/admin/moderar-recetas?eliminadas";
	}
	
	@PostMapping("/moderar-comentarios/eliminar")
	@Transactional
	public String moderarComentariosDelete(@RequestParam(required = false) long[] idComentario) {

		if (idComentario != null) {
			for (long id : idComentario) {
				Comment c = entityManager.find(Comment.class, id);
				entityManager.remove(c);
			}
		}

		return "redirect:/admin/moderar-comentarios?eliminados";
	}

	@PostMapping("/moderar-recetas/aprobar")
	@Transactional
	public String moderarRecetasAprobar(@RequestParam(required = false) long[] idReceta) {

		if (idReceta != null) {
			for (long id : idReceta) {
				List<RecipeReport> rr = entityManager.createNamedQuery("RecipeReport.ByRecipe", RecipeReport.class)
						.setParameter("recipeId", id).getResultList();

				for (RecipeReport rReport : rr) {
					entityManager.remove(rReport);
				}
			}
		}

		return "redirect:/admin/moderar-recetas?aprobadas";
	}
	
	@PostMapping("/moderar-comentarios/aprobar")
	@Transactional
	public String moderarComentariosAprobar(@RequestParam(required = false) long[] idComentario) {

		if (idComentario != null) {
			for (long id : idComentario) {
				List<CommentReport> cr = entityManager.createNamedQuery("CommentReport.ByComment", CommentReport.class)
						.setParameter("commentId", id).getResultList();

				for (CommentReport cReport : cr) {
					entityManager.remove(cReport);
				}
			}
		}

		return "redirect:/admin/moderar-comentarios?aprobados";
	}
	
	@PostMapping("/moderar-recetas/deshabilitar")
	@Transactional
	public String moderarRecetasDeshabilitar(@RequestParam(required = false) long[] idReceta) {

		if (idReceta != null) {
			for (long id : idReceta) {
				List<RecipeReport> rr = entityManager.createNamedQuery("RecipeReport.ByRecipe", RecipeReport.class)
						.setParameter("recipeId", id).getResultList();

				for (RecipeReport rReport : rr) {
					User u = entityManager.find(User.class, rReport.getRecipe().getUser().getId());
					u.setEnabled((byte) 0);
					
					entityManager.remove(entityManager.find(Recipe.class, rReport.getRecipe().getId()));
				}
			}
		}

		return "redirect:/admin/moderar-recetas?deshabilitadas";
	}
	
	@PostMapping("/moderar-comentarios/deshabilitar")
	@Transactional
	public String moderarComentariosDeshabilitar(@RequestParam(required = false) long[] idComentario) {

		if (idComentario != null) {
			for (long id : idComentario) {
				List<CommentReport> cr = entityManager.createNamedQuery("CommentReport.ByComment", CommentReport.class)
						.setParameter("commentId", id).getResultList();

				for (CommentReport cReport : cr) {
					User u = entityManager.find(User.class, cReport.getComment().getUser().getId());
					u.setEnabled((byte) 0);
					
					entityManager.remove(entityManager.find(Comment.class, cReport.getComment().getId()));
				}
			}
		}

		return "redirect:/admin/moderar-comentarios?deshabilitados";
	}
	
	@PostMapping("/toggleuser")
	@Transactional
	public String delUser(Model model,	@RequestParam long id) {
		User target = entityManager.find(User.class, id);
		if (target.getEnabled() == 1) {
			// disable
			File f = localData.getFile("user", ""+id);
			if (f.exists()) {
				f.delete();
			}
			target.setEnabled((byte)0); // disable user
		} else {
			// enable
			target.setEnabled((byte)1);
		}
		return "redirect:/user/" + id;
	}	

}
