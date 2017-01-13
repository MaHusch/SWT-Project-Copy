
package pizzaShop.controller;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.AccountSystem.StaffMemberHelper;
import pizzaShop.model.ManagementSystem.Store;

@Controller
public class StaffMemberController {

	private ErrorClass displayError = new ErrorClass(false);
	private ErrorClass registerError = new ErrorClass(false);
	private final StaffMemberHelper staffMemberHelper;

	private final Store store;

	@Autowired
	public StaffMemberController(Store store, StaffMemberHelper staffMemberHelper) {
		this.store = store;
		this.staffMemberHelper = staffMemberHelper;

	}

	@RequestMapping("/staffmember_display")
	public String staffmember_display(Model model) {

		model.addAttribute("staffmember", store.getStaffMemberList());
		model.addAttribute("error", displayError);

		return "staffmember_display";
	}

	@RequestMapping("/register_staffmember")
	public String registrationIndex(Model model, @RequestParam(value = "name", required = false) String name) {

		StaffMember member = store.getStaffMemberByUsername(name);
		model.addAttribute("staffMember", member);
		model.addAttribute("error", registerError);
		return "register_staffmember";
	}

	@RequestMapping(value = "/registerEmployee", method = RequestMethod.POST)
	public String registerStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("role") String role) {

		registerError.setError(false);
		try {
			staffMemberHelper.registerStaffMember(surname, forename, telephonenumber, username, password, role);
		} catch (Exception e) {
			registerError.setError(true);
			registerError.setMessage(e.getMessage());
			return "redirect:register_staffmember";
		}
		return "redirect:staffmember_display";
	}

	@RequestMapping(value = "/updateStaffMember")
	public String updateStaffMember(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("salary") String salaryStr, RedirectAttributes redirectAttrs) {

		registerError.setError(false);
		try {
			staffMemberHelper.updateStaffMember(surname, forename, telephonenumber, username, password, salaryStr);
		} catch (Exception e) {
			registerError.setError(true);
			registerError.setMessage(e.getMessage());
			redirectAttrs.addAttribute("name", username).addFlashAttribute("message", "StaffMember");
			return "redirect:register_staffmember";
		}

		return "redirect:staffmember_display";
	}

	@RequestMapping(value = "/deleteStaffMember")
	public String deleteStaffMember(Model model, @RequestParam("StaffMemberName") String username,
			@LoggedIn Optional<UserAccount> lUserAccount) {
		
		displayError.setError(false);
		try {
			staffMemberHelper.deleteStaffMember(username, lUserAccount.orElse(null));
		} catch (Exception e) {
			displayError.setError(true);
			displayError.setMessage(e.getMessage());
		}
		return "redirect:staffmember_display";
	}

	@RequestMapping("/editEmployee")
	public String directToEditStaffMember(Model model, @RequestParam("StaffMemberName") String name,
			RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("name", name).addFlashAttribute("message", "StaffMember");
		return "redirect:register_staffmember";

	}

}
