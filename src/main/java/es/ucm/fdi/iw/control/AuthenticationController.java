package es.ucm.fdi.iw.control;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.ucm.fdi.iw.IwUserDetailsService;
import es.ucm.fdi.iw.model.User;

@Controller
public class AuthenticationController {

	private static final Logger log = LogManager.getLogger(AuthenticationController.class);

	@Autowired
	PasswordEncoder bcrypt;

	@Autowired
	private IwUserDetailsService userDetailsService;

	@Autowired
	private Environment env;

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/login")
	public String login() {
		return "login";

	}

	@PostMapping("/login")
	public String loggin(String username, String password, Model model, HttpSession session) {
		try {

			User user = entityManager.createNamedQuery("User.ByLogin", User.class).setParameter("userLogin", username)
					.getSingleResult();

			if (bcrypt.matches(password, user.getPassword())) {
				session.setAttribute("user", user);
				return "redirect:/";
			}

		} catch (Exception e) {
			model.addAttribute("error", true);
		}
		return "login";

	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";// You can redirect wherever you want, but generally it's a good practice to
										// show login screen again.
	}

	@PostMapping("/register")
	public String registration(String name, String username, String email, String password, String matchPassword,
			Model model, HttpSession session) {

		// check if email exist
		if (checkExistUsername(username)) {
			model.addAttribute("error", "username already exist");
			return ("loginAndRegistration");
		}

		// check if email exist
		if (checkExistEmail(email)) {
			model.addAttribute("error", "email already exist");
			return ("loginAndRegistration");
		}

		// check if passwords are equals
		if (!password.equalsIgnoreCase(matchPassword)) {
			model.addAttribute("error", "password are not equals");
			return ("loginAndRegistration");
		}

		User user = new User();
		String finalPass = bcrypt.encode(password);
		user.setName(name);
		user.setEmail(email);
		user.setLogin(username);
		user.setPassword(finalPass);
		user.setRoles("USER");
		if (persistUser(user)) {
			session.setAttribute("user", user);
			System.out.println("conseguido");
			return "index";
		}

		return "index";
	}

	private boolean checkExistUsername(String username) {

		try {
			User user = entityManager.createNamedQuery("User.ByLogin", User.class).setParameter("userLogin", username)
					.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	private boolean checkExistEmail(String username) {

		try {
			User user = entityManager.createNamedQuery("User.ByEmail", User.class).setParameter("email", username)
					.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Transactional
	private boolean persistUser(User user) {
		try {
			entityManager.persist(user);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
