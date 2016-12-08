package pizzaShop.model.catalog_item;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import static org.salespointframework.core.Currencies.*;

/**
 * ItemClass for representing an Item in the ItemCatalog
 * 
 * @author Florentin Dörre
 * 
 */
@Entity
public class Item extends Product {

	ItemType type;

	private static final long serialVersionUID = -8534543519430874036L;

	/**
	 * unused Constructor for Spring
	 */
	@SuppressWarnings("deprecation")
	public Item() {
	}

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the product
	 * @param price
	 *            the price of the product
	 * @param type
	 *            describes what kind of product this is
	 */
	public Item(String name, javax.money.MonetaryAmount price, ItemType type) {
		// test Arguments (empty or false)
		super(name, price);
		this.type = type;
		if (type == ItemType.FREEDRINK)
			this.setPrice(Money.of(0.0, EURO));
	}

	// TODO: construktor for freedrink? (no price needed)

	/**
	 * changes the type of the product
	 * 
	 * @param type
	 *            type
	 */
	public void setType(ItemType type) {
		this.type = type;
	}

	/**
	 * getter for ItemType
	 * 
	 * @return returns the ItemType of the Product
	 */
	public ItemType getType() {
		return type;
	}

	/**
	 * function for a better output in the frontend
	 * 
	 * @return gives the german translation
	 */
	public String toString() // for catalog template
	{
		switch (type) {
		default:
			return "Getränk";
		case FREEDRINK:
			return "Freigetränk";
		case INGREDIENT:
			return "Zutat";
		case SALAD:
			return "Salad";
		case PIZZA:
			return "Pizza";
		case CUTLERY:
			return "Essgarnitur";
		}
	}

}
