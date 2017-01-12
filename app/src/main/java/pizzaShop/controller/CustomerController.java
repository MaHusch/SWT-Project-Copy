package pizzaShop.controller;

import java.util.Map.Entry;

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
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.Cutlery;
import pizzaShop.model.OrderSystem.PizzaOrder;

@Controller
public class CustomerController {

	private final CustomerRepository customerRepository;
	private final CustomerHelper customerHelper;
	private ErrorClass error = new ErrorClass(false);

	@Autowired
	public CustomerController(CustomerRepository customerRepository, CustomerHelper customerHelper) {
		this.customerRepository = customerRepository;
		this.customerHelper = customerHelper;
	}

	@RequestMapping("/register_customer")
	public String registrationIndex(Model model, @RequestParam(value = "cid", required = false) Long id) {

		Long customerId = id;

		if (customerId != null) {
			Customer customer = customerRepository.findOne(id);
			model.addAttribute("existingCustomer", customer);
		}

		model.addAttribute("error", error);
		return "register_customer";
	}

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public String addCustomer(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("local") String local, @RequestParam("postcode") String postcode,
			@RequestParam("street") String street, @RequestParam("housenumber") String housenumber) {

		error.setError(false);
		try {
			customerHelper.createCustomer(surname, forename, telephonenumber, local, street, housenumber, postcode);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
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

		error.setError(false);
		try {
			customerHelper.updateCustomer(surname, forename, telephonenumber, local, street, housenumber, postcode, id);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
			redirectAttrs.addAttribute("cid", id).addFlashAttribute("message", "Customer");
			return "redirect:register_customer";
		}
		return "redirect:customer_display";
	}
}
