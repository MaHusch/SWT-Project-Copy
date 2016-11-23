package pizzaShop.model.actor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.store.Store;

@Entity
public class Seller extends StaffMember{
	
	@Id @GeneratedValue private long employeeID;
	
	public Seller(String surname, String forename,String telephoneNumber, String username, String password){
		super(surname,forename,telephoneNumber);
		this.updateUserAccount(username, password, Role.of("ROLE_SELler"));
	} 
	
	public Seller(String surname, String forename,String telephoneNumber){
		super(surname,forename,telephoneNumber);
	}
	
}
