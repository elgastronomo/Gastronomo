/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * @author roberto
 *
 */
@Entity
@NamedQueries(@NamedQuery(name = "Comment.ByRecipe", query = "SELECT c FROM Comment c WHERE c.recipe.id = :recipeId"))
public class Comment {

	private long id;
	private Recipe recipe;
	private Date date;
	private String title;
	private String comment;
	private User user;

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the recipe
	 */
	@ManyToOne(targetEntity = Recipe.class)
	public Recipe getRecipe() {
		return recipe;
	}

	/**
	 * @param recipe the recipe to set
	 */
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the comment
	 */
	// @ManyToOne
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the user
	 */
	@ManyToOne(targetEntity = User.class)
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
