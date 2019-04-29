package es.ucm.fdi.iw.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries(@NamedQuery(name = "Nutrient.ByName", query = "SELECT n FROM Nutrient n WHERE n.nutrient = :nutrient"))
public class Nutrient {

	private long id;
	private String nutrient;

	private Set<RecipeNutrient> recipeNutrients = new HashSet<>();

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

	public String getNutrient() {
		return nutrient;
	}

	public void setNutrient(String nutrient) {
		this.nutrient = nutrient;
	}

	@OneToMany(mappedBy = "nutrient",  cascade = CascadeType.ALL)
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
