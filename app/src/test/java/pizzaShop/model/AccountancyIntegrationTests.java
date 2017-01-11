package pizzaShop.model;

import static org.junit.Assert.*;
import static org.salespointframework.core.Currencies.EURO;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.AccountingSystem.AccountingHelper;
import pizzaShop.model.ManagementSystem.Store;

public class AccountancyIntegrationTests extends AbstractIntegrationTests {
	
	@Autowired Accountancy accountancy;
	@Autowired BusinessTime time;
	@Autowired Store store;
	AccountingHelper helper;
	
	@Before
	public void setUp()
	{
		helper = new AccountingHelper(accountancy, time, store);
	}
	
	@Test
	public void totalTest() {
		MonetaryAmount total = helper.total();
		MonetaryAmount test_total = Money.of(0,EURO);
		String description = "testEntry";
		MonetaryAmount price = Money.of(400.44, EURO);
		
		for(AccountancyEntry ac : accountancy.findAll())
		{
			test_total = test_total.add(ac.getValue());
		}
		
		assertTrue(test_total.equals(total));
		
		AccountancyEntry a = new AccountancyEntry(price, description);
		accountancy.add(a);
		MonetaryAmount new_total = total.add(price);
		assertTrue(helper.total().equals(new_total));
	} 
	
}
