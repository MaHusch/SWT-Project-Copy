package pizzaShop.controller;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.Optional;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.Seller;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.Oven;
import pizzaShop.model.store.StaffMemberRepository;
import pizzaShop.model.store.Store;

@Controller
public class AdminController {

	private ErrorClass error = new ErrorClass(false);
	private final StaffMemberRepository staffMemberRepository;
	private final UserAccountManager employeeAccountManager;
	private final Accountancy accountancy;

	private final Store store;

	@Autowired
	public AdminController(Store store, StaffMemberRepository staffMemberRepository,
			UserAccountManager employeeAccountManager, Accountancy accountancy) {
		this.store = store;
		this.staffMemberRepository = staffMemberRepository;
		this.employeeAccountManager = employeeAccountManager;
		this.accountancy = accountancy;

	}

	@RequestMapping("/register_staffmember")
	public String registrationIndex(Model model, @RequestParam(value = "name", required = false) String name) {

		StaffMember member = store.getStaffMemberByName(name);
		// System.out.println(member.getUsername());
		model.addAttribute("staffMember", member);

		// ArrayList<StaffMember> staffMemberList = (ArrayList<StaffMember>)
		// store.getStaffMemberList();
		// StaffMember updatedMember =
		// staffMemberList.get(staffMemberList.indexOf(member));

		model.addAttribute("error", error);
		return "register_staffmember";
	}

	@RequestMapping(value = "/registerEmployee", method = RequestMethod.POST)
	public String addStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("role") String role) {

		if (surname == "" || forename == "" || telephonenumber == "" || username == "" || password == ""
				|| role == "") {
			error.setError(true);
			model.addAttribute("error", error);
			return "redirect:register_staffmember";
		}

		StaffMember staffMember;

		switch (role) {
		case "Bäcker":
			role = "BAKER";
			staffMember = new Baker(surname, forename, telephonenumber);
			break;
		case "Lieferant":
			role = "DELIVERY";
			staffMember = new Deliverer(surname, forename, telephonenumber);
			break;

		default: // Seller ist bei HTML sowieso als default ausgewählt
			role = "SELLER";
			staffMember = new Seller(surname, forename, telephonenumber);
			break;
		}

		store.getStaffMemberList().add(staffMember);

		/*
		 * Optional<UserAccount> userAccount =
		 * employeeAccountManager.findByUsername(username);
		 * 
		 * if(userAccount.isPresent()){
		 * employeeAccountManager.disable(userAccount.get().getId()); }
		 * 
		 * Optional<UserAccount> userAccount =
		 * employeeAccountManager.findByUsername(username);
		 * 
		 * if (employeeAccountManager.contains(userAccount.get().getId())) {
		 * 
		 * } else {
		 * 
		 * }
		 */

		store.updateUserAccount(staffMember, username, password, Role.of("ROLE_" + role));

		return "redirect:staffmember_display";
	}

	@RequestMapping(value = "/updateStaffMember")
	public String updateStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password) {
		StaffMember member = store.getStaffMemberByName(username);

		member.getPerson().setForename(forename);
		member.getPerson().setSurname(surname);
		member.getPerson().setTelephoneNumber(telephonenumber);

		Optional<UserAccount> userAccount = employeeAccountManager.findByUsername(username);

		if (userAccount.isPresent()) {
			employeeAccountManager.changePassword(userAccount.get(), password);
		}

		return "redirect:staffmember_display";
	}

	@RequestMapping(value = "/deleteStaffMember")
	public String updateStaffMember(Model model, @RequestParam("StaffMemberName") String username) {
		StaffMember member = store.getStaffMemberByName(username);

		Optional<UserAccount> userAccount = employeeAccountManager.findByUsername(username);

		if (userAccount.isPresent()) {
			employeeAccountManager.disable(userAccount.get().getId());
		}

		ArrayList<StaffMember> staffMemberList = (ArrayList<StaffMember>) store.getStaffMemberList();
		staffMemberList.remove(member);

		return "redirect:staffmember_display";
	}

	@RequestMapping(value = "/addOven", method = RequestMethod.POST)
	public String addOven(Model model) {

		store.getOvens().add(new Oven(store));
		accountancy.add(new AccountancyEntry(Money.of(-1000, EURO), "Neuer Ofen gekauft"));
		model.addAttribute("error", error);

		return "redirect:ovens";

	}

	@RequestMapping(value = "/deleteOven", method = RequestMethod.POST)
	public String deleteOVen(@RequestParam("ovenID") int id) {

		store.deleteOven(id);

		return "redirect:ovens";
	}

}
