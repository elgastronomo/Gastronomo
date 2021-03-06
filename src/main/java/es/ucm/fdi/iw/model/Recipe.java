/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import javax.persistence.PreRemove;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * @author roberto
 *
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Recipe.AllRecipes", query = "SELECT r FROM Recipe r"),
		@NamedQuery(name = "Recipe.ByDifficultyAndCuisine", query = "SELECT r FROM Recipe r WHERE r.difficulty IN :difficulty AND r.cuisine IN :cuisine"),
		@NamedQuery(name = "Recipe.ByCuisine", query = "SELECT r FROM Recipe r WHERE r.cuisine IN :cuisine"),
		@NamedQuery(name = "Recipe.ByDifficulty", query = "SELECT r FROM Recipe r WHERE r.difficulty IN :difficulty") })
public class Recipe implements Comparable {

	@JsonView(Views.Public.class)
	private long id;
	@JsonView(Views.Public.class)
	private String name;
	private String duration;
	private String url;
	private String attribution;
	private float calories;
	private float weight;
	private float rations;
	private User user;
	private String steps;
	private String difficulty;
	private String cuisine;
	private Set<RecipeIngredient> recipeIngredients = new HashSet<>();
	private List<Comment> comments = new ArrayList<>();
	private List<RecipeReport> reports = new ArrayList<>();
	private List<Tag> tags = new ArrayList<>();
	private Set<RecipeNutrient> recipeNutrients = new HashSet<>();
	private List<User> favUsers = new ArrayList<>();
	private Timestamp created;

	public List<String> parseSteps() {
		List<String> parsedSteps = new ArrayList<>();

		JSONObject jSteps = new JSONObject(steps);

		for (String key : jSteps.keySet()) {
			parsedSteps.add(jSteps.getString(key));
		}

		return parsedSteps;
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

	public int durationInt() {
		String[] d = duration.split(" ");
		Integer durationInteger = 0;
		if (d.length == 4) {
			durationInteger += (Integer.parseInt(d[0]) * 60) + Integer.parseInt(d[2]);
		} else {
			if (d[1].contains("H")) {
				durationInteger += Integer.parseInt(d[0]) * 60;
			} else {
				durationInteger += Integer.parseInt(d[0]);
			}
		}
		return durationInteger;
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

	public float getRations() {
		return rations;
	}

	public void setRations(float rations) {
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

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
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
	@OneToMany(targetEntity = Comment.class, cascade = CascadeType.ALL)
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
	 * @return the tags
	 */
	@ManyToMany(targetEntity = Tag.class)
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * @return the tags names
	 */
	public List<String> tagsNames() {
		List<String> tagsNames = new ArrayList<String>();

		for (Tag tag : getTags()) {
			tagsNames.add(tag.getTag());
		}

		return tagsNames;
	}

	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	@OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
	public Set<RecipeNutrient> getRecipeNutrients() {
		return recipeNutrients;
	}

	public void setRecipeNutrients(Set<RecipeNutrient> recipeNutrients) {
		this.recipeNutrients = recipeNutrients;
	}

	public void addRecipeNutrient(RecipeNutrient recipeNutrient) {
		this.recipeNutrients.add(recipeNutrient);
	}

	@Override
	public int compareTo(Object recipe) {
		return (((Recipe) recipe).getId() != this.id) ? 1 : 0;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	@OneToMany(targetEntity = RecipeReport.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "recipe_id")
	public List<RecipeReport> getReports() {
		return reports;
	}

	public void setReports(List<RecipeReport> reports) {
		this.reports = reports;
	}

	@ManyToMany(targetEntity = User.class, mappedBy = "favRecipes")
	public List<User> getFavUsers() {
		return favUsers;
	}

	public void setFavUsers(List<User> favUsers) {
		this.favUsers = favUsers;
	}

	@PreRemove
	private void removeFavRecipesFromUsers() {
		for (User u : favUsers) {
			u.getFavRecipes().remove(this);
		}
	}
}
