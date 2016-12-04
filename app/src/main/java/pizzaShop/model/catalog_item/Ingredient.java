package pizzaShop.model.catalog_item;

import javax.persistence.Entity;

/**
 * Item for better checking if added Item to a pizza is an ingredient
 * @author Florentin
 *
 */
@Entity
public class Ingredient extends Item {
	
	private static final long serialVersionUID = -229325384666805584L;
	
	
	@SuppressWarnings("unused")
	private Ingredient(){}
	
	public Ingredient(String name, javax.money.MonetaryAmount price)
	{
		super(name,price,ItemType.INGREDIENT);
	}
	
	
}
