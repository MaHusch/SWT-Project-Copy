package pizzaShop.model.AccountSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.util.Optional;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.ManagementSystem.Store;

@Component
public class StaffMemberHelper {
	private final UserAccountManager employeeAccountManager;
	private final Accountancy accountancy;
	private final Store store;

	@Autowired
	public StaffMemberHelper(Store store, UserAccountManager employeeAccountManager, Accountancy accountancy) {
		this.store = store;
		this.employeeAccountManager = employeeAccountManager;
		this.accountancy = accountancy;

	}

	public void registerStaffMember(String surname, String forename, String telephonenumber, String username,
			String password, String role) throws IllegalArgumentException {

		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || username.equals("")
				|| password.equals("") || role.equals("")) {
			throw new IllegalArgumentException("Eingabefelder überprüfen!");
		}

		String msg = store.validateTelephonenumber(telephonenumber, null);
		if (!msg.isEmpty()) {
			throw new IllegalArgumentException(msg);
		}

		StaffMember staffMember;

		switch (role) {
		case "Bäcker":
			role = "BAKER";
			staffMember = new Baker(surname, forename, telephonenumber);
			break;
		case "Lieferant":
			role = "DELIVERER";
			staffMember = new Deliverer(surname, forename, telephonenumber);
			break;

		default: // Seller ist bei HTML sowieso als default ausgewählt
			role = "SELLER";
			staffMember = new Seller(surname, forename, telephonenumber);
			break;
		}

		if (store.getStaffMemberByName(username) == null) {
			store.getStaffMemberList().add(staffMember);
			store.updateUserAccount(staffMember, username, password, Role.of("ROLE_" + role));

		} else {
			throw new IllegalArgumentException("Username ist schon in Benutzung!");
		}
	}

	public void updateStaffMember(String surname, String forename, String telephonenumber, String username,
			String password, String salaryStr) {

		StaffMember member = store.getStaffMemberByName(username);
		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || username.equals("")
				|| password.equals("") || salaryStr.equals("")) {
			throw new IllegalArgumentException("Eingabefelder überprüfen!");
		}

		for (char c : telephonenumber.toCharArray()) {
			if (!Character.isDigit(c)) {
				throw new IllegalArgumentException("Telefonnummer darf nur Ziffern enthalten!");

			}
		}
		member.getPerson().setTelephoneNumber(telephonenumber);
		member.getPerson().setForename(forename);
		member.getPerson().setSurname(surname);
		float salary = Float.parseFloat(salaryStr);
		member.setSalary(Money.of(salary, EURO));

		Optional<UserAccount> userAccount = employeeAccountManager.findByUsername(username);

		if (userAccount.isPresent()) {
			employeeAccountManager.changePassword(userAccount.get(), password);
		} else {
			throw new IllegalArgumentException("Mitarbeiter hat keinen Benutzerccount!");
		}

	}

	public void deleteStaffMember(String username, UserAccount lUserAccount) throws IllegalArgumentException {
		if (lUserAccount == null)
			throw new IllegalArgumentException("Nicht eingeloggt!");

		StaffMember member = store.getStaffMemberByName(username);

		if (member.getUserAccount().equals(lUserAccount) && member.getRole().getName().contains("ADMIN")) {
			throw new IllegalArgumentException("Eingeloggter Admin kann nicht gelöscht werden!");
		}
		Optional<UserAccount> userAccount = employeeAccountManager.findByUsername(username);

		if (userAccount.isPresent()) {
			employeeAccountManager.disable(userAccount.get().getId());
		}
		store.getStaffMemberList().remove(member);

	}
}
