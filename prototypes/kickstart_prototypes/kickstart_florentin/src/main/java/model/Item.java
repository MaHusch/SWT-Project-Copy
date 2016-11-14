package model;

import javax.persistence.Entity;

import org.salespointframework.catalog.Product;

@Entity
public class Item extends Product {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8534543519430874036L;

	public Item(String name, javax.money.MonetaryAmount price)
	{
		super(name,price);
		
	}
	
}
