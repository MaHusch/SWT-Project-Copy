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
public class Customer extends Person {

	/**
	 * Cutlery of the Customer
	 */
	private Cutlery myCutlery = null;

	private @Id @GeneratedValue long customerID;

	private String surname;
	private String forename;
	private String telephoneNumber;
	@OneToOne(cascade = { CascadeType.ALL })
	private Address address;

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
		this.surname = surname;
		this.forename = forename;
		this.telephoneNumber = telephoneNumber;
		this.address = new Address(local, postcode, street, housenumber);
		// super(surname,forename,telephoneNumber);
		// TODO Auto-generated constructor stub
	}

	/**
	 * getter for myCutlery
	 * @return myCutlery
	 */
	public Cutlery getMyCutlery() {
		return myCutlery;
	}
	/**
	 * setter for myCutlery
	 * @param myCutlery
	 */
	
	public void setMyCutlery(Cutlery myCutlery) {
		this.myCutlery = myCutlery;
	}
	
	/**
	 * getter for CustomerId
	 * @return
	 */
	public long getId() {
		return customerID;
	}

	/*
	 * Vererbung ist rein symbolischer Natur da sonst probleme mit dem
	 * Repository auftreten; Repository eventuell durch List/Map ersetzen
	 */

	/** 
	 * getter ofr Telephonenumber
	 * @return telephoneNumber
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	/**
	 * setter for Telephonenumber
	 * @param telephoneNumber
	 */
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * getter for Forename
	 * @return forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * setter for Forename
	 * @param forename
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * getter for Surname
	 * @return surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * setter for Surname
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * getter for Address
	 * @return address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * setter for Address
	 * @param address
	 */

	public void setAddress(Address address) {
		this.address = address;
	}

}
