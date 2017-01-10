package pizzaShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Address;
import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Person;
import pizzaShop.model.store.AddressRepository;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.tan_management.TanManagement;

@Controller
public class SellerController {

	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final AddressRepository addressRepository;
	private ErrorClass error;

	@Autowired
	public SellerController(TanManagement tanManagement, CustomerRepository customerRepository, AddressRepository addressRepository) {
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
		error = new ErrorClass(false);
	}

	@RequestMapping("/register_customer")
	public String registrationIndex(Model model, @RequestParam(value = "cid", required = false) Long id) {
		
		Long customerId = id;
		
		if(customerId != null){
			Customer customer = customerRepository.findOne(id);		
			model.addAttribute("existingCustomer", customer);
		}
		error.setError(false);
		model.addAttribute("error", error);
		return "register_customer";
	}

	@RequestMapping(value = "/registerCustomer", method = RequestMethod.POST)
	public String addCustomer(Model model, @RequestParam("surname") String surname, @RequestParam("forename") String forename,
			@RequestParam("telnumber") String telephonenumber, @RequestParam("local") String local,
			@RequestParam("postcode") String postcode, @RequestParam("street") String street,
			@RequestParam("housenumber") String housenumber) {
		
		System.out.println("PenisVagina deine Mamam");
		if (surname == "" || forename == "" || telephonenumber == "" || local == "" || street == "" || housenumber == ""
				|| postcode == "") {
			error.setError(true);
			error.setMessage("Eingabefelder überprüfen!");
			model.addAttribute("error", error);
			return "register_customer";
		} else {
			error.setError(false);
				
			Address newAddress = new Address(local, postcode, street, housenumber);
			
			boolean addressAlreadyExists = false;
			
			for(Address address : this.addressRepository.findAll())
			{
				if(address.equals(newAddress)){
					newAddress = address;
					addressAlreadyExists = true;
					break;
				}
			}
			
			if(!addressAlreadyExists)
			{
				newAddress = this.addressRepository.save(newAddress);
			}
		
			Person editedPerson = new Person(surname,forename, telephonenumber, newAddress);
			
			Customer editedCustomer= new Customer(editedPerson);
			
			customerRepository.save(editedCustomer);
			tanManagement.generateNewTan(telephonenumber);
			return "redirect:customer_display";
		}
	}

}
