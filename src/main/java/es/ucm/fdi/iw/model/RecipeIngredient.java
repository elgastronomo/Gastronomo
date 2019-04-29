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

@Entity
@NamedQueries({
	@NamedQuery(name="RecipeIngredient.Filter.Ingredient",
			query="SELECT ri.recipe FROM RecipeIngredient ri WHERE ri.ingredient.name LIKE :name"),
	
	@NamedQuery(name="RecipeIngredient.Filter.IngredientAndCuisine",
			query="SELECT ri.recipe FROM RecipeIngredient ri WHERE ri.ingredient.name LIKE :name AND ri.recipe.cuisine IN :cuisine"),
	
	@NamedQuery(name="RecipeIngredient.Filter.IngredientAndDifficulty",
			query="SELECT ri.recipe FROM RecipeIngredient ri WHERE ri.ingredient.name LIKE :name AND ri.recipe.difficulty IN :difficulty"),
	
	@NamedQuery(name="RecipeIngredient.Filter.IngredientAndDifficultyAndCuisine",
	query="SELECT ri.recipe FROM RecipeIngredient ri WHERE ri.ingredient.name LIKE :name AND ri.recipe.cuisine IN :cuisine AND ri.recipe.difficulty IN :difficulty"),
})
public class RecipeIngredient {

	private long id;
	private Recipe recipe;
	private Ingredient ingredient;

	// additional fields
	private float weight;

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

	@ManyToOne()
	@JoinColumn(name = "recipe_id")
	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}


	@ManyToOne()
	@JoinColumn(name = "ingredient_id")
	public Ingredient getIngredient() {
		return ingredient;
	}

	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

}
