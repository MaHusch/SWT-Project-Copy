package pizzaShop.model.AccountSystem;

import static org.salespointframework.core.Currencies.EURO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.javamoney.moneta.Money;

/**
 * SellerClass for representing a Seller of the PizzaShop
 * 
 * @author Martin Huschenbett
 *
 */
@Entity
public class Seller extends StaffMember {

	@Id
	@GeneratedValue
	private long employeeID;

	/**
	 * Constructor
	 * 
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 */
	public Seller(String surname, String forename, String telephoneNumber) {
		super(surname, forename, telephoneNumber);
		salary = Money.of(300, EURO);
	}

}
