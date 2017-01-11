package pizzaShop.model.AccountSystem;

import javax.money.MonetaryAmount;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

/**
 * abstract StaffMemberClass for representing the StaffMembers of the PizzaShop
 * 
 * @author Martin Huschenbett
 *
 */
@Entity
public abstract class StaffMember{

	@Id
	@GeneratedValue
	private long employeeID;

	private String username;
	private String password;
	private UserAccount userAccount;
	private Role role;
	protected MonetaryAmount salary;
	
	@OneToOne(cascade = CascadeType.ALL) private Person myPerson = null;

	/**
	 * Constructor
	 * 
	 * @param surname
	 * @param forename
	 * @param telephoneNumber
	 */

	public StaffMember(String surname, String forename, String telephoneNumber) {

		myPerson = new Person(surname,forename,telephoneNumber);

		// Store.staffMemberList.add(this); now in Controller

	}

	/**
	 * getter for Username
	 * 
	 * @return username
	 */
	public String getUsername() {

		return this.username;
	}
	
	public Person getPerson()
	{
		return this.myPerson;
	}
	
	public MonetaryAmount getSalary(){
		return salary;
	}
	
	public MonetaryAmount setSalary(MonetaryAmount s){
		return salary = s;
	}

	/*
	 * public void updateUserAccount(String username, String password, Role
	 * role) {
	 * 
	 * if (this.userAccount == null) { setUsername(username);
	 * setPassword(password); setRole(role);
	 * 
	 * setUserAccount(Store.employeeAccountManager.create(this.username,
	 * this.password, this.role));
	 * Store.employeeAccountManager.save(this.userAccount); } else { //
	 * updateUserAccount
	 * 
	 * }
	 * 
	 * }
	 */

	/**
	 * setter for UserAccount
	 * 
	 * @param userAccount
	 */
	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	/**
	 * setter for Username
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * setter for password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return this.password;
	}

	/**
	 * setter for the Role
	 * 
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * getter for the Role
	 * 
	 * @return role
	 */
	public Role getRole() {
		return this.role;
	}

	/**
	 * getter for the EmployeeID
	 * 
	 * @return employeeID
	 */
	public long getId() {
		System.out.println(this.employeeID);
		return employeeID;
	}

	/**
	 * getter for the UserAccount
	 * 
	 * @return userAccount
	 */
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

}
