package pizzaShop.model.actor;

/**
 * abstract PersonClass for every Person who is represented in PizzaShop
 * 
 * @author Martin Huschenbett
 *
 */

public abstract class Person {

	private String surname;
	private String forename;
	private String telephoneNumber;
	private Address address;

	/**
	 * empty constructor for Spring
	 */
	protected Person() {
	};

	/**
	 * Constructor
	 * 
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
		return address;
	}

	/**
	 * setter for Address
	 * 
	 * @param address
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

}
