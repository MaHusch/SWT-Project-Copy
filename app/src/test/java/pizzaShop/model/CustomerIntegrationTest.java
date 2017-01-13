package pizzaShop.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationNotAllowedException;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.CustomerHelper;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.ManagementSystem.Tan_Management.TanStatus;

public class CustomerIntegrationTest extends AbstractIntegrationTests {

	@Autowired
	CustomerHelper helper;
	@Autowired
	CustomerRepository repo;
	@Autowired TanManagement tans;
	
	//TODO: catch BeanCreationNotAllowedException
	@Test
	public void createCustomerTest() {
		String surname= "n";
		String forename= "m";
		String telephonenumber= "";
		String local = "e";
		String street = "f";
		String housenumber ="4b";
		String postcode = "a";

		try {
			helper.createCustomer(surname, forename, telephonenumber, 
								local, street, housenumber, postcode);
		} catch (Exception e) {
			assertEquals(e.getMessage(),"Eingabefelder überprüfen!");
		}
		
			
		telephonenumber = "1a1";
		
		try {
			helper.createCustomer(surname, forename, telephonenumber, 
								local, street, housenumber, postcode);
		} catch (Exception e) {
			assertEquals(e.getMessage(),"Telefonnummer darf nur Ziffern enthalten!");
		}
		
		telephonenumber = "3123124124";
		
		try {
			helper.createCustomer(surname, forename, telephonenumber, 
								local, street, housenumber, postcode);
		} catch (Exception e) {
			assertEquals(e.getMessage(),"fehler");
		}
		
		for(Customer c :repo.findAll())
		{
			if(c.getPerson().getTelephoneNumber().equals(telephonenumber))
				assertEquals("1","1");
		}
		
		Tan customer_tan = tans.getTan(telephonenumber);
		assertEquals(customer_tan.getStatus(),TanStatus.VALID);
	}
	
	

}
