package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
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
	private LocalData localData;

	@GetMapping("/{id}")
	public String getUser(@PathVariable long id, Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}

		model.addAttribute("user", target);
		model.addAttribute("siteName",
				"Tu perfil - " + target.getName() + " - " + env.getProperty("es.ucm.fdi.site-title-short"));

		return "perfil";
	}

	@PostMapping("/{id}/editar")
	@Transactional
	public String postUser(@PathVariable long id, @RequestParam(required=false) String photo, 
						   @RequestParam String name, @RequestParam String email, HttpSession session) {
		User target = entityManager.find(User.class, id);

		User requester = (User) session.getAttribute("user");
		if (requester.getId() != target.getId() && !requester.hasRole("ADMIN")) {
			return "redirect:/user/" + requester.getId();
		}
		
		target.setName(name);
		target.setEmail(email);

		if (photo != null) {
			// This will decode the String which is encoded by using Base64 class
			// String base64Image =
			// 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAPAAAADwCAYAAAA+VemSAAAgAEl...=='
			String base64Image = photo.split(",")[1];
			byte[] imageByte = Base64.decodeBase64(base64Image);
			File f = localData.getFile("user", "" + id);

			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				stream.write(imageByte);
			} catch (Exception e) {
				log.info("Error uploading " + id + " ", e);
			}
		}

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
}
