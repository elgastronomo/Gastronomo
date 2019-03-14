/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author roberto
 *
 */
@Entity
@NamedQueries(
		@NamedQuery(name="Recipe.AllRecipes",
					query="SELECT r FROM Recipe r"
		)
)
public class Recipe {

	private long id;
	private String name;
	private String duration;
	private String url;
	private String attribution;
	private float calories;
	private float weight;
	private int rations;
	private User user;
	private String steps;
	private String difficulty;
	private String cuisine;
	private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
	private List<Comment> comments = new ArrayList<>();
	private List<Valoration> valorations = new ArrayList<>();
	private List<Tag> tags = new ArrayList<>();
	private Set<RecipeNutrient> recipeNutrients = new HashSet<>();

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
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
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
	 * @return the calories
	 */
	public float getCalories() {
		return calories;
	}

	/**
	 * @param calories the calories to set
	 */
	public void setCalories(float calories) {
		this.calories = calories;
	}

	public String getAttribution() {
		return attribution;
	}

	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getRations() {
		return rations;
	}

	public void setRations(int rations) {
		this.rations = rations;
	}

	public String getSteps() {
		return steps;
	}

	public void setSteps(String steps) {
		this.steps = steps;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
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

	@OneToMany(mappedBy = "recipe")
	public Set<RecipeIngredient> getRecipeIngredients() {
		return recipeIngredients;
	}

	public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
		this.recipeIngredients = recipeIngredients;
	}
	
	public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
		this.recipeIngredients.add(recipeIngredient);
	}

	/**
	 * @return the comments
	 */
	@OneToMany(targetEntity = Comment.class)
	@JoinColumn(name = "recipe_id")
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
	@OneToMany(targetEntity = Valoration.class)
	@JoinColumn(name = "recipe_id")
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
	@ManyToMany(targetEntity = Tag.class)
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@OneToMany(mappedBy = "recipe")
	public Set<RecipeNutrient> getRecipeNutrients() {
		return recipeNutrients;
	}

	public void setRecipeNutrients(Set<RecipeNutrient> recipeNutrients) {
		this.recipeNutrients = recipeNutrients;
	}

	public void addRecipeNutrient(RecipeNutrient recipeNutrient) {
		this.recipeNutrients.add(recipeNutrient);
	}

}
