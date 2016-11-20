package kickstart.model.actor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer extends Person{
	
	private boolean myCutlery = false; //temporärer Workaround weil Klasse Cutlery nicht existiert 
	@Id @GeneratedValue private long customerID;
	
	public Customer(String surname, String forename, String telephoneNumber) {
		super(surname, forename, telephoneNumber);
		// TODO Auto-generated constructor stub
	}

	public boolean getMyCutlery() {
		return myCutlery;
	}

	public void setMyCutlery(boolean myCutlery) {
		this.myCutlery = myCutlery;
	}
	
	
	
}
