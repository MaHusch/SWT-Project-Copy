package kickstart.model;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountingMethods {
	
	private final Accountancy accountancy;
	private final BusinessTime businessTime;

	@Autowired
	public AccountingMethods(Accountancy accountancy, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
	}
	
	public MonetaryAmount total(){
		MonetaryAmount total = Money.of(0, "EUR");
		
		for (AccountancyEntry a : accountancy.findAll())
		        total = total.add(a.getValue());
		
		return total;
	}
	
	
	
	
}
