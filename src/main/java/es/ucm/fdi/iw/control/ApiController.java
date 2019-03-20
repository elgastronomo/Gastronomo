package es.ucm.fdi.iw.control;

import java.util.List;

import javax.persistence.EntityManager;
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

import es.ucm.fdi.iw.model.Tag;
import es.ucm.fdi.iw.model.User;
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
	@GetMapping("/users/{id}/tags")
	@JsonView(Views.Public.class)
	public List<Tag> status(Model model, @PathVariable long id) {
		User u = (User) session.getAttribute("user");

		return (List<Tag>) entityManager.createNamedQuery("Tag.ByUser").setParameter("userId", Long.parseLong("1"))
				.getResultList();
	}

	@PostMapping("/users/{id}/tags")
	@JsonView(Views.Public.class)
	@Transactional
	public String addFavouriteTag(Model model, @PathVariable long id, @RequestBody Tag tag)
			throws JsonProcessingException {
		User u = (User) entityManager.find(User.class, Long.parseLong("1"));

		Tag t = (Tag) entityManager.createNamedQuery("Tag.ByName").setParameter("tagName", tag.getTag())
				.getSingleResult();

		if (u != null && t != null) {
			u.addFavTag(t);
			entityManager.persist(u);
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
		User u = (User) entityManager.find(User.class, Long.parseLong("1"));

		Tag t = (Tag) entityManager.createNamedQuery("Tag.ByName").setParameter("tagName", tag.getTag())
				.getSingleResult();

		if (u != null && t != null) {
			u.removeFavTag(t);
			entityManager.persist(u);
			return "Tag: " + t.getTag() + " eliminada de favoritos";
		} else {
			return "Error eliminando tag de favoritos";
		}
	}
}
