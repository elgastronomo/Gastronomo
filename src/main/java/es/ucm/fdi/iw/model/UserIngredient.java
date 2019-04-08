package es.ucm.fdi.iw.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@NamedQueries(@NamedQuery(name = "UserIngredient.ByName", query = "SELECT ui FROM UserIngredient ui WHERE LOWER(ui.customName) LIKE TRIM(LOWER(:name)) AND ui.user.id = :user"))
public class UserIngredient {

	private long id;
	private User user;
	private Ingredient ingredient;

	// additional fields
	@JsonView(Views.Public.class)
	private String customName;

	public UserIngredient() {
	}

	public UserIngredient(String ingredient, User u) {
		user = u;
		customName = ingredient;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@ManyToOne()
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne()
	@JoinColumn(name = "ingredient_id")
	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

}
