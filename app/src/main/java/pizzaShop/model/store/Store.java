package pizzaShop.model.store;

import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog.CatalogHelper;
import pizzaShop.model.catalog.Cutlery;
import pizzaShop.model.catalog.Ingredient;
import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemCatalog;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.catalog.Pizza;
import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;
import pizzaShop.model.tan_management.TanStatus;

@Component
public class Store {

	private final UserAccountManager employeeAccountManager;
	private final ItemCatalog itemCatalog;
	private final PizzaOrderRepository pizzaOrderRepo;
	private final StaffMemberRepository staffMemberRepository;
	private final CustomerRepository customerRepository;
	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final CatalogHelper catalogHelper;
	private final TanManagement tanManagement;
	private List<StaffMember> staffMemberList; // why List and Repository for
												// StaffMember?
	private ArrayList<Oven> ovenList;
	private Pizza nextPizza;

	private ErrorClass error;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog,
			PizzaOrderRepository pizzaOrderRepo, StaffMemberRepository staffMemberRepository,
			CustomerRepository customerRepository, Accountancy accountancy, BusinessTime businessTime,
			CatalogHelper catalogHelper, TanManagement tanManagement) {

		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
		this.pizzaOrderRepo = pizzaOrderRepo;
		this.staffMemberRepository = staffMemberRepository;
		this.customerRepository = customerRepository;
		this.staffMemberList = new ArrayList<StaffMember>();
		this.ovenList = new ArrayList<Oven>();
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.catalogHelper = catalogHelper;
		this.tanManagement = tanManagement;
		error = new ErrorClass(false);

		ovenList.add(new Oven(this));
		ovenList.add(new Oven(this));
		ovenList.add(new Oven(this));

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

	public PizzaOrder analyzeOrder(PizzaOrder order) {
		for (OrderLine l : order.getOrder().getOrderLines()) {
			Item temp = itemCatalog.findOne(l.getProductIdentifier()).get();
			if (temp.getType().equals(ItemType.PIZZA)) {
				Pizza p = (Pizza) temp;
				for (int i = 0; i < l.getQuantity().getAmount().intValue(); i++) {
					p.setOrderId(order.getId());
					System.out.println("AnalyzeOrder PizzaOrderQueue: " + p.getOrderId());
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

	public void updatePizzaOrder(Pizza pizza) {

		if (pizza.equals(null)) {
			return;
		} else {

			Iterable<PizzaOrder> pizzaOrders = pizzaOrderRepo.findAll();

			for (PizzaOrder order : pizzaOrders) {

				System.out.println("Order ID: " + order.getId());
				System.out.println("Pizza ID: " + pizza.getOrderId());

				if (order.getId().toString().equals(pizza.getOrderId().toString())) {
					order.markAsBaked();
					System.out.println("updatePizzaOrder im if statement");
					/*
					 * try { pizza.removeFirstOrder(); } catch (Exception e) {
					 * // TODO Auto-generated catch block
					 * System.out.println("Fehler bei updatePizzaOrder"); }
					 */
					pizzaOrderRepo.save(order);
					return;
				}
			}
		}
	}

	// deletes customer based on and ID, and cancles all his orders.
	public void deleteCustomer(Model model, long id) {

		Tan foundTan = tanManagement.getTan(customerRepository.findOne(id).getPerson().getTelephoneNumber());

		if (!foundTan.getStatus().equals(TanStatus.NOT_FOUND)) {
			tanManagement.invalidateTan(foundTan);
		}

		Iterable<PizzaOrder> allPizzaOrders = this.pizzaOrderRepo.findAll();

		for (PizzaOrder pizzaOrder : allPizzaOrders) {

			Customer customer = pizzaOrder.getCustomer();

			if (customer != null) {
				if (customer.getId() == id) {
					pizzaOrder.setCustomer(null);
					pizzaOrder.cancelOrder();
				}
			}

		}

		customerRepository.delete(id);
	}

	public void completeOrder(PizzaOrder p, String msg, Deliverer del) {
		p.completeOrder();
		pizzaOrderRepo.save(p);
		if (del == null) {
			AccountancyEntry a = new AccountancyEntry(p.getTotalPrice(),
					"Order über Account " + p.getOrder().getUserAccount().getUsername() + " " + msg);
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
	public boolean lentCutlery(Customer customer, LocalDateTime time) {
		Cutlery cutlery = new Cutlery(Money.of(15.0, EURO), time);
		if (customer.equals(null))
			return false;
		if (customer.getCutlery() != null)
			return false; // has to return his lent cutlery before TODO: error
							// on cart template

		customer.setCutlery(cutlery);

		this.customerRepository.save(customer);

		return true;
	}

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
				message = "hat seine Essgarnitur nicht zurückgegeben";
			accountancy.add(new AccountancyEntry(Money.of(customer.getCutlery().getPrice().getNumber(), EURO),
					customer.getPerson().getForename() + " " + customer.getPerson().getSurname() + message));
		}

		customer.setCutlery(null);

		this.customerRepository.save(customer);
	}

	public void configurePizza(Model model, String ids[], String pizzaName, String admin_flag, String pizzaID,
			Cart cart) {

		Pizza newPizza;

		newPizza = new Pizza("custom", Money.of(0, "EUR"));

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
}
