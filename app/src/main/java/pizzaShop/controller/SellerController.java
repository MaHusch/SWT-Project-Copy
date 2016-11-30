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

	private final TanManagement tanManagment;
	private final CustomerRepository customerRepository;

	@Autowired
	public SellerController(TanManagement tanManagment, CustomerRepository customerRepository) {
		this.tanManagment = tanManagment;
		this.customerRepository = customerRepository;
	}

	@RequestMapping("/register_customer")
	public String registrationIndex(Model model){
		return "register_customer";
	}


	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public String addCustomer(@RequestParam  ("surname")   String  surname,
								 @RequestParam  ("forename")  String  forename,
								 @RequestParam  ("telnumber") String  telephonenumber){
		
		if ( surname == "" || forename == ""  || telephonenumber == "") {			
			return "redirect:register_customer";
		}

		customerRepository.save(new Customer(surname, forename, telephonenumber));
		tanManagment.generateNewTan(telephonenumber);
		return "index";
	}
	
}
