package es.ucm.fdi.iw.model;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * A user.
 * 
 * Note that, in this particular application, we will automatically be creating
 * users for students. Those users will have the group password as their "password", 
 * but will be generally unable to actually log in without the group password.  
 * 
 * @author mfreire
 */
@Entity
@NamedQueries({
	@NamedQuery(name="User.ByLogin",
	query="SELECT u FROM User u "
			+ "WHERE u.login = :userLogin"),
	@NamedQuery(name="User.HasLogin",
	query="SELECT COUNT(u) "
			+ "FROM User u "
			+ "WHERE u.login = :userLogin")	
})
public class User {
	private long id;
	private String login;
	private String password;
	private String roles; // split by ',' to separate roles
	private byte enabled;
	
	public boolean hasRole(String roleName) {
		String requested = roleName.toLowerCase();
		return Arrays.stream(roles.split(","))
				.anyMatch(r -> r.equals(requested));
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}	

	@Column(unique=true)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public byte getEnabled() {
		return enabled;
	}

	public void setEnabled(byte enabled) {
		this.enabled = enabled;
	}
	

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", roles=" + roles + ", enabled="
				+ enabled + "]";
	}
}