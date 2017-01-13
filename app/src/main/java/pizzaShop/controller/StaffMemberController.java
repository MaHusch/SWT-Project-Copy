package pizzaShop.controller;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.Optional;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
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
import pizzaShop.model.ManagementSystem.Store;

@Controller
public class StaffMemberController {

	private ErrorClass displayError = new ErrorClass(false);
	private ErrorClass registerError = new ErrorClass(false);
	private final UserAccountManager employeeAccountManager;
	private final Accountancy accountancy;

	private final Store store;

	@Autowired
	public StaffMemberController(Store store, UserAccountManager employeeAccountManager, Accountancy accountancy) {
		this.store = store;
		this.employeeAccountManager = employeeAccountManager;
		this.accountancy = accountancy;

	}

	
	@RequestMapping("/staffmember_display")
	public String staffmember_display(Model model) {

		model.addAttribute("staffmember", store.getStaffMemberList());
		model.addAttribute("error", displayError);
		
		

		return "staffmember_display";
	}	
	
	@RequestMapping("/register_staffmember")
	public String registrationIndex(Model model, @RequestParam(value = "name", required = false) String name) {

		StaffMember member = store.getStaffMemberByName(name);
		model.addAttribute("staffMember", member);
		model.addAttribute("error", registerError);
		return "register_staffmember";
	}

	@RequestMapping(value = "/registerEmployee", method = RequestMethod.POST)
	public String addStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("role") String role) {

		registerError.setError(false);
		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || username.equals("") || password.equals("")
				|| role.equals("")) {
			registerError.setError(true);
			registerError.setMessage("Eingabefelder überprüfen!");
			return "redirect:register_staffmember";
		}
		
		String msg = store.validateTelephonenumber(telephonenumber, null);
		if(!msg.isEmpty())
		{
			registerError.setError(true);
			registerError.setMessage(msg);
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
	
	
	
	

	@RequestMapping(value = "/updateStaffMember")
	public String updateStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("salary") String salaryStr, RedirectAttributes redirectAttrs) {
		
		registerError.setError(false);
		StaffMember member = store.getStaffMemberByName(username);
		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || username.equals("") || password.equals("") || salaryStr.equals("")) {
			registerError.setError(true);
			registerError.setMessage("Eingabefelder überprüfen!");
			redirectAttrs.addAttribute("name", username).addFlashAttribute("message", "StaffMember");
			return "redirect:register_staffmember";
		}

		
		for(char c : telephonenumber.toCharArray()){
			if(!Character.isDigit(c)){
				registerError.setError(true);
				registerError.setMessage("Telefonnummer darf nur Ziffern enthalten!");
				redirectAttrs.addAttribute("name", username).addFlashAttribute("message", "StaffMember");
				return "redirect:register_staffmember";
				
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
		}

		return "redirect:staffmember_display";
	}
	
	@RequestMapping(value = "/deleteStaffMember")
	public String deleteStaffMember(Model model, @RequestParam("StaffMemberName") String username, @LoggedIn Optional<UserAccount> lUserAccount) {
		StaffMember member = store.getStaffMemberByName(username);
		displayError.setError(false);
		if(member.getUserAccount().equals(lUserAccount.get())) 
		{	
			displayError.setError(true);
			displayError.setMessage("Eingeloggter Admin kann nicht gelöscht werden!");
			
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


	@RequestMapping("/editEmployee")
	public String directToEditStaffMember(Model model, @RequestParam("StaffMemberName") String name,
			RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("name", name).addFlashAttribute("message", "StaffMember");
		return "redirect:register_staffmember";
	
	}

}
