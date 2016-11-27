package pizzaShop.controller;

import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.Seller;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.store.Store;

@Controller
public class AdminController {
	
	public AdminController(){}
	
	@RequestMapping("/register_staffmember")
	public String registrationIndex(Model model){
		return "register_staffmember";
	}
	
	
	@RequestMapping(value = "/registerEmployee", method = RequestMethod.POST)
	public String addStaffMember(@RequestParam  ("surname")   String  surname,
								 @RequestParam  ("forename")  String  forename,
								 @RequestParam  ("telnumber") String  telephonenumber,
								 @RequestParam  ("username")  String  username,
								 @RequestParam  ("password")  String  password,
								 @RequestParam  ("role") 	  String  role            ){
		
		
		
		if ( surname == "" || forename == ""  || 
			 telephonenumber == ""|| username == ""|| password == "" || role == "" ) {			
			return "registeremployee";
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
				
			default: //Seller ist bei HTML sowieso als default ausgewählt
				role = "SELLER";
				staffMember = new Seller(surname, forename, telephonenumber);
				break;
		}	
		

		Store.staffMemberList.add(staffMember);
		staffMember.updateUserAccount(username, password, Role.of("ROLE_" + role));
		
		return "index";
	}
	
}
