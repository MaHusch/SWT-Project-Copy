package pizzaShop.model.actor;

import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import pizzaShop.model.catalog.Cutlery;
import pizzaShop.model.store.AddressRepository;


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

	private ArrayList<Long> deliveryAddressesIDs = new ArrayList<Long>();
	private ArrayList<String> deliveryAddressesStrings = new ArrayList<String>();

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

	
	public Customer(Person person) {

		this.myPerson = person;

		this.deliveryAddressesIDs.add(this.myPerson.getAddress().getID());
		this.deliveryAddressesStrings.add(this.myPerson.getAddress().toString());

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
	
	public ArrayList<Long> getDeliveryAddressesIDs() {
		return this.deliveryAddressesIDs;
	}
	
	public ArrayList<String> getDeliveryAddressesStrings() {
		return this.deliveryAddressesStrings;
	}
	
	public boolean addDeliveryAddress(Address newAddress) {
		this.deliveryAddressesStrings.add(newAddress.toString());
		return this.deliveryAddressesIDs.add(newAddress.getID());
	}
	
	/*public String getDeliveryAddressesString() {
		String completeString = "";
		
		Iterator<String> addresseIterator = this.deliveryAddressesStrings.iterator();
		
		while(addresseIterator.hasNext())
		{
				completeString += addresseIterator.next() + " / " +System.getProperty("line.separator");
		}
		
		return completeString;
	}*/
}
