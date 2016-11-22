package pizzaShop.model.store;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;

public class SalaryThread implements Runnable {

	private final Accountancy accountancy;
	private final BusinessTime businessTime;

	// @Autowired
	public SalaryThread(Accountancy accountancy, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.businessTime = businessTime;
	}

	public void run() {
		LocalDateTime currentDate = businessTime.getTime();
		long delay = 5;
		long next = System.currentTimeMillis();
		while (true) {
			LocalDateTime newDate = businessTime.getTime();
			if (newDate.isAfter(currentDate) && (newDate.getMonth() != currentDate.getMonth() | newDate.getYear() != currentDate.getYear())) {
				int dateDiff = (newDate.getYear() - currentDate.getYear()) * 12 + newDate.getMonthValue() - currentDate.getMonthValue();

				for (int i = 0; i < dateDiff; i++) {
					accountancy.add(new AccountancyEntry(Money.of(-300, "EUR"), "Gehalt für " + Month.of((currentDate.getMonthValue() + i - 1) % 12 + 1).getDisplayName(TextStyle.FULL, Locale.GERMAN)));
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
