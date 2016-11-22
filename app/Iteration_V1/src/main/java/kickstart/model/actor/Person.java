package kickstart.model.actor;

public class Person {
	
	private String surname;
	private String forename; 
	private String telephoneNumber; 
	private String address;
	
	public Person(String surname, String forename,String telephoneNumber){
		this.surname = surname;
		this.forename = forename; 
		this.telephoneNumber = telephoneNumber;
	}

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
