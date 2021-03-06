/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author roberto
 *
 */
@Entity
@NamedQueries(@NamedQuery(name = "Comment.ByRecipe", query = "SELECT c FROM Comment c WHERE c.recipe.id = :recipeId"))
public class Comment {

	private long id;
	@JsonView(Views.Public.class)
	private Recipe recipe;
	@JsonView(Views.Public.class)
	private Timestamp created;
	@JsonView(Views.Public.class)
	private String title;
	
	private String comment;
	@JsonView(Views.Public.class)
	private User user;
	private List<CommentReport> reports = new ArrayList<>();

	public Comment() {
	}

	public Comment(Recipe recipe, String title, String comment, User user) {
		this.recipe = recipe;
		this.created = new Timestamp(new Date().getTime());
		this.title = title;
		this.comment = comment;
		this.user = user;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the comment
	 */
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

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	@OneToMany(targetEntity = CommentReport.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "comment_id")
	public List<CommentReport> getReports() {
		return reports;
	}

	public void setReports(List<CommentReport> reports) {
		this.reports = reports;
	}

}
