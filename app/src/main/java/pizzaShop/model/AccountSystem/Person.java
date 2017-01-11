package pizzaShop.model.AccountSystem;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * PersonClass for every Person (via Composite) who is represented in PizzaShop
 * 
 * @author Martin Huschenbett
 *
 */

@Entity
public class Person {

	private String surname;
	private String forename;
	private String telephoneNumber;
	@OneToOne(cascade = { CascadeType.ALL }) private Address address;
	@Id @GeneratedValue private long PersonID;

	/**
	 * empty constructor for Spring
	 */
	
	@SuppressWarnings("unused")
	public Person() {
	};

	/**
	 * Constructor
	 * 
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 */

	public Person(String surname, String forename, String telephoneNumber, Address address) {
		this.surname = surname;
		this.forename = forename;
		this.telephoneNumber = telephoneNumber;
		this.address = address;
	}

	/**
	 * constructor for staffmember ... no address?! 
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 */
	public Person(String surname, String forename, String telephoneNumber) {
		this.surname = surname;
		this.forename = forename;
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * getter for Telephonenumber
	 * 
	 * @return telephoneNumber
	 */
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	/**
	 * setter for Telephonenumber
	 * 
	 * @param telephoneNumber
	 */

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	/**
	 * getter for Forename
	 * 
	 * @return forename
	 */
	public String getForename() {
		return forename;
	}

	/**
	 * setter for Forename
	 * 
	 * @param forename
	 */
	public void setForename(String forename) {
		this.forename = forename;
	}

	/**
	 * getter for Surname
	 * 
	 * @return surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * setter for Surname
	 * 
	 * @param surname
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * getter for Address
	 * 
	 * @return address
	 */
	public Address getAddress() {
		return this.address;
	}

	/**
	 * setter for Address
	 * 
	 * @param address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public String toString(){
		
		return (this.forename + " " + this.surname + System.getProperty("line.separator") + this.address.toString());
	}

}
