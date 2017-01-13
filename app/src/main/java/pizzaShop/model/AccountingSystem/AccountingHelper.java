package pizzaShop.model.AccountingSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.ManagementSystem.Store;

@Component
public class AccountingHelper {
	
	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final Store store;

	@Autowired
	public AccountingHelper(Accountancy accountancy, BusinessTime businessTime, Store store) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.store = store;
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
	
	public void forward(int days) {
		//accountingMethods.
		
		int tDays = 0;
		int cDays = 0;
		LocalDateTime cTime = businessTime.getTime();
		while(tDays < days){
			int cMonth = cTime.getMonthValue(); 
			cTime = cTime.plusDays(1);
			tDays++;
			cDays++;
			if(cTime.getMonthValue() > cMonth){
				businessTime.forward(Duration.ofDays(cDays));
				cDays = 0;
				try {
					
					int AVERAGE_TIME_PER_STAFF = 15;
					Thread.sleep(store.getStaffMemberList().size()*AVERAGE_TIME_PER_STAFF);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(tDays == days){
				businessTime.forward(Duration.ofDays(cDays));
			}
		}
	}
	
	public LocalDateTime getFirstMonday(){
		LocalDateTime frstMon = businessTime.getTime();
		frstMon = frstMon.minusDays(frstMon.getDayOfYear() - 1);
		while (true) {
			if (frstMon.getDayOfWeek().equals(DayOfWeek.MONDAY))
				break;
			frstMon = frstMon.plusDays(1);
		}
		return frstMon;
	}
	
	
	
}
