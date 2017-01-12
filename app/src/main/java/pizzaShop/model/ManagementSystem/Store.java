package pizzaShop.model.ManagementSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import pizzaShop.controller.ErrorClass;
import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.AccountSystem.Person;
import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.DataBaseSystem.AddressRepository;
import pizzaShop.model.DataBaseSystem.CatalogHelper;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.ManagementSystem.Tan_Management.TanStatus;
import pizzaShop.model.OrderSystem.Cutlery;
import pizzaShop.model.OrderSystem.Ingredient;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.Pizza;
import pizzaShop.model.OrderSystem.PizzaOrder;
import pizzaShop.model.OrderSystem.PizzaOrderStatus;
import pizzaShop.model.ProductionSystem.Oven;

@Component
public class Store {

	private final UserAccountManager employeeAccountManager;
	private final ItemCatalog itemCatalog;
	private final PizzaOrderRepository pizzaOrderRepo;
	private final CustomerRepository customerRepository;
	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final CatalogHelper catalogHelper;
	private final TanManagement tanManagement;
	private List<StaffMember> staffMemberList; // why List and Repository for
												// StaffMember?
	private ArrayList<Oven> ovenList;
	private Pizza nextPizza;

	private ArrayList<String> eMailList;

	private MailSender mailSender;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();
	private Map<ProductIdentifier, ArrayList<String>> pizzaMap = new HashMap<ProductIdentifier, ArrayList<String>>();

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog,
			PizzaOrderRepository pizzaOrderRepo, 
			CustomerRepository customerRepository, Accountancy accountancy, BusinessTime businessTime,
			CatalogHelper catalogHelper, TanManagement tanManagement, AddressRepository addressRepository,
			MailSender mailSender) {

		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
		this.pizzaOrderRepo = pizzaOrderRepo;
		this.customerRepository = customerRepository;
		this.staffMemberList = new ArrayList<StaffMember>();
		this.ovenList = new ArrayList<Oven>();
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.catalogHelper = catalogHelper;
		this.tanManagement = tanManagement;
		this.eMailList = new ArrayList<String>();
		this.mailSender = mailSender;
		new ErrorClass(false);
		ovenList.add(new Oven(this));
		ovenList.add(new Oven(this));
		ovenList.add(new Oven(this));

	}

	public ArrayList<String> getEMailList() {
		return this.eMailList;
	}

	public boolean addEmailToMailingList(String eMailAddress) {

		if (!this.eMailList.contains(eMailAddress)) {
			return this.eMailList.add(eMailAddress);
		}

		return false;

	}

	public boolean removeEmailFromMailingList(String eMailAddress) {
		return this.eMailList.remove(eMailAddress);
	}

	public void sendNewsletter(String newsletterText) {

		for (String eMailAddress : this.eMailList) {

			SimpleMailMessage simpleMessage = new SimpleMailMessage();

			simpleMessage.setTo(eMailAddress);
			simpleMessage.setSubject("Papa_Pizza_Newsletter");
			simpleMessage.setText(newsletterText);

			try {
				this.mailSender.send(simpleMessage);
			} catch (MailException ex) {
				System.err.println(ex.getMessage());
			}

		}

	}

	public Pizzaqueue getPizzaQueue() {

		return pizzaQueue;
	}

	public ArrayList<Oven> getOvens() {

		return ovenList;
	}

	public List<StaffMember> getStaffMemberList() {
		return staffMemberList;
	}

	public void updateUserAccount(StaffMember member, String username, String password, Role role) {

		if (member.getUserAccount() == null) {
			member.setUsername(username);
			member.setPassword(password);
			member.setRole(role);

			member.setUserAccount(employeeAccountManager.create(username, password, role));

		} else {

		}

	}

	public StaffMember getStaffMemberByName(String name) {

		for (StaffMember staffMember : staffMemberList) {
			if (staffMember.getUsername().equals(name))
				return staffMember;
		}

		return null;
	}

	public StaffMember getStaffMemberByForename(String name) {

		for (StaffMember staffMember : staffMemberList) {
			if (staffMember.getPerson().getForename().equals(name)) {
				return staffMember;
			}
		}
		return null;
	}

	/**
	 * Method to fill the pizzaqueue, if order contains pizza
	 * 
	 * @param order
	 *            PizzaOrder to be analyzed
	 * @return analyzed PizzaOrder
	 */
	public PizzaOrder analyzeOrder(PizzaOrder order) {
		for (OrderLine l : order.getOrder().getOrderLines()) {
			Item temp = itemCatalog.findOne(l.getProductIdentifier()).get();
			if (temp.getType().equals(ItemType.PIZZA)) {
				Pizza p = (Pizza) temp;
				for (int i = 0; i < l.getQuantity().getAmount().intValue(); i++) {
					addOrder(order.getId(), p);
					System.out.println(getFirstOrder(p));
					pizzaQueue.add(p);
					order.addAsUnbaked();
				}

			}
		}
		if (order.getUnbakedPizzas() == 0) {
			order.readyOrder();
		}
		pizzaOrderRepo.save(order);
		System.out.println(pizzaQueue);
		return order;

	}

	/**
	 * Update unbaked pizza in a {@link PizzaOrder}
	 * 
	 * @param pizza
	 */
	public void updatePizzaOrder(Pizza pizza) {

		if (pizza == null) {
			return;
		} else {

			Iterable<PizzaOrder> pizzaOrders = pizzaOrderRepo.findAll();

			for (PizzaOrder order : pizzaOrders) {

				System.out.println("Order ID: " + order.getId());
				System.out.println("Pizza ID: " + getFirstOrder(pizza));

				if (order.getId().toString().equals(getFirstOrder(pizza))) {
					order.markAsBaked();
					System.out.println("updatePizzaOrder im if statement");

					try {
						removeFirstOrder(pizza);
					} catch (Exception e) {
						System.out.println("Fehler bei updatePizzaOrder");
					}

					pizzaOrderRepo.save(order);
					return;
				}
			}
		}
	}

	public void addOrder(OrderIdentifier o, Pizza p) {
		if (!pizzaMap.containsKey(p.getId())) {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(o.toString());
			pizzaMap.put(p.getId(), tmp);
		} else {
			pizzaMap.get(p.getId()).add(o.toString());
		}

	}

	/**
	 * 
	 * @return returns the first order as a String
	 */
	public String getFirstOrder(Pizza p) {
		if (pizzaMap.get(p.getId()).isEmpty())
			System.out.println(p.getName() + " hat keine Bestellung");
		return pizzaMap.get(p.getId()).get(0);
	}

	/**
	 * removes order from the list
	 * 
	 * @return returns the name of the first order in the list
	 * @throws Exception
	 *             when there is no order in the list
	 */
	public String removeFirstOrder(Pizza p) throws Exception {
		if (pizzaMap.get(p.getId()).isEmpty())
			throw new NullPointerException("Die Pizza hat keine Orders zugewiesen");
		return pizzaMap.get(p.getId()).remove(0);
	}

	public void printQueue(Pizza p) {
		System.out.println(pizzaMap.get(p.getId()).toString());
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
		
		Iterable<PizzaOrder> allPizzaOrders = this.pizzaOrderRepo.findAll();
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
		pizzaOrderRepo.delete(ordersToDelete);
		

		if (!foundTan.getStatus().equals(TanStatus.NOT_FOUND)) {
			tanManagement.invalidateTan(foundTan);
		}

		if (c.getCutlery() != null) {
			this.returnCutlery("decayed", c);
		}

		customerRepository.delete(id);
	}

	/**
	 * finish Order and create AccountancyEntry
	 * 
	 * @param p
	 * @param msg
	 * @param del
	 */
	public void completeOrder(PizzaOrder p, String msg, Deliverer del) {
		p.completeOrder();
		pizzaOrderRepo.save(p);
		if (del == null) {
			AccountancyEntry a = new AccountancyEntry(p.getTotalPrice(),
					"Order von " + p.getCustomer().getPerson().getForename() + p.getCustomer().getPerson().getSurname()
							+ " über Account " + p.getOrder().getUserAccount().getUsername() + " " + msg);
			accountancy.add(a);

		} else {

			/*
			 * Probleme mit dem totalPrice, deshalb erstmal ein Workaround
			 * ProductPaymentEntry a = ProductPaymentEntry.of(p.getOrder(),
			 * "Order über Account " +
			 * p.getOrder().getUserAccount().getUsername() + " "+msg);
			 */
			AccountancyEntry a = new AccountancyEntry(p.getTotalPrice(),
					"Order über Account " + del.getUsername() + " " + msg);
			accountancy.add(a);
		}
	}

	public void getNextPizza() throws Exception {

		if (!pizzaQueue.isEmpty()) {
			nextPizza = pizzaQueue.poll();
		} else {
			throw new NullPointerException("There is no Pizza in the PizzaQueue!");
		}
	}

	/**
	 * put first pizza in queue into given oven
	 * 
	 * @param oven
	 *            oven to be filled
	 * @return whether successfull or not
	 */
	public boolean putPizzaIntoOven(Oven oven) {

		for (int i = 0; i < ovenList.size(); i++) {
			if (ovenList.get(i).getId() == oven.getId() && ovenList.get(i).isEmpty()) {
				ovenList.get(i).fill(nextPizza, businessTime);
				System.out.println(
						"Pizza in Ofen Nummer: " + ovenList.get(i).getId() + " Pizza: " + ovenList.get(i).getPizza());

				return true;
			}
		}
		return false;
	}

	/**
	 * function to simulate the case "customer lent a {@link Cutlery}" (no
	 * inventory needed thus only created on lending)
	 * 
	 * @param customer
	 *            lending customer
	 * @param time
	 *            current time when borrowed
	 * @return
	 */

	/**
	 * Function for returning a {@link Cutlery} lent by a customer
	 * 
	 * @param lost
	 *            <code> true </code> if customer lost his
	 *            {@link catalog_item.Cutlery}, <code> false </code> if he
	 *            returns it properly
	 * @param customer
	 *            customer who wants to return his {@link catalog_item.Cutlery}
	 * @throws Exception
	 *             when customer hasn't lent a {@link catalog_item.Cutlery}
	 *             beforehand
	 */
	public void returnCutlery(String status, Customer customer) throws Exception {
		// TODO: decayed not equals lost --> other Accountancymessage
		String message = " hat seine Essgarnitur verloren";
		if (customer == null)
			throw new NullPointerException("Welcher Kunde?");
		if (customer.getCutlery() == (null))
			throw new NullPointerException("Kunde hatte keine Essgarnitur ausgeliehen bzw ist schon verfallen");

		if (status.equals("lost") || status.equals("decayed")) {
			if (status.equals("decayed"))
				message = " hat seine Essgarnitur nicht zurückgegeben";
			accountancy.add(new AccountancyEntry(Money.of(customer.getCutlery().getPrice().getNumber(), EURO),
					customer.getPerson().getForename() + " " + customer.getPerson().getSurname() + message));
		}

		customer.setCutlery(null);

		this.customerRepository.save(customer);
	}

	public void configurePizza(Model model, String ids[], String pizzaName, String admin_flag, String pizzaID,
			Cart cart) {

		Pizza newPizza;

		newPizza = new Pizza("custom", Money.of(2, "EUR"));

		for (int i = 0; i < ids.length; i++) {

			Item foundItem = catalogHelper.findItemByIdentifier(ids[i], ItemType.INGREDIENT);

			if (foundItem != null) {
				MonetaryAmount itemPrice = foundItem.getPrice();
				String itemName = foundItem.getName();

				Ingredient newIngredient = new Ingredient(itemName, itemPrice);
				newPizza.addIngredient(newIngredient);

				if (pizzaName.equals("") || (pizzaName == null)) {
					newPizza.setName("DIY Pizza");
				} else {
					newPizza.setName(pizzaName);
				}
			}
		}

		System.out.println(admin_flag + " " + pizzaID);
		if ((admin_flag != null && !admin_flag.equals("")) && admin_flag.equals("true")
				&& (pizzaID != null && !pizzaID.equals(""))) {
			Pizza pizza = (Pizza) (catalogHelper.findItemByIdentifier(pizzaID, null));
			itemCatalog.delete(pizza);
		}

		boolean exist = false;
		Item existingPizza = null;
		for (Item i : itemCatalog.findByType(ItemType.PIZZA)) {

			if (i.toString().equals(newPizza.toString())) {
				exist = true;
				existingPizza = i;
			}
		}

		if (!exist) {
			Pizza savedPizza = itemCatalog.save(newPizza);
			cart.addOrUpdateItem(savedPizza, Quantity.of(1));
		} else {
			model.addAttribute("existingPizza", existingPizza.getName());
			if (existingPizza != null)
				cart.addOrUpdateItem(existingPizza, Quantity.of(1));
		}
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
					System.out.println("if statement geht nicht");

				}
			}
		}
	}

	public void deleteOven(int Id) {

		for (int i = 0; i < ovenList.size(); i++) {
			if (ovenList.get(i).getId() == Id) {
				ovenList.remove(i);
			}
		}
	}

	/**
	 * 
	 * @param t
	 *            input telephonnumber
	 * @param p
	 *            {@link Person} to be edited (if new Person --> null)
	 * @return errormessage (isEmpty if valid)
	 */
	public String validateTelephonenumber(String t, Person p) {
		for (char c : t.toCharArray()) {
			if (!Character.isDigit(c))
				return "Telefonnummer enthält Buchstaben";

		}

		for (Customer cu : this.customerRepository.findAll()) {
			if (cu.getPerson().getTelephoneNumber().equals(t) && !cu.getPerson().equals(p))
				return "Diese Telefonnummber ist bereits vergeben";
		}
		return "";
	}
}
