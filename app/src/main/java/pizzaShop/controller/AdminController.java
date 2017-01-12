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
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzaShop.model.AccountSystem.Baker;
import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.AccountSystem.Seller;
import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.DataBaseSystem.StaffMemberRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ProductionSystem.Oven;

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
		
		
		if( store.getStaffMemberByName(username) == null){
			store.getStaffMemberList().add(staffMember);
			store.updateUserAccount(staffMember, username, password, Role.of("ROLE_" + role));
		}else{
			model.addAttribute("error", error);
		}
		
		
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


		return "redirect:staffmember_display";
	}
	
	@RequestMapping("/staffmember_display")
	public String staffmember_display(Model model) {

		model.addAttribute("staffmember", store.getStaffMemberList());
		model.addAttribute("error",error);
		
		

		return "staffmember_display";
	}
	
	@RequestMapping(value = "/deleteStaffMember")
	public String updateStaffMember(Model model, @RequestParam("StaffMemberName") String username, @LoggedIn Optional<UserAccount> lUserAccount) {
		StaffMember member = store.getStaffMemberByName(username);
		error.setError(false);
		if(member.getUserAccount().equals(lUserAccount.get())) 
		{	
			error.setError(true);
			error.setMessage("Eingeloggter Admin kann nicht gelöscht werden!");
			
			return "redirect:staffmember_display"; 
		}
		Optional<UserAccount> userAccount = employeeAccountManager.findByUsername(username);

		if (userAccount.isPresent()) {
			employeeAccountManager.disable(userAccount.get().getId());
		}

		ArrayList<StaffMember> staffMemberList = (ArrayList<StaffMember>) store.getStaffMemberList();
		staffMemberList.remove(member);

		return "redirect:staffmember_display";
	}

	@RequestMapping(value = "/updateStaffMember")
	public String updateStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password, RedirectAttributes redirectAttrs) {
		StaffMember member = store.getStaffMemberByName(username);
		
		if (surname == "" || forename == "" || telephonenumber == "" || username == "" || password == "") {
			error.setError(true);
			redirectAttrs.addAttribute("name", username).addFlashAttribute("message", "StaffMember");
			return "redirect:register_staffmember";
		}

		member.getPerson().setForename(forename);
		member.getPerson().setSurname(surname);
		member.getPerson().setTelephoneNumber(telephonenumber);

		Optional<UserAccount> userAccount = employeeAccountManager.findByUsername(username);

		if (userAccount.isPresent()) {
			employeeAccountManager.changePassword(userAccount.get(), password);
		}

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
