package model;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

@Entity
public class Salad extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1212237042230448941L;

	public Salad(String name, MonetaryAmount price) {
		super(name, price);
		// TODO Auto-generated constructor stub
	}

}
