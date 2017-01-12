package pizzaShop.controller;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.AccountSystem.Address;
import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.Person;
import pizzaShop.model.DataBaseSystem.AddressRepository;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;

@Controller
public class SellerController {

	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	// private final AddressRepository addressRepository;
	private ErrorClass error;
	private final Store store;

	@Autowired
	public SellerController(Store store, TanManagement tanManagement, CustomerRepository customerRepository) {// ,
		// AddressRepository
		// addressRepository)
		this.store = store; // {
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		// this.addressRepository = addressRepository;
		error = new ErrorClass(false);
	}

	@RequestMapping("/register_customer")
	public String registrationIndex(Model model, @RequestParam(value = "cid", required = false) Long id) {

		Long customerId = id;

		if (customerId != null) {
			Customer customer = customerRepository.findOne(id);
			model.addAttribute("existingCustomer", customer);
		}
		error.setError(false);
		model.addAttribute("error", error);
		return "register_customer";
	}

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public String addCustomer(Model model, @RequestParam("surname") String surname,
			@RequestParam("forename") String forename, @RequestParam("telnumber") String telephonenumber,
			@RequestParam("local") String local, @RequestParam("postcode") String postcode,
			@RequestParam("street") String street, @RequestParam("housenumber") String housenumber) {

		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || local.equals("") || street.equals("") || housenumber.equals("")
				|| postcode.equals("")) {

			error.setError(true);
			error.setMessage("Eingabefelder überprüfen!");
			model.addAttribute("error", error);
			return "register_customer";
		}

		String msg = store.validateTelephonenumber(telephonenumber, null);
		if (!msg.isEmpty()) {
			error.setError(true);
			error.setMessage(msg);
			model.addAttribute("error", error);
			return "register_customer";
		}
		error.setError(false);

		// Address newAddress = new Address(local, postcode, street,
		// housenumber);

		/*
		 * boolean addressAlreadyExists = false;
		 * 
		 * for(Address address : this.addressRepository.findAll()) {
		 * if(address.equals(newAddress)){ newAddress = address;
		 * addressAlreadyExists = true; break; } }
		 * 
		 * if(!addressAlreadyExists) { newAddress =
		 * this.addressRepository.save(newAddress); }
		 */

		// Person editedPerson = new Person(surname,forename, telephonenumber,
		// newAddress);

		Customer editedCustomer = new Customer(surname, forename, telephonenumber, local, postcode, street,
				housenumber);

		customerRepository.save(editedCustomer);
		tanManagement.generateNewTan(telephonenumber);
		Tan newTan = null;
		for (Entry<Tan, String> t : tanManagement.getAllNotConfirmedTans())
			if (t.getValue().equals(telephonenumber))
				newTan = t.getKey();
		if (newTan != null)
			tanManagement.confirmTan(newTan);
		return "redirect:customer_display";
	}
}
