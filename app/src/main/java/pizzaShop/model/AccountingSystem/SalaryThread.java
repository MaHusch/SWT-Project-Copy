package pizzaShop.model.AccountingSystem;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;

import pizzaShop.model.AccountSystem.CustomerHelper;
import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.ManagementSystem.Store;

public class SalaryThread implements Runnable {

	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final Store store;
	private final CustomerHelper customerHelper;

	// @Autowired
	public SalaryThread(Accountancy accountancy, BusinessTime businessTime, Store store,
			CustomerHelper customerHelper) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.store = store;
		this.customerHelper = customerHelper;

	}

	public void run() {
		LocalDateTime currentDate = businessTime.getTime();
		long delay = 3;
		long next = System.currentTimeMillis();
		int EVERY_THIRD_TIME = 0;
		while (true) {
			if (EVERY_THIRD_TIME == 2) {
				try {
					customerHelper.checkCutleries();
				} catch (Exception e) {
					break;
				}

				EVERY_THIRD_TIME = 0;
			} else {
				EVERY_THIRD_TIME++;
			}
			LocalDateTime newDate = businessTime.getTime();
			boolean TIME_PASSED = newDate.isAfter(currentDate);
			boolean MONTH_CHANGED = newDate.getMonth() != currentDate.getMonth();
			boolean YEAR_CHANGED = newDate.getYear() != currentDate.getYear();

			if (TIME_PASSED && (MONTH_CHANGED | YEAR_CHANGED)) {
				
				int dateDiff = (newDate.getYear() - currentDate.getYear()) * 12 + newDate.getMonthValue()
						- currentDate.getMonthValue();

	
				for (int i = 0; i < dateDiff; i++) {
					for(StaffMember s : store.getStaffMemberList()){
						String msg = "Gehalt: " + s.getPerson().getForename() 
												+ " ("	+ s.getRole().toString().substring(5) + ") für "
												+ Month.of((currentDate.getMonthValue() + i - 1) % 12 + 1)
												.getDisplayName(TextStyle.FULL, Locale.GERMAN);
						
						accountancy.add(new AccountancyEntry(s.getSalary().multiply(-1),msg));
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
