package model;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class Drink extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 138763215192867688L;

	public Drink(String name, MonetaryAmount price) {
		super(name, price);
	}

}
