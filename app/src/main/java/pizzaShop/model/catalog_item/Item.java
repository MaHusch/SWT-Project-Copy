package pizzaShop.model.catalog_item;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import static org.salespointframework.core.Currencies.*;

/**
 * @author Florentin
 * Class for Items which can be put in the ItemCatalog
 */
@Entity
public class Item extends Product {

	
	
	ItemType type;
	
	private static final long serialVersionUID = -8534543519430874036L;

	
	@SuppressWarnings("deprecation")
	public Item(){}
	
	/**
	 * 
	 * @param name the name of the product
	 * @param price the price of the product
	 * @param type  describes what kind of product this is 
	 */
	public Item(String name, javax.money.MonetaryAmount price,ItemType type) 
	{
		// test Arguments (empty or false)
		super(name,price);
		this.type = type;
		if (type == ItemType.FREEDRINK) this.setPrice(Money.of(0.0, EURO));
	}
	
	// TODO: construktor for freedrink? (no price needed)
	
	/**
	 * changes the type of the product 
	 * @param type type 
	 */
	public void setType(ItemType type)
	{
		this.type = type;
	}
	
	/**
	 * 
	 * @return returns the ItemType of the Product
	 */
	public ItemType getType()
	{
		return type;
	}
	
	/**
	 * function for a better output in the frontend
	 * @return gives the german translation 
	 */
	public String toString() //for catalog template
	{
		switch(type) 
		{
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
