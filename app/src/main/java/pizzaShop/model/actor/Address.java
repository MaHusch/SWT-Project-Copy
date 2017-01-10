package pizzaShop.model.actor;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class for representing an Address of a Person
 * 
 * @author Martin Huschenbett
 *
 */

@Entity
public class Address {//implements Serializable{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 2262194612205942085L;

	@Id
	@GeneratedValue
	private long AddressID;

	private String street;
	private String postcode;
	private String housenumber;
	private String local;

	/**
	 * empty Constructor for Spring
	 */
	public Address() {

	}

	/**
	 * Constructor
	 * 
	 * @param local
	 * @param postcode
	 * @param street
	 * @param housenumber
	 */
	public Address(String local, String postcode, String street, String housenumber) {
		this.local = local;
		this.postcode = postcode;
		this.street = street;
		this.housenumber = housenumber;

	}
	
	public long getID(){
		return this.AddressID;
	}

	/**
	 * getter for Street
	 * 
	 * @return street
	 */
	public String getStreet() {
		return this.street;
	}

	/**
	 * getter for Local
	 * 
	 * @return local
	 */
	public String getLocal() {
		return this.local;
	}

	/**
	 * getter for Housenumber
	 * 
	 * @return
	 */

	public String getHousenumber() {
		return this.housenumber;
	}

	/**
	 * getter for Postcode
	 * 
	 * @return postcode
	 */
	public String getPostcode() {
		return this.postcode;
	}

	/**
	 * setter for Street
	 * 
	 * @param street
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * Setter for Local
	 * 
	 * @param local
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * setter for Housenumber
	 * 
	 * @param housenumber
	 */
	public void setHousenumber(String housenumber) {
		this.housenumber = housenumber;
	}

	/**
	 * setter for Postcode
	 * 
	 * @param postcode
	 */

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * toString converts the elements of the Address into one String
	 */
	@Override
	public String toString() {

		return (postcode + " " + local + System.getProperty("line.separator") + street + " " + housenumber);
	}

}
