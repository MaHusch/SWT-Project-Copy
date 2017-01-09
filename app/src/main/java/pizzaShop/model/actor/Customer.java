package pizzaShop.model.actor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import pizzaShop.model.catalog_item.Cutlery;

/**
 * CustomerClass for representing a Customer of the PizzaShop
 * 
 * @author Martin Huschenbett
 *
 */

@Entity
public class Customer {

	/**
	 * Cutlery of the Customer
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private Cutlery myCutlery = null;

	@OneToOne(cascade = CascadeType.ALL)
	private Person myPerson = null;

	private @Id @GeneratedValue long customerID;

	/**
	 * unused constructor for Spring
	 */
	public Customer() {

	}

	/**
	 * Constructor
	 * 
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 * @param local
	 * @param postcode
	 * @param street
	 * @param housenumber
	 */

	public Customer(String surname, String forename, String telephoneNumber, String local, String postcode,
			String street, String housenumber) {
		this.myPerson = new Person(surname, forename, telephoneNumber,
				new Address(local, postcode, street, housenumber));

	}

	/**
	 * getter for myCutlery
	 * 
	 * @return myCutlery
	 */
	public Cutlery getCutlery() {
		return myCutlery;
	}

	/**
	 * setter for myCutlery
	 * 
	 * @param myCutlery
	 */

	public void setCutlery(Cutlery myCutlery) {
		this.myCutlery = myCutlery;
	}

	/**
	 * getter for CustomerId
	 * 
	 * @return
	 */
	public long getId() {
		return customerID;
	}

	public Person getPerson() {
		return this.myPerson;
	}

}
