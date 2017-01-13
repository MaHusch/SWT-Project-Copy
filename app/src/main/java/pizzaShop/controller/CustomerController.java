package pizzaShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.CustomerHelper;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;

@Controller
public class CustomerController {

	private final CustomerRepository customerRepository;
	private final CustomerHelper customerHelper;
	private ErrorClass displayError = new ErrorClass(false);
	private ErrorClass registerError = new ErrorClass(false);
	private final TanManagement tanManagement;

	@Autowired
	public CustomerController(CustomerRepository customerRepository, CustomerHelper customerHelper, TanManagement tanManagement) {
		this.customerRepository = customerRepository;
		this.customerHelper = customerHelper;
		this.tanManagement = tanManagement;
	}
	

	@RequestMapping("/register_customer")
	public String registrationIndex(Model model, @RequestParam(value = "cid", required = false) Long id) {

		Long customerId = id;
		
		// We are using the register_customer form to register and to edit a customer.
		// In order for the form fields to be filled in when editing the customer, an already 
		// existing model needs to be added.
		
		if (customerId != null) {
			Customer customer = customerRepository.findOne(id);
			model.addAttribute("existingCustomer", customer);
		}

		model.addAttribute("error", registerError);
		return "register_customer";
	}
	
	@RequestMapping("/customer_display")
	public String customer_display(Model model) {
	
		model.addAttribute("customer", customerRepository.findAll());
		model.addAttribute("tanListe", this.tanManagement);
		model.addAttribute("error", displayError);
	
		return "customer_display";
	}

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public String addCustomer(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("local") String local, @RequestParam("postcode") String postcode,
			@RequestParam("street") String street, @RequestParam("housenumber") String housenumber) {

		registerError.setError(false);
		try {
			customerHelper.createCustomer(surname, forename, telephonenumber, local, street, housenumber, postcode);
		} catch (Exception e) {
			registerError.setError(true);
			registerError.setMessage(e.getMessage());
			return "redirect:register_customer";
		}
		return "redirect:customer_display";
	}

	@RequestMapping(value = "/updateCustomer", method = RequestMethod.POST)
	public String updateCustomer(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("local") String local, @RequestParam("postcode") String postcode,
			@RequestParam("street") String street, @RequestParam("housenumber") String housenumber,
			@RequestParam("cid") long id, RedirectAttributes redirectAttrs) {

		registerError.setError(false);
		try {
			customerHelper.updateCustomer(surname, forename, telephonenumber, local, street, housenumber, postcode, id);
		} catch (Exception e) {
			registerError.setError(true);
			registerError.setMessage(e.getMessage());
			redirectAttrs.addAttribute("cid", id).addFlashAttribute("message", "Customer");
			return "redirect:register_customer";
		}
		return "redirect:customer_display";
	}

	

	@RequestMapping("/deleteCustomer")
	public String deleteCustomer(Model model, @RequestParam("cid") long id) {
		displayError.setError(false);
		try {
			customerHelper.deleteCustomer(model, id);
		} catch (Exception e) {
			displayError.setError(true);
			displayError.setMessage(e.getMessage());
		}
	
		return "redirect:customer_display";
	
	}

	@RequestMapping("/editCustomer")
	public String editCustomer(Model model, @RequestParam("cid") long id, RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("cid", id).addFlashAttribute("message", "Customer");
		return "redirect:register_customer";
	
	}

	@RequestMapping("returnCutlery")
	public String returnCutlery(@RequestParam("lost") String lostStr, @RequestParam("cid") long id) {
		String cutleryStatus = "lost";
		if (lostStr.equals("0"))
			cutleryStatus = "returned";
	
		try {
			customerHelper.returnCutlery(cutleryStatus, this.customerRepository.findOne(id));
		} catch (Exception e) {
			displayError.setError(true);
			displayError.setMessage(e.getMessage());
			;
		}
	
		return "redirect:customer_display";
	}
}
