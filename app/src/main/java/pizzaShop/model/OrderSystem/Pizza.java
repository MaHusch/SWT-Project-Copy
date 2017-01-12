package pizzaShop.model.OrderSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderIdentifier;

/**
 * PizzaClass for representing a pizza of the PizzaShop
 * 
 * @author Florentin Dörre
 */
@Entity
public class Pizza extends Item {

	private static final long serialVersionUID = 4746830994439574139L;
	@ElementCollection
	private List<String> ingredients;

	private boolean isFinished;

	@SuppressWarnings("unused")

	private Pizza() {
	}

	/**
	 * @see Item
	 * @param name
	 *            equally to the name at the item class
	 * @param price
	 *            equally to the name at the item class sets the ItemType to
	 *            "PIZZA" creates an empty array list for ingredients at the
	 *            beginning a pizza is
	 */
	public Pizza(String name, javax.money.MonetaryAmount price) {
		super(name, price, ItemType.PIZZA);
		this.ingredients = new ArrayList<String>();
		// this.orderQueue =
		this.setStatus(false);
	}

	/**
	 * 
	 * @param status
	 *            <CODE>true</CODE> after pizza was baked
	 * 
	 */
	public void setStatus(boolean status) {

		isFinished = status;

	}

	/**
	 * saves the name of the ingredient in the ingredients list
	 * 
	 * @param i
	 *            ingredient which will be put on the pizza
	 * @return <CODE>false</CODE> if ingredient already on the pizza
	 *         <CODE>true</CODE> if successfully put on the pizza
	 */
	public boolean addIngredient(Ingredient i) // change name ?
	{
		if (ingredients.contains(i.getName()))
			return false;

		ingredients.add(i.getName());
		Collections.sort(ingredients);
		this.setPrice(getPrice().add(i.getPrice()));
		return true;
	}

	public void addIngredient(List<Ingredient> newIngredients) {
		for (Ingredient i : newIngredients) {
			this.addIngredient(i);
		}
	}

	/**
	 * 
	 * @param i
	 *            ingredient which will be removed from the pizza
	 * @return <CODE>null</CODE> if the ingredient wasnt on the pizza otherwise
	 *         returns the name of the removed ingredient
	 */
	public String removeIngredient(Ingredient i) {
		if (ingredients.contains(i.getName())) {
			ingredients.remove(i.getName());
			this.setPrice(this.getPrice().subtract(i.getPrice()));
			return i.getName();
		}

		return null;
	}

	/**
	 * 
	 * @return a List of Strings containing the names of the ingredients on the
	 *         pizza
	 */
	public List<String> getIngredients() {
		return ingredients;
	}

	public boolean getStatus() {
		return isFinished;
	}

	/**
	 * to save the order(as a string) containing the pizza
	 * 
	 * @param o
	 *            OrderIdentifier of a order which should contain the pizza
	 */
	

	/**
	 * @return returns the Pizza(ingriedientnames)
	 * @see pizzaShop.model.OrderSystem.Item#toString()
	 */
	public String toString() {
		String result = super.toString();
		List<String> i = ingredients;

		if (i.size() > 0) { 
			result += " (" + i.get(0);

			for (int n = 1; n < i.size(); n++) {
				result += "," + i.get(n);
				if((i.size() > n+1) && ((n+1) % 3 == 0))
					result += System.getProperty("line.separator");
			}

			result += ") ";
		}

		return result;
	}
}
