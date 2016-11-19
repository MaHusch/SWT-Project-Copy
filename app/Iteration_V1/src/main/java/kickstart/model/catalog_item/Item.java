package kickstart.model.catalog_item;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import static org.salespointframework.core.Currencies.*;

@Entity
public class Item extends Product {

	/**
	 * 
	 */
	
	ItemType type;
	
	private static final long serialVersionUID = -8534543519430874036L;

	@SuppressWarnings("unused")	
	public Item(){}
	
	public Item(String name, javax.money.MonetaryAmount price,ItemType type)
	{
		super(name,price);
		this.type = type;
		
		if (type == ItemType.FREEDRINK) this.setPrice(Money.of(0.0, EURO));
	}
	
	public void setType(ItemType type)
	{
		this.type = type;
	}
	
	public ItemType getType()
	{
		return type;
	}
	
}
