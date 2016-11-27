package pizzaShop.model.actor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer extends Person {

	private boolean myCutlery = false; // temporärer Workaround weil Klasse
										// Cutlery nicht existiert
	private @Id @GeneratedValue long customerID;

	private String surname;
	private String forename; 
	private String telephoneNumber; 
	private String address;
	
	public Customer() {
		
	}

	public Customer(String surname, String forename, String telephoneNumber) {
		this.surname = surname;
		this.forename = forename; 
		this.telephoneNumber = telephoneNumber;
		//super(surname,forename,telephoneNumber);
		// TODO Auto-generated constructor stub
	}

	public boolean getMyCutlery() {
		return myCutlery;
	}

	public void setMyCutlery(boolean myCutlery) {
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
	
}
