package pizzaShop.model.AccountSystem;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.Cutlery;
import pizzaShop.model.OrderSystem.PizzaOrder;

@Component
public class CustomerHelper {
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final Store store;

	@Autowired
	public CustomerHelper(Store store, TanManagement tanManagement, CustomerRepository customerRepository,
			PizzaOrderRepository pizzaOrderRepository) {// ,
		this.store = store;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
	}
	
	public void createCustomer(String surname, String forename, String telephonenumber, String local, String street,
			String housenumber, String postcode) throws Exception {
		
		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || local.equals("")
				|| street.equals("") || housenumber.equals("") || postcode.equals("")) {
			throw new IllegalArgumentException("Eingabefelder überprüfen!");
		}

		String msg = store.validateTelephonenumber(telephonenumber, null);
		if (!msg.isEmpty()) {
			throw new IllegalArgumentException(msg);
		}
		
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
	}

	public void updateCustomer(String surname, String forename, String telephonenumber, String local, String street,
			String housenumber, String postcode, long id) throws Exception {
		Customer oldCustomer = customerRepository.findOne(id);
		String oldTelephoneNumber = oldCustomer.getPerson().getTelephoneNumber();

		if (surname.equals("") || forename.equals("") || telephonenumber.equals("") || local.equals("")
				|| street.equals("") || housenumber.equals("") || postcode.equals("")) {
			throw new IllegalArgumentException("Eingabefelder überprüfen!");
		}

		Cutlery oldCutlery = oldCustomer.getCutlery();

		if (!oldTelephoneNumber.equals(telephonenumber)) {
			tanManagement.updateTelephoneNumber(oldTelephoneNumber, telephonenumber);
		}

		Customer updatedCustomer = new Customer(surname, forename, telephonenumber, local, postcode, street,
				housenumber);

		if (oldCutlery != null) {
			updatedCustomer.setCutlery(oldCutlery);
		}

		Iterable<PizzaOrder> allPizzaOrders = pizzaOrderRepository.findAll();

		for (PizzaOrder pizzaOrder : allPizzaOrders) {
			Customer customer = pizzaOrder.getCustomer();
			if (customer != null) {
				if (customer.getId() == id) {
					pizzaOrder.setCustomer(updatedCustomer);
				}
			}
		}

		customerRepository.save(updatedCustomer);
		customerRepository.delete(id);

	}

}
