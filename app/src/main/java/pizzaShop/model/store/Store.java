package pizzaShop.model.store;

import java.util.ArrayList;
import java.util.List;

import org.salespointframework.order.OrderLine;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;

@Component
public class Store {

	private final UserAccountManager employeeAccountManager;
	private final ItemCatalog itemCatalog;
	private final PizzaOrderRepository pizzaOrderRepo;

	private List<StaffMember> staffMemberList;
	private ArrayList<Oven> ovenList;
	private Pizza nextPizza;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog, PizzaOrderRepository pizzaOrderRepo) {

		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
		this.pizzaOrderRepo = pizzaOrderRepo;

		this.staffMemberList = new ArrayList<StaffMember>();
		this.ovenList = new ArrayList<Oven>();

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
			// updateUserAccount

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
			if (staffMember.getForename().equals(name)) {
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

	public Item findItemByIdentifier(String identifier, ItemType filter) {
		Iterable<Item> items;

		if (filter == null) {
			items = this.itemCatalog.findAll();
		} else {
			items = this.itemCatalog.findByType(filter);
		}

		for (Item item : items) {
			if (item.getId().getIdentifier().equals(identifier)) {
				return item;
			}
		}

		return null;
	}

	public static ItemType StringtoItemtype(String type) // use to remove
															// redundancy?!
	{
		switch (type) {
		default:
			return ItemType.FREEDRINK;
		case "DRINK":
			return ItemType.DRINK;
		case "INGREDIENT":
			return ItemType.INGREDIENT;
		case "PIZZA":
			return ItemType.PIZZA;
		case "SALAD":
			return ItemType.SALAD;
		case "Cutlery":
			return ItemType.CUTLERY;
		}
	}
	
	

	public void cleanUpItemCatalog() { // unused?
		Iterable<Item> items1 = itemCatalog.findAll();
		Iterable<Item> items2 = itemCatalog.findAll();

		for (Item item1 : items1) {
			for (Item item2 : items2) {
				if (item1.getName().equals(item2.getName()))
					itemCatalog.delete(item2);
			}

		}
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
						e.printStackTrace();
					}
					pizzaOrderRepo.save(order);
					return;
				}
			}
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
				ovenList.get(i).fill(nextPizza);
				System.out.println(ovenList.get(i).getPizza());

				return true;
			}
		}
		return false;
	}
}
