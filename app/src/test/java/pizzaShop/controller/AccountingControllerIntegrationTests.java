package pizzaShop.controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.accountancy.Accountancy;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;

public class AccountingControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired AccountingController controller;
	@Autowired Accountancy accountancy;
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
