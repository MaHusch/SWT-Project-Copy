package pizzaShop.model.AccountingSystem;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Iterator;
import java.util.Locale;

import javax.money.MonetaryAmount;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;

import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.ManagementSystem.Store;


public class SalaryThread implements Runnable {

	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final Store store;
	

	// @Autowired
	public SalaryThread(Accountancy accountancy, BusinessTime businessTime, Store store) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.store = store;
		
	}

	public void run() {
		LocalDateTime currentDate = businessTime.getTime();
		long delay = 3;
		long next = System.currentTimeMillis();
		while (true) {
			LocalDateTime newDate = businessTime.getTime();
			if (newDate.isAfter(currentDate) && (newDate.getMonth() != currentDate.getMonth() | newDate.getYear() != currentDate.getYear())) {
				int dateDiff = (newDate.getYear() - currentDate.getYear()) * 12 + newDate.getMonthValue() - currentDate.getMonthValue();

				
				MonetaryAmount currentIncome;
				StaffMember currentStaff;
				for (int i = 0; i < dateDiff; i++) {
					Iterator<StaffMember> staffIterator = store.getStaffMemberList().iterator();
					while(staffIterator.hasNext()){
						currentStaff = staffIterator.next();
						currentIncome = currentStaff.getSalary();
						accountancy.add(new AccountancyEntry(currentIncome.multiply(-1), "Gehalt: "+currentStaff.getPerson().getForename() +" ("+currentStaff.getRole().toString().substring(5)+") für " + Month.of((currentDate.getMonthValue() + i - 1) % 12 + 1).getDisplayName(TextStyle.FULL, Locale.GERMAN)));
						
					}
					
					
				}
				currentDate = newDate;
			}
			next += delay;
			long sleep = next - System.currentTimeMillis();
			if (sleep > 0)
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		}

	}

}
