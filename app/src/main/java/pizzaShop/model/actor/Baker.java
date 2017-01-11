package pizzaShop.model.actor;

import static org.salespointframework.core.Currencies.EURO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.javamoney.moneta.Money;

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
		salary = Money.of(250, EURO);
		//myOvens = new ArrayList<Oven>();
	}

}
