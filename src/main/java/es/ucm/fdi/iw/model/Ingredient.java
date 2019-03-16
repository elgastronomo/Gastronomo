/**
 * 
 */
package es.ucm.fdi.iw.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * @author roberto
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Ingredient.AllIngredients",
			query="SELECT i FROM Ingredient i"
	),
	@NamedQuery(name="Ingredient.ByName",
				query="SELECT i FROM Ingredient i WHERE :names LIKE '%i.name%' "
	)
})
public class Ingredient {

	private long id;
	private String name;

	private Set<RecipeIngredient> recipeIngredients = new HashSet<>();

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

	@OneToMany(mappedBy = "ingredient")
	public Set<RecipeIngredient> getRecipeIngredients() {
		return recipeIngredients;
	}

	public void setRecipeIngredients(Set<RecipeIngredient> recipeIngredients) {
		this.recipeIngredients = recipeIngredients;
	}
	
	public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
		this.recipeIngredients.add(recipeIngredient);
	}

}
