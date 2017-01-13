package pizzaShop.model.AccountSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.ManagementSystem.Tan_Management.TanStatus;
import pizzaShop.model.OrderSystem.Cutlery;
import pizzaShop.model.OrderSystem.PizzaOrder;
import pizzaShop.model.OrderSystem.PizzaOrderStatus;

@Component
public class CustomerHelper {
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final Store store;
	private final Accountancy accountancy;
	private final BusinessTime businessTime;

	@Autowired
	public CustomerHelper(Store store, TanManagement tanManagement, CustomerRepository customerRepository,
			PizzaOrderRepository pizzaOrderRepository, Accountancy accountancy, BusinessTime businessTime ) {
		this.store = store;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.accountancy = accountancy;
		this.businessTime = businessTime;
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

	/**
	 * deletes customer based on and ID, and cancels all his orders.
	 * 
	 * @param model
	 * @param id
	 * @throws Exception
	 */
	public void deleteCustomer(Model model, long id) throws Exception {
	
		Customer c = customerRepository.findOne(id);
		Tan foundTan = tanManagement.getTan(c.getPerson().getTelephoneNumber());
	
		Iterable<PizzaOrder> allPizzaOrders = this.pizzaOrderRepository.findAll();
		ArrayList<PizzaOrder> ordersToDelete = new ArrayList<PizzaOrder>();
		for (PizzaOrder pizzaOrder : allPizzaOrders) {
			Customer customer = pizzaOrder.getCustomer();
	
			if (customer.getId() == id) {
				ordersToDelete.add(pizzaOrder);
				PizzaOrderStatus pOStatus = pizzaOrder.getOrderStatus();
				if (!(pOStatus.equals(PizzaOrderStatus.CANCELLED) || pOStatus.equals(PizzaOrderStatus.COMPLETED))) {
					throw new Exception("Kunde hat noch offene Bestellungen!");
				}
			}
	
		}
		pizzaOrderRepository.delete(ordersToDelete);
	
		if (!foundTan.getStatus().equals(TanStatus.NOT_FOUND)) {
			tanManagement.invalidateTan(foundTan);
		}
	
		if (c.getCutlery() != null) {
			this.returnCutlery("decayed", c);
		}
	
		customerRepository.delete(id);
	}

	/**
	 * Function for returning a {@link Cutlery} lent by a customer
	 * 
	 * @param lost
	 *            <code> true </code> if customer lost his
	 *            {@link catalog_item.Cutlery}, <code> false </code> if he
	 *            returns it properly
	 * @param customer
	 *            customer who wants to return his {@link Cutlery}
	 * @throws Exception
	 *             when customer hasn't lent a {@link Cutlery} beforehand
	 */
	public void returnCutlery(String status, Customer customer) throws NullPointerException {
		String message = " hat seine Essgarnitur verloren";
		if (customer == null)
			throw new NullPointerException("Welcher Kunde?");
		if (customer.getCutlery() == (null))
			throw new NullPointerException("Kunde hatte keine Essgarnitur ausgeliehen bzw ist schon verfallen");
	
		if (status.equals("lost") || status.equals("decayed")) {
			if (status.equals("decayed"))
				message = " hat seine Essgarnitur nicht zurückgegeben";
			accountancy.add(new AccountancyEntry(Money.of(customer.getCutlery().getPrice().multiply(-1).getNumber(), EURO),
					customer.getPerson().getForename() + " " + customer.getPerson().getSurname() + message));
		}
	
		customer.setCutlery(null);
	
		this.customerRepository.save(customer);
	}

	/**
	 * check whether any cutlery has decayed
	 */
	public void checkCutleries() {
		for (Customer c : this.customerRepository.findAll()) {
			if (c.getCutlery() != null && c.getCutlery().getDate().isBefore(businessTime.getTime())) {
				try {
					this.returnCutlery("decayed", c);
				} catch (Exception e) {
				}
			}
		}
	}

}
