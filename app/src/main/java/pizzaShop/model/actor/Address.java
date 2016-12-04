package pizzaShop.model.actor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address {
	
	@Id @GeneratedValue private long AddressID;
	
	private String street;
	private String postcode;
	private String housenumber;
	private String local;
	
	public Address()
	{
		
	}

	public Address(String local, String postcode, String street, String housenumber)
	{
		this.local = local;
		this.postcode = postcode;
		this.street = street;
		this.housenumber = housenumber;
		
	}
	
	public String getStreet(){return this.street;}
	public String getLocal(){return this.local;}
	public String getHousenumber(){return this.housenumber;}
	public String getPostcode(){return this.postcode;}
	
	public void setStreet(String street){this.street = street;}
	public void setLocal(String local){this.local = local;}
	public void setHousenumber(String housenumber){this.housenumber = housenumber;}
	public void setPostcode(String postcode){this.postcode = postcode;}
	
}
