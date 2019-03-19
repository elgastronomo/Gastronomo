package es.ucm.fdi.iw.control;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import es.ucm.fdi.iw.IwUserDetailsService;
import es.ucm.fdi.iw.model.User;

@Controller
public class LoginAndRegistrationController {

	
	
	private static final Logger log = LogManager.getLogger(LoginAndRegistrationController.class);
	
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
		return "registration";
		
	}
	
	@PostMapping("/login/in")
	public String loggin(String username, String password, Model model, HttpSession session) {
		try {
			
			User user = (User) userDetailsService.loadUserByUsername(username);
			
			if(bcrypt.matches(password, user.getPassword())) {
				session.setAttribute("user", user);
				return "index";
			}
			
		} catch (Exception e) {
			model.addAttribute("error",true);
		} 
		return "login";
	
		
	}
	
	@PostMapping("/login/registration")
	public String registration(String username,String email, String password,String matchPassword,Model model, HttpSession session) {
		
		//check if email exist		
		if(checkExistUsername(username)) {
			model.addAttribute("error", "username already exist");
			return("registration");
		}
		
		//check if email exist
		if(checkExistEmail(email)) {
			model.addAttribute("error", "email already exist");
			return("registration");
		}
		
		//check if passwords are equals
		if(!password.equalsIgnoreCase(matchPassword)) {
			model.addAttribute("error", "password are not equals");
			return("registration");
		}
		
		User user = new User();
		String finalPass = bcrypt.encode(password);
		user = new User();
		user.setEmail(email);
		user.setLogin(username);
		user.setPassword(finalPass);
		user.setRoles("USER");
		if(persistUser(user)) {
			session.setAttribute("user", user); 
			return "index";
		}
		
		return "registration";
					
	}
	
	private boolean checkExistUsername(String username) {
		
		try {
			User user = entityManager.createNamedQuery("User.ByLogin", User.class)
			.setParameter("userLogin", username)
			.getSingleResult();		
			return true;
		} 
		catch (Exception e) {
			return false;
		}
	
	}
	
	private boolean checkExistEmail(String username) {
		
		try {
			User user = entityManager.createNamedQuery("User.ByEmail", User.class)
			.setParameter("userLogin", username)
			.getSingleResult();		
			return true;
		} 
		catch (Exception e) {
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
