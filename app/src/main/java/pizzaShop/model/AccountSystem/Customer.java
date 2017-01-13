package pizzaShop.model.AccountSystem;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import pizzaShop.model.OrderSystem.Cutlery;

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

	public Customer(String surname, String forname, String telephoneNumber, String local, String postcode,
			String street, String streetnumber) {

		Address a1 = new Address(local, postcode, street, streetnumber);
		Person p1 = new Person(surname, forname, telephoneNumber, a1);

		this.myPerson = p1;
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
