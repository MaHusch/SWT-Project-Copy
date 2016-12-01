package pizzaShop.model.store;

import java.util.ArrayList;
import java.util.Iterator;

import org.salespointframework.order.OrderLine;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.actor.Admin;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.tan_management.TanManagement;

@Component
public class Store {

	private static Store store = null;

	public static UserAccountManager employeeAccountManager;
	public static ItemCatalog itemCatalog;
	public PizzaOrderRepository pizzaOrderRepo;

	public static ArrayList<StaffMember> staffMemberList;
	public static Admin admin;

	private ArrayList<Oven> ovenList;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();

	public Store() {
	}

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog,
			PizzaOrderRepository pizzaOrderRepo) {

		this.employeeAccountManager = employeeAccountManager;
		this.staffMemberList = new ArrayList<StaffMember>();
		this.itemCatalog = itemCatalog;
		this.ovenList = new ArrayList<Oven>();
		this.admin = new Admin("Mustermann", "Max", "123456789");
		this.admin.updateUserAccount("admin", "123", Role.of("ROLE_ADMIN"));
		this.pizzaOrderRepo = pizzaOrderRepo;

		Oven oven1 = new Oven(this);
		Oven oven2 = new Oven(this);
		Oven oven3 = new Oven(this);
		Oven oven4 = new Oven(this);
		System.out.println(oven3.getId());

		this.store = this;
	}

	// Store is a singleton
	public static Store getInstance() {
		/*
		 * if (store == null) { store = new Store(); }
		 */

		return store;
	}

	public Pizzaqueue getPizzaQueue() {

		return pizzaQueue;
	}

	public ArrayList<Oven> getOvens() {

		return ovenList;
	}

	public StaffMember getStaffMemberByName(String name) {

		for (StaffMember staffMember : staffMemberList) {
			if (staffMember.getUsername().equals(name))
				return staffMember;
		}

		return null;
	}

	public PizzaOrder analyzeOrder(PizzaOrder order) {
		for (OrderLine l : order.getOrder().getOrderLines()) {
			Item temp = itemCatalog.findOne(l.getProductIdentifier()).get();
			if (temp.getType().equals(ItemType.PIZZA)) {
				for (int i = 0; i < l.getQuantity().getAmount().intValue(); i++) {
					((Pizza) temp).addOrder(order.getId());
					System.out.println("Order ID in Analyze: " + order.getId());
					pizzaQueue.add(((Pizza) temp));
					order.addAsUnbaked();
					System.out.println("Analyze Order: " + order.getUnbakedPizzas());
				}

			}
		}
		if(order.getUnbakedPizzas() == 0){
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

	public void cleanUpItemCatalog() {
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
}
