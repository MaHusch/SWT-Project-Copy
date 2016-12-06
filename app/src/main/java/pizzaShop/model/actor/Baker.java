package pizzaShop.model.actor;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;

import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.Oven;
import pizzaShop.model.store.Pizzaqueue;
import pizzaShop.model.store.Store;

/**
 * BakerClass for representing a Baker of the PizzaShop
 * @author Martin Huschenbett
 *
 */


@Entity
public class Baker extends StaffMember {

	@Id
	@GeneratedValue
	private long employeeID;

	/**
	 * Constructor
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 */
	public Baker(String surname, String forename, String telephoneNumber) {
		super(surname, forename, telephoneNumber);
		//myOvens = new ArrayList<Oven>();
	}

}
