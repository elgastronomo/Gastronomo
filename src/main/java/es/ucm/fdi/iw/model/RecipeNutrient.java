package es.ucm.fdi.iw.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class RecipeNutrient {

	private long id;
	private Recipe recipe;
	private Nutrient nutrient;

	// additional fields
	private float cuantity;
	private String unit;
	private float dailyPercentage;

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

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "recipe_id")
	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
	}

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "nutrient_id")
	public Nutrient getNutrient() {
		return nutrient;
	}

	public void setNutrient(Nutrient nutrient) {
		this.nutrient = nutrient;
	}

	public float getCuantity() {
		return cuantity;
	}

	public void setCuantity(float cuantity) {
		this.cuantity = cuantity;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getDailyPercentage() {
		return dailyPercentage;
	}

	public void setDailyPercentage(float dailyPercentage) {
		this.dailyPercentage = dailyPercentage;
	}

}
