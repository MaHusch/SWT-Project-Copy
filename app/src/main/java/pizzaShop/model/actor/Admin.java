package pizzaShop.model.actor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.store.Store;

	
	/**
	 * AdminClass for representing an Admin of the PizzaShop
	 * @author Martin Huschenbett
	 *
	 */


@Entity
public class Admin extends StaffMember{
	
	@Id @GeneratedValue private long employeeID;
	
	/**
	 * Constructor
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 */
	
	public Admin(String surname, String forename,String telephoneNumber){
		super(surname,forename,telephoneNumber);
	}
	
}
