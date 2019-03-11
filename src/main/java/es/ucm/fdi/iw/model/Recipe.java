/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import es.ucm.fdi.iw.model.User;



/**
 * @author roberto
 *
 */
@Entity
public class Recipe {
	
	private Long Id;
	private String name;
	private int duration;
	private String url;
	private Double valuation;
	private int calories;
	private User user;
	private int proteins;
	private int carbohydrates;
	private int fats;
	private String description;
	private List<Ingredient> ingredients;
	private List<Comment> comments;
	private List<Valoration> valorations;
	private List<Tag> tags;
	private List<Portion> portions;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		Id = id;
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
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the valuation
	 */
	public Double getValuation() {
		return valuation;
	}
	/**
	 * @param valuation the valuation to set
	 */
	public void setValuation(Double valuation) {
		this.valuation = valuation;
	}
	/**
	 * @return the calories
	 */
	public int getCalories() {
		return calories;
	}
	/**
	 * @param calories the calories to set
	 */
	public void setCalories(int calories) {
		this.calories = calories;
	}
	/**
	 * @return the user
	 */
	@ManyToOne(targetEntity=User.class)
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the proteins
	 */
	public int getProteins() {
		return proteins;
	}
	/**
	 * @param proteins the proteins to set
	 */
	public void setProteins(int proteins) {
		this.proteins = proteins;
	}
	/**
	 * @return the carbohydrates
	 */
	public int getCarbohydrates() {
		return carbohydrates;
	}
	/**
	 * @param carbohydrates the carbohydrates to set
	 */
	public void setCarbohydrates(int carbohydrates) {
		this.carbohydrates = carbohydrates;
	}
	/**
	 * @return the fats
	 */
	public int getFats() {
		return fats;
	}
	/**
	 * @param fats the fats to set
	 */
	public void setFats(int fats) {
		this.fats = fats;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToMany(targetEntity=Ingredient.class)
	public List<Ingredient> getIngredient() {
		return ingredients;
	}
	public void setIngredient(List<Ingredient> ingredient) {
		this.ingredients = ingredient;
	}
	/**
	 * @return the comments
	 */
	@OneToMany(targetEntity=Comment.class)
	public List<Comment> getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
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
	 * @return the tags
	 */
	@ManyToMany(targetEntity=Tag.class)
	public List<Tag> getTags() {
		return tags;
	}
	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}
	
	@OneToMany(targetEntity=Portion.class)
	public List<Portion> getPortions() {
		return portions;
	}
	/**
	 * @param portions the portions to set
	 */
	public void setPortions(List<Portion> portions) {
		this.portions = portions;
	}
	
}
