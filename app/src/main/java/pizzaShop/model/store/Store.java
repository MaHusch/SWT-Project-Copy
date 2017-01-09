package pizzaShop.model.store;

import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.order.OrderLine;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog.Cutlery;
import pizzaShop.model.catalog.Ingredient;
import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.catalog.Pizza;

@Component
public class Store {

	private final UserAccountManager employeeAccountManager;
	private final ItemCatalog itemCatalog;
	private final PizzaOrderRepository pizzaOrderRepo;
	private final StaffMemberRepository staffMemberRepository;
	private final CustomerRepository customerRepository;
	private final Accountancy accountancy;
	private final BusinessTime businessTime;

	private List<StaffMember> staffMemberList; // why List and Repository for
												// StaffMember?
	private ArrayList<Oven> ovenList;
	private Pizza nextPizza;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog,
			PizzaOrderRepository pizzaOrderRepo, StaffMemberRepository staffMemberRepository,
			CustomerRepository customerRepository, Accountancy accountancy, BusinessTime businessTime) {

		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
		this.pizzaOrderRepo = pizzaOrderRepo;
		this.staffMemberRepository = staffMemberRepository;
		this.customerRepository = customerRepository;
		this.staffMemberList = new ArrayList<StaffMember>();
		this.ovenList = new ArrayList<Oven>();
		this.accountancy = accountancy;
		this.businessTime = businessTime;

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
				for (int i = 0; i < l.getQuantity().getAmount().intValue(); i++) {
					((Pizza) temp).addOrder(order.getId());
					pizzaQueue.add(((Pizza) temp));
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
				System.out.println("Pizza ID: " + pizza.getFirstOrder());

				if (order.getId().toString().equals(pizza.getFirstOrder())) {
					order.markAsBaked();
					try {
						pizza.removeFirstOrder();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.out.println("Fehler bei updatePizzaOrder");
					}
					pizzaOrderRepo.save(order);
					return;
				}
			}
		}
	}

	public void completeOrder(PizzaOrder p, String msg) {
		p.completeOrder();
		pizzaOrderRepo.save(p);
		/*
		 * Probleme mit dem totalPrice, deshalb erstmal ein Workaround
		 * ProductPaymentEntry a = ProductPaymentEntry.of(p.getOrder(),
		 * "Order über Account " + p.getOrder().getUserAccount().getUsername() +
		 * " "+msg);
		 */
		AccountancyEntry a = new AccountancyEntry(p.getTotalPrice(),
				"Order über Account " + p.getOrder().getUserAccount().getUsername() + " " + msg);
		accountancy.add(a);
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
				System.out.println(ovenList.get(i).getPizza());

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
