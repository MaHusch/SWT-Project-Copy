package model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class Pizza extends Item {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4746830994439574139L;
	@OneToMany private List<Ingredient> ingredients; 
	
	public Pizza(String name, javax.money.MonetaryAmount price, Ingredient Startitem)
	{
		super(name,price);
		this.ingredients = new LinkedList<Ingredient>();
		ingredients.add(Startitem);
	}
	
	public boolean addIngredient(Ingredient i)
	{
		if(ingredients.contains(i)) return false;
		
		ingredients.add(i);
		this.setPrice(getPrice().add(i.getPrice()));
		return true;
	}
	
	public Ingredient removeIngredient(Ingredient i)
	{
		if(ingredients.contains(i))
		{
			ingredients.remove(i);
			this.setPrice(getPrice().subtract(i.getPrice()));
			return i;
		}
		
		return null;
	}
	
	public List<Ingredient> getIngredients()
	{
		return ingredients;
	}

}
