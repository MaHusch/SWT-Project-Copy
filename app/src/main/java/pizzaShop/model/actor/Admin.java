package pizzaShop.model.actor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.store.Store;

@Entity
public class Admin extends StaffMember{
	
	@Id @GeneratedValue private long employeeID;
	
	public Admin(String surname, String forename,String telephoneNumber){
		super(surname,forename,telephoneNumber);
	}
	
}
