package pizzaShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Customer;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.tan_management.TanManagement;

@Controller
public class SellerController {

	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;

	@Autowired
	public SellerController(TanManagement tanManagement, CustomerRepository customerRepository) {
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
	}

	@RequestMapping("/register_customer")
	public String registrationIndex(Model model){
		return "register_customer";
	}


	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public String addCustomer(@RequestParam  ("surname")   String  surname,
								 @RequestParam  ("forename")  String  forename,
								 @RequestParam  ("telnumber") String  telephonenumber,
	 							 @RequestParam  ("local") String  local,
	 							@RequestParam  ("postcode") String  postcode,
	 							@RequestParam  ("street") String  street,
	 							@RequestParam  ("housenumber") String  housenumber){
		
		if ( surname == "" || forename == ""  || telephonenumber == "" || local == "" || street == "" || housenumber == "" || postcode == "") {			
			return "redirect:register_customer";
		}

		customerRepository.save(new Customer(surname, forename, telephonenumber, local, postcode, street, housenumber));
		tanManagement.generateNewTan(telephonenumber);
		return "redirect:customer_display";
	}
	
}
