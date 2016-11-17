package kickstart.model.catalog_item;

import javax.persistence.Entity;

import org.salespointframework.catalog.Product;

@Entity
public class Item extends Product {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8534543519430874036L;

	@SuppressWarnings("unused")	
	public Item(){}
	
	public Item(String name, javax.money.MonetaryAmount price)
	{
		super(name,price);
		
	}
	
	
}
