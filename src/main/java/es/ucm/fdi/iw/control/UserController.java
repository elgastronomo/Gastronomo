package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
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

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Comment;
import es.ucm.fdi.iw.model.CommentReport;
import es.ucm.fdi.iw.model.Menu;
import es.ucm.fdi.iw.model.Recipe;
import es.ucm.fdi.iw.model.RecipeReport;
import es.ucm.fdi.iw.model.User;

@Controller()
@RequestMapping("user")
public class UserController {

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private IwSocketHandler iwSocketHandler;

	@Autowired
	private LocalData localData;

	@GetMapping("/{id}")
	public String getUser(@PathVariable long id, Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}
		
		if (requester.getEnabled() == 0) {
			session.invalidate();
			return "redirect:/?userDisabled";
		}

		model.addAttribute("user", target);
		model.addAttribute("siteName",
				"Tu perfil - " + target.getName() + " - " + env.getProperty("es.ucm.fdi.site-title-short"));

		return "perfil";
	}

	@PostMapping("/{id}/editar")
	@Transactional
	public String postUser(@PathVariable long id, @RequestParam(required = false) String photo,
			@RequestParam String name, @RequestParam String email, @RequestParam String login, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}

		target.setLogin(login);
		target.setName(name);
		target.setEmail(email);

		if (photo != null) {
			// This will decode the String which is encoded by using Base64 class
			// String base64Image =
			// 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPAAAADwCAYAAAA+VemSAAAgAEl...=='
			String base64Image = photo.split(",")[1];
			byte[] imageByte = Base64.decodeBase64(base64Image);
			File f = localData.getFile("user", "" + target.getId());

			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				stream.write(imageByte);
			} catch (Exception e) {
				log.info("Error uploading " + target.getId() + " ", e);
			}
		}

		session.setAttribute("user", target);

		return "redirect:/user/" + target.getId();
	}

	@PostMapping(value = "/{id}/menu/receta/{idReceta}")
	@Transactional
	public String addRecipeToMenu(@PathVariable long id, @PathVariable long idReceta, @RequestParam String[] menus,
			Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}

		for (String menu : menus) {
			Menu m = entityManager.find(Menu.class, Long.parseLong(menu));
			Recipe r = entityManager.find(Recipe.class, idReceta);
			m.getRecipes().add(r);
		}

		return "redirect:/user/" + target.getId();
	}

	@GetMapping(value = "/{id}/menu/{idMenu}/receta/{idReceta}")
	@Transactional
	public String delRecipeMenu(@PathVariable long id, @PathVariable long idMenu, @PathVariable long idReceta,
			Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}

		Menu m = entityManager.find(Menu.class, idMenu);
		Recipe r = entityManager.find(Recipe.class, idReceta);
		m.getRecipes().remove(r);

		return "redirect:/user/" + target.getId();
	}

	@GetMapping(value = "/{id}/photo")
	public StreamingResponseBody getPhoto(@PathVariable long id, Model model) throws IOException {
		File f = localData.getFile("user", "" + id);
		InputStream in;
		if (f.exists()) {
			in = new BufferedInputStream(new FileInputStream(f));
		} else {
			in = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("static/img/avatar.png"));
		}
		return new StreamingResponseBody() {
			@Override
			public void writeTo(OutputStream os) throws IOException {
				FileCopyUtils.copy(in, os);
			}
		};
	}

	@PostMapping(value = "/{id}/menu")
	@Transactional
	public String menu(@PathVariable long id, @RequestParam String title, @RequestParam String description,
			HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}

		Menu menu = new Menu();
		menu.setName(title);
		menu.setDescription(description);
		menu.setUser(target);

		entityManager.persist(menu);
		target.addMenu(menu);

		session.setAttribute("user", target);

		return "redirect:/user/" + target.getId();
	}

	@PostMapping(value = "/{id}/eliminarMenu")
	@Transactional
	public String deleteMenu(@PathVariable long id, @RequestParam long idMenu, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}

		Menu menu = entityManager.find(Menu.class, idMenu);
		entityManager.remove(menu);

		session.setAttribute("user", target);

		return "redirect:/user/" + target.getId();
	}
}
