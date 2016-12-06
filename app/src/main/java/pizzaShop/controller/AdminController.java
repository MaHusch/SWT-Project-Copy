package pizzaShop.controller;

import org.salespointframework.useraccount.Role;
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

	private final Store store;
	
	@Autowired
	public AdminController(Store store, StaffMemberRepository staffMemberRepository) {
		this.store = store;
		this.staffMemberRepository = staffMemberRepository;
		
		
	}

	@RequestMapping("/register_staffmember")
	public String registrationIndex(Model model, @RequestParam(value = "sid", required = false) long ID) {
		
		StaffMember member = staffMemberRepository.findOne(ID);
		model.addAttribute("staffMember",member);
		//staffMemberRepository.delete(id);
		
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
		store.updateUserAccount(staffMember, username, password, Role.of("ROLE_" + role));
		
		

		return "index";
	}
	

	@RequestMapping(value = "/addOven", method = RequestMethod.POST)
	public String addOven(Model model) {

		store.getOvens().add(new Oven(store));
		model.addAttribute("error", error);

		return "redirect:ovens";

	}

}
