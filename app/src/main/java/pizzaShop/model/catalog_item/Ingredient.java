package pizzaShop.model.catalog_item;

import javax.persistence.Entity;

/**
 * IngredientClass for representing an Ingredient of a Pizza
 * 
 * @author Florentin Dörre
 */
@Entity
public class Ingredient extends Item {

	private static final long serialVersionUID = -229325384666805584L;

	/**
	 * unused Constructor
	 */
	@SuppressWarnings("unused")
	private Ingredient() {
	}

	/**
	 * Constructor
	 * @param name
	 * @param price
	 */
	public Ingredient(String name, javax.money.MonetaryAmount price) {
		super(name, price, ItemType.INGREDIENT);
	}

}
