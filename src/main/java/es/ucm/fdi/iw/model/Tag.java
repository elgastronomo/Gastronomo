/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author roberto
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Tag.ByName", query = "SELECT t FROM Tag t WHERE t.tag = :tagName"),
		@NamedQuery(name = "Tag.ByUser", query = "SELECT u.favTags FROM User u WHERE u.id = :userId") })
public class Tag {

	private long id;
	@JsonView(Views.Public.class)
	private String tag;
	private List<Recipe> recipe = new ArrayList<>();

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * @return the recipe
	 */
	@ManyToMany(targetEntity = Recipe.class, mappedBy = "tags")
	public List<Recipe> getRecipe() {
		return recipe;
	}

	/**
	 * @param recipe the recipe to set
	 */
	public void setRecipe(List<Recipe> recipe) {
		this.recipe = recipe;
	}

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

}
