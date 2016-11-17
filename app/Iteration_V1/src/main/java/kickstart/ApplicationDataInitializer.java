package kickstart;

import java.time.Duration;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kickstart.model.store.SalaryThread;

@Component
public class ApplicationDataInitializer implements DataInitializer {

	private final Accountancy accountancy;
	private final UserAccountManager userAccountManager;
	private final BusinessTime businessTime;

	@Autowired
	public ApplicationDataInitializer(Accountancy accountancy, UserAccountManager userAccountManager,
			BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.userAccountManager = userAccountManager;
		this.businessTime = businessTime;
	}

	@Override
	public void initialize() {

		AccountancyEntry ace1 = new AccountancyEntry(Money.of(50, "EUR"), "Einkauf");
		AccountancyEntry ace2 = new AccountancyEntry(Money.of(-200, "EUR"), "Diebstahl");
		AccountancyEntry ace3 = new AccountancyEntry(Money.of(536, "EUR"), "Großbestellung");
		accountancy.add(ace1);
		accountancy.add(ace2);
		accountancy.add(ace3);

		UserAccount ua = userAccountManager.create("Hans", "123", Role.of("ROLE_SELLER"));

		(new Thread(new SalaryThread(accountancy, businessTime))).start();

		// TODO Auto-generated method stub

	}

}
