package pizzaShop.model.actor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import pizzaShop.model.catalog_item.Cutlery;

@Entity
public class Customer extends Person {

	private Cutlery myCutlery = null; 
										
	private @Id @GeneratedValue long customerID;
	
	private String surname;
	private String forename; 
	private String telephoneNumber;
	@OneToOne(cascade = {CascadeType.ALL})
	private Address address;
	
	
	public Customer() {
		
	}

	public Customer(String surname ,String forename, String telephoneNumber, String local, String postcode, String street, String housenumber) {
		this.surname = surname;
		this.forename = forename; 
		this.telephoneNumber = telephoneNumber;
		this.address = new Address(local,postcode,street,housenumber);
		//super(surname,forename,telephoneNumber);
		// TODO Auto-generated constructor stub
	}

	public Cutlery getMyCutlery() {
		return myCutlery;
	}
	
	public void setMyCutlery(Cutlery myCutlery) {
		this.myCutlery = myCutlery;
	}
	
	public long getId(){
		return customerID;
	}
	

	/* Vererbung ist rein symbolischer Natur da sonst probleme mit dem Repository auftreten;
	 * Repository eventuell durch List/Map ersetzen
	 */
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	
	
	
}
