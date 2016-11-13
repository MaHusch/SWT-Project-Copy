package kickstart.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import kickstart.Store;

@Entity
public class Admin extends StaffMember{
	
	@Id @GeneratedValue private long employeeID;
	
	public Admin(String surname, String forename,String telephoneNumber){
		super(surname,forename,telephoneNumber);
		super.updateUserAccount("admin", "123", Role.of("ROLE_ADMIN"));
	}
	
}
