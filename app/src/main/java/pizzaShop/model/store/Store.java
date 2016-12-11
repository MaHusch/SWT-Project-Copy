package pizzaShop.model.store;

import static org.salespointframework.core.Currencies.EURO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.javamoney.moneta.Money;
import org.salespointframework.order.OrderLine;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog_item.Cutlery;
import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;

@Component
public class Store {

	private final UserAccountManager employeeAccountManager;
	private final ItemCatalog itemCatalog;
	private final PizzaOrderRepository pizzaOrderRepo;
	private final StaffMemberRepository staffMemberRepository;
	private final CustomerRepository customerRepository;
	
	private List<StaffMember> staffMemberList; // why List and Repository for StaffMember?
	private ArrayList<Oven> ovenList;
	private Pizza nextPizza;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog, 
			PizzaOrderRepository pizzaOrderRepo, StaffMemberRepository staffMemberRepository,CustomerRepository customerRepository) {

		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
		this.pizzaOrderRepo = pizzaOrderRepo;
		this.staffMemberRepository = staffMemberRepository;
		this.customerRepository = customerRepository;
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
	
	public void createNewItem(String name, String type, Number price) throws Exception
	{
		Item newItem;
		
		ItemType itype = Store.StringtoItemtype(type);
		
		if(name.isEmpty()) 
		{
			throw new IllegalArgumentException("Name darf nicht leer sein");
		}
		
		if(price.floatValue() < 0)
		{
			throw new IllegalArgumentException("Preis darf nicht negativ sein");
		}
		
		if(itype.equals(ItemType.PIZZA))
		{
			newItem = new Pizza(name,Money.of(price, EURO));	
		}
		else if(type.equals(ItemType.CUTLERY))
		{
			newItem = new Ingredient(name,Money.of(price, EURO));
		}
		else
		{	
			newItem = new Item(name,Money.of(price, EURO),itype);
		}
		
		itemCatalog.save(newItem);
	
	}

	public void saveEditedItem(Item editedItem, String name, String type , Number price) throws Exception
	{
		if(editedItem.equals(null)) throw new NullPointerException("zu editierendes Item existiert nicht");
		if(name.isEmpty()) throw new IllegalArgumentException("Name darf nicht leer sein");
		if(price.floatValue() < 0) throw new IllegalArgumentException("Preis darf nicht negativ sein");
		
		ItemType newType = Store.StringtoItemtype(type);
		
		if(editedItem.getType().equals(newType))
		{
		itemCatalog.delete(editedItem); //altes Element rauslöschen
		editedItem.setName(name);
		System.out.println(editedItem.getName());
		editedItem.setPrice(Money.of(price, EURO));
		itemCatalog.save(editedItem); 
		
		}
		else
		{
			System.out.println("anderer Itemtyp --> neues Item");
			itemCatalog.delete(editedItem);
			this.createNewItem(name, type, price);
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
	
	public boolean lentCutlery(Customer customer, LocalDateTime time)
	{
		Cutlery cutlery = new Cutlery("Essgarnitur",Money.of(15.0, EURO),time);
		if(customer.equals(null)) return false;
		//if(!customer.getCutlery().equals(null)) return false; // has to return his lent cutlery before
		

		customer.setCutlery(cutlery);
		
		this.customerRepository.save(customer);
		
		return true;
	}
	
	/**
	 * Function for returning a cutlery lent by a customer
	 * @param lost <code> true </code> if customer lost his {@link catalog_item.Cutlery}, <code> false </code> if he returns it properly
	 * @param customer customer who wants to return his {@link catalog_item.Cutlery}
	 * @throws Exception when customer hasn't lent a {@link catalog_item.Cutlery} beforehand
	 */
	public void returnCutlery(boolean lost, Customer customer) throws Exception
	{
		if(customer.getCutlery().equals(null)) 
			throw new NullPointerException("Kunde hatte keine Essgarnitur ausgeliehen bzw ist schon verfallen");
		
		if(lost)
		{
			//TODO: accountancy entry
			
		}
		customer.setCutlery(null);
	}
	 
}
