package pizzaShop.model.store;

import javax.money.MonetaryAmount;

import static org.salespointframework.core.Currencies.EURO;
import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountingHelper {
	
	private final Accountancy accountancy;
	private final BusinessTime businessTime;

	@Autowired
	public AccountingHelper(Accountancy accountancy, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
	}
	
	public MonetaryAmount total(){
		MonetaryAmount total = Money.of(0, EURO);
		
		for (AccountancyEntry a : accountancy.findAll())
		        total = total.add(a.getValue());
		
		return total;
	}
	
	public MonetaryAmount intervalTotal(Interval i){
		MonetaryAmount total = Money.of(0, EURO);
		
		for (AccountancyEntry a : accountancy.find(i))
		        total = total.add(a.getValue());
		
		return total;
	}
	
	
	
	
}
