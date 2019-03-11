package es.ucm.fdi.iw.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
	@NamedQuery(name="User.ByLogin",
	query="SELECT u FROM User u "
			+ "WHERE u.name = :userLogin"),
	@NamedQuery(name="User.HasLogin",
	query="SELECT COUNT(u) "
			+ "FROM User u "
			+ "WHERE u.name = :userLogin")	
})
public class User {
	
	public enum Type {
		ADMINISTRATOR,
		NORMAL
	}
	
	private Long id;
	private String name;
	private String password;
	private Date Birthdate;
	private Type type;
	private String email;
	private boolean enabled;
	private String roles; // split by ',' to separate roles
	private List<Comment> comments;
	private List<Valoration> valorations;
	private List<Recipe> recipes;
	
	/**
	 * @return the valorations
	 */
	@OneToMany(targetEntity=Valoration.class)
	public List<Valoration> getValorations() {
		return valorations;
	}
	/**
	 * @param valorations the valorations to set
	 */
	public void setValorations(List<Valoration> valorations) {
		this.valorations = valorations;
	}
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the birthdate
	 */
	public Date getBirthdate() {
		return Birthdate;
	}
	/**
	 * @param birthdate the birthdate to set
	 */
	public void setBirthdate(Date birthdate) {
		Birthdate = birthdate;
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}
	
	@OneToMany(targetEntity=Comment.class)
	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	/**
	 * @return the recipes
	 */
	@ManyToMany(targetEntity=Recipe.class)
	public List<Recipe> getRecipes() {
		return recipes;
	}
	/**
	 * @param recipes the recipes to set
	 */
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	
	
}
