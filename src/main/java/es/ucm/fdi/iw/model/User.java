package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * A user.
 * 
 * Note that, in this particular application, we will automatically be creating
 * users for students. Those users will have the group password as their
 * "password", but will be generally unable to actually log in without the group
 * password.
 * 
 * @author mfreire
 */
@Entity
@NamedQueries({ @NamedQuery(name = "User.ByLogin", query = "SELECT u FROM User u " + "WHERE u.login = :userLogin"),

		@NamedQuery(name = "User.HasLogin", query = "SELECT COUNT(u) " + "FROM User u " + "WHERE u.login = :userLogin"),

		@NamedQuery(name = "User.ByEmail", query = "SELECT u FROM User u " + "WHERE u.email = :email"),

})
public class User {
	@JsonView(Views.Public.class)
	private long id;
	private String login;
	private String password;
	private String roles; // split by ',' to separate roles
	private byte enabled;

	public boolean hasRole(String roleName) {
		return Arrays.stream(roles.split(",")).anyMatch(r -> r.equalsIgnoreCase(roleName));
	}

	// app specific fields
	@JsonView(Views.Public.class)
	private String name;
	private String email;
	private int karma;
	private List<Comment> comments = new ArrayList<>();
	private List<Recipe> recipes = new ArrayList<>();
	private List<Recipe> favRecipes = new ArrayList<>();
	private Set<UserIngredient> favIngredients = new HashSet<>();
	private Set<Tag> favTags = new HashSet<>();
	private List<Menu> menus = new ArrayList<>();

	public User() {
	}

	public User(int karma) {
		this.karma = 0;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(unique = true)
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getKarma() {
		return karma;
	}

	public void setKarma(int karma) {
		this.karma = karma;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(targetEntity = Comment.class)
	@JoinColumn(name = "user_id")
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @return the recipes
	 */
	@OneToMany(targetEntity = Recipe.class)
	@JoinColumn(name = "user_id")
	public List<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}

	@ManyToMany(targetEntity = Recipe.class)
	public List<Recipe> getFavRecipes() {
		return favRecipes;
	}

	public void setFavRecipes(List<Recipe> favRecipes) {
		this.favRecipes = favRecipes;
	}

	@OneToMany(mappedBy = "user")
	public Set<UserIngredient> getFavIngredients() {
		return favIngredients;
	}

	@Transient
	public List<String> getFavIngredientsString() {
		List<String> favIngredientsString = new ArrayList<>();

		favIngredients.forEach(i -> favIngredientsString.add(i.getIngredient().getName()));

		return favIngredientsString;
	}

	public void setFavIngredients(Set<UserIngredient> favIngredients) {
		this.favIngredients = favIngredients;
	}

	@ManyToMany(targetEntity = Tag.class)
	public Set<Tag> getFavTags() {
		return favTags;
	}

	public void setFavTags(Set<Tag> favTags) {
		this.favTags = favTags;
	}

	@Transient
	public List<String> getFavTagsString() {
		List<String> favTagsString = new ArrayList<>();

		favTags.forEach(t -> favTagsString.add(t.getTag()));

		return favTagsString;
	}

	public void addFavTag(Tag tag) {
		this.favTags.add(tag);
	}

	public void removeFavTag(Tag tag) {
		this.favTags.remove(tag);
	}

	@OneToMany(targetEntity = Menu.class)
	@JoinColumn(name = "user_id")
	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public void addMenu(Menu menu) {
		this.menus.add(menu);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", login=" + login + ", password=" + password + ", roles=" + roles + ", enabled="
				+ enabled + "]";
	}
}