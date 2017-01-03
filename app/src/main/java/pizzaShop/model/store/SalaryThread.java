package pizzaShop.model.store;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;

import pizzaShop.model.actor.StaffMember;

public class SalaryThread implements Runnable {

	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final Store store;
	private final HashMap<Role, Integer> incomeMap;

	// @Autowired
	public SalaryThread(Accountancy accountancy, BusinessTime businessTime, Store store, HashMap<Role, Integer> incomeMap) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.incomeMap = incomeMap;
		this.store = store;
	}

	public void run() {
		LocalDateTime currentDate = businessTime.getTime();
		long delay = 5;
		long next = System.currentTimeMillis();
		while (true) {
			LocalDateTime newDate = businessTime.getTime();
			if (newDate.isAfter(currentDate) && (newDate.getMonth() != currentDate.getMonth() | newDate.getYear() != currentDate.getYear())) {
				int dateDiff = (newDate.getYear() - currentDate.getYear()) * 12 + newDate.getMonthValue() - currentDate.getMonthValue();

				
				int currentIncome;
				StaffMember currentStaff;
				for (int i = 0; i < dateDiff; i++) {
					Iterator<StaffMember> staffIterator = store.getStaffMemberList().iterator();
					while(staffIterator.hasNext()){
						currentStaff = staffIterator.next();
						currentIncome = incomeMap.get(currentStaff.getRole());
						accountancy.add(new AccountancyEntry(Money.of(currentIncome*-1, "EUR"), "Gehalt: "+currentStaff.getForename() +" ("+currentStaff.getRole().toString().substring(5)+") für " + Month.of((currentDate.getMonthValue() + i - 1) % 12 + 1).getDisplayName(TextStyle.FULL, Locale.GERMAN)));
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
