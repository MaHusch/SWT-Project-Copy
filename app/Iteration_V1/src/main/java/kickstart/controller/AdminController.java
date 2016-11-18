package kickstart.controller;

import org.salespointframework.useraccount.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kickstart.model.actor.StaffMember;
import kickstart.model.store.Store;

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
		
		switch (role) {
			case "Verkäufer":
				role = "SELLER";
				break;
			case "Bäcker":
				role = "BAKER";
				break;
			case "Lieferant":
				role = "DELIVERY";
				break;
		}	

		StaffMember staffMember = new StaffMember(surname,forename,telephonenumber);
		staffMember.updateUserAccount(username, password, Role.of("ROLE_" + role));
		
		return "index";
	}
	
}
