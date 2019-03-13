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
import javax.persistence.OneToMany;

/**
 * @author roberto
 *
 */
@Entity
public class Ingredient {
	
	private Long id;
	private String name;
	private int calories;
	private int protein;
	private int carbohydrates;
	private int fats;
	private List<Recipe> recipes = new ArrayList<>(); ;
	private List<Portion> portions = new ArrayList<>(); ;
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
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
	 * @return the protein
	 */
	public int getProtein() {
		return protein;
	}
	/**
	 * @param protein the protein to set
	 */
	public void setProtein(int protein) {
		this.protein = protein;
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
	 * @return the recipes
	 */
	@ManyToMany(targetEntity=Recipe.class)
	public List<Recipe> getRecipes() {
		return recipes;
	}
	/**
	 * @param recipes the recipes to set
	 */
	public void setRecipes(List<Recipe> recipes) {
		this.recipes = recipes;
	}
	/**
	 * @return the portions
	 */
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
