package kickstart;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.OrderManager;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class ApplicationDataInitializer implements DataInitializer{
	
	private final Accountancy accountancy;
	//private final OrderManager orderManager;
	//private final UserAccountManager userAccountManager;

	@Autowired
	public ApplicationDataInitializer(Accountancy accountancy, OrderManager orderManager, UserAccountManager userAccountManager) {
		this.accountancy = accountancy;
		//this.orderManager = orderManager;
		//this.userAccountManager = userAccountManager;
		
	}

	@Override
	public void initialize() {

		//final Role sellerRole = Role.of("ROLE_SELLER");
		//UserAccount us = userAccountManager.create("peter", "123", sellerRole);
		AccountancyEntry ace1 = new AccountancyEntry(Money.of(50, "EUR"), "Einkauf");
		AccountancyEntry ace2 = new AccountancyEntry(Money.of(-200, "EUR"), "Diebstahl");
		AccountancyEntry ace3 = new AccountancyEntry(Money.of(536, "EUR"), "Großbestellung");
		accountancy.add(ace1);
		accountancy.add(ace2);
		accountancy.add(ace3);
		
		
		
		
		//System.out.println(accountancy.get(ace1.getId()).get().getValue().getNumber());
		
		
		
		
		// TODO Auto-generated method stub
		
	}

}





	