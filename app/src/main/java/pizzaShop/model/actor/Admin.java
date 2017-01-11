package pizzaShop.model.actor;

import static org.salespointframework.core.Currencies.EURO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.javamoney.moneta.Money;
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
		salary = Money.of(400, EURO);
	}
	
}
