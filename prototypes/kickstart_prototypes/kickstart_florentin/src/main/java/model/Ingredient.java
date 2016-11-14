package model;

import javax.persistence.Entity;

@Entity
public class Ingredient extends Item {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -229325384666805584L;

	public Ingredient(String name, javax.money.MonetaryAmount price)
	{
		super(name,price);
	}

}
