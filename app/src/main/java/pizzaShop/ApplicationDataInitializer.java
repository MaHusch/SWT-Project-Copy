package pizzaShop;

import static org.salespointframework.core.Currencies.EURO;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.AccountSystem.Address;
import pizzaShop.model.AccountSystem.Admin;
import pizzaShop.model.AccountSystem.Baker;
import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.AccountSystem.Person;
import pizzaShop.model.AccountSystem.Seller;
import pizzaShop.model.AccountingSystem.SalaryThread;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.DataBaseSystem.StaffMemberRepository;
import pizzaShop.model.ManagementSystem.Pizzaqueue;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.Cutlery;
import pizzaShop.model.OrderSystem.Ingredient;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.Pizza;

/**
 * 
 * @author Florentin
 *  
 *  Class initializes data like staffmember or items for the ItemCatalog
 */
@Component
public class ApplicationDataInitializer implements DataInitializer {

	private final Accountancy accountancy;
	private final UserAccountManager employeeAccountManager;
	private final BusinessTime businessTime;
	private final CustomerRepository customerRepository;
	private final TanManagement tanManagement;
	private final Store store;
	private final ItemCatalog itemCatalog;
	private final StaffMemberRepository staffMemberRepository;

	/**
	 * gets the components via autowired
	 * @param accountancy
	 * @param employeeAccountManager
	 * @param businessTime
	 * @param customerRepository
	 * @param tanManagement
	 */
	@Autowired
	public ApplicationDataInitializer(Accountancy accountancy, UserAccountManager employeeAccountManager,
			BusinessTime businessTime, CustomerRepository customerRepository, TanManagement tanManagement, Store store, ItemCatalog itemCatalog, StaffMemberRepository staffMemberRepository) {
		this.accountancy = accountancy;
		this.employeeAccountManager = employeeAccountManager;
		this.businessTime = businessTime;
		this.customerRepository = customerRepository;
		this.tanManagement = tanManagement;
		this.store = store;
		this.itemCatalog = itemCatalog;
		this.staffMemberRepository = staffMemberRepository;
	}
	
	/**
	 * calls each initialize function
	 */
	@Override
	public void initialize() {

		initializeCatalog();
		initializeAccountancy();
		initializeCustomers();
		initializeUser();	
	}
	
	/**
	 * initializes users like deliverer and baker and puts them into the StaffMemberList
	 */
	private void initializeUser()
	{
		Admin admin = new Admin("Mustermann", "Max", "123456789");
		store.updateUserAccount(admin, "admin", "123", Role.of("ROLE_ADMIN"));
		
		Baker Baker_Eduardo_Pienso = new Baker("Pienso", "Eduardo", "2341241212");
		store.updateUserAccount(Baker_Eduardo_Pienso, "eddy", "pass", Role.of("ROLE_BAKER"));
		Deliverer Deliverer_Florentin_Doerre = new Deliverer("Pepper", "Roni", "015123456");
		store.updateUserAccount(Deliverer_Florentin_Doerre, "flo", "123", Role.of("ROLE_DELIVERER"));
		Deliverer Deliverer_Martin_Huschenbett = new Deliverer("Huschenbett", "Martin", "40918310");
		store.updateUserAccount(Deliverer_Martin_Huschenbett, "maddin", "qwe", Role.of("ROLE_DELIVERER"));
		Seller Seller_Hans_Bergstein = new Seller("Bergstein", "Hans", "492161268");
		store.updateUserAccount(Seller_Hans_Bergstein, "hans123", "qwe", Role.of("ROLE_SELLER"));
		
		store.getStaffMemberList().addAll(Arrays.asList(admin, Seller_Hans_Bergstein, Deliverer_Florentin_Doerre, Deliverer_Martin_Huschenbett, Baker_Eduardo_Pienso));
	}
	
	/**
	 * Itemcatalog with its items is initialized here
	 * @param itemCatalog ItemCatalog to fill 
	 * 
	 */
	private void initializeCatalog() {

		if (itemCatalog.findAll().iterator().hasNext()) {
			return;
		}

		Ingredient cheese = new Ingredient("Käse", Money.of(0.50, EURO));
		Ingredient mushroom = new Ingredient("Pilze", Money.of(1.00, EURO));
		Ingredient pineapple = new Ingredient("Ananas", Money.of(1.00, EURO));
		Ingredient onions = new Ingredient("Zwiebeln", Money.of(0.50, EURO));
		Ingredient paprika = new Ingredient("Paprika", Money.of(0.50, EURO));
		Ingredient bacon = new Ingredient("Bacon", Money.of(0.50, EURO));
		Ingredient chicken_stripes = new Ingredient("Hähnchenstreifen",Money.of(1.50, EURO));
		Ingredient spinach = new Ingredient("Spinat",Money.of(0.60, EURO));
		Ingredient olive = new Ingredient("Oliven",Money.of(0.90, EURO));
		Ingredient chili = new Ingredient("Chili",Money.of(0.70, EURO));
		Ingredient base = new Ingredient("Teig",Money.of(2.00, EURO));
		
		Pizza pizza1 = new Pizza("Barbecue", Money.of(0.00, EURO));
		pizza1.addIngredient(Arrays.asList(base,bacon,onions,paprika,cheese));
	
		Pizza pizza2 = new Pizza("BigApple", Money.of(0.00, EURO));
		pizza2.addIngredient(Arrays.asList(base,chicken_stripes,spinach,cheese));
	
		Pizza pizza3 = new Pizza("Mediteran", Money.of(0.00, EURO));
		pizza3.addIngredient(Arrays.asList(base,chicken_stripes,olive,cheese));
		
		Pizza pizza4 = new Pizza("Fungi", Money.of(0.0, EURO));
		pizza4.addIngredient(Arrays.asList(base,cheese,mushroom,onions));
		

		Pizza pizza5 = new Pizza("Diablo", Money.of(0.0, EURO));
		pizza5.addIngredient(Arrays.asList(base,cheese,mushroom,paprika,chili));
		
		
		Item beer = new Item("Desperados", Money.of(1.60, EURO), ItemType.DRINK);
		Item cola = new Item("Coca Cola", Money.of(2.50, EURO), ItemType.DRINK);
		Item water = new Item("BonAqua", Money.of(1.50, EURO), ItemType.DRINK);
		Item wine = new Item("Spätlese", Money.of(5.00, EURO),ItemType.DRINK);
		Item apple_spritzer = new Item("Lift-Apfelschorle",Money.of(1.50, EURO), ItemType.DRINK);
		Item freebeer = new Item("Sternburg", Money.of(0.0, EURO), ItemType.FREEDRINK); 
		Item freewine = new Item("Aldi-Wein", Money.of(0.0, EURO),ItemType.FREEDRINK);
		
																					
		Item salat1 = new Item("Ceasar-Salat", Money.of(2.0, EURO), ItemType.SALAD);
		Item salat2 = new Item("Chef-Salat",Money.of(3.0, EURO),ItemType.SALAD);
		
		

		itemCatalog.save(Arrays.asList(pizza1,pizza2,pizza3,pizza4,pizza5,spinach,wine,
										freewine,apple_spritzer,water,cola,
										cheese, mushroom,chicken_stripes,bacon,paprika, 
										beer, freebeer, salat1,salat2, pineapple, onions, base, olive));
		
		
	}
	
	/**
	 * Accountancy initialized here
	 */
	public void initializeAccountancy() 
	{
		
		/*AccountancyEntry ace1 = new AccountancyEntry(Money.of(50, "EUR"), "Einkauf");
		AccountancyEntry ace2 = new AccountancyEntry(Money.of(-200, "EUR"), "Diebstahl");
		AccountancyEntry ace3 = new AccountancyEntry(Money.of(536, "EUR"), "Großbestellung");
		accountancy.add(ace1);
		accountancy.add(ace2);
		accountancy.add(ace3);*/
		
		(new Thread(new SalaryThread(accountancy, businessTime, store))).start();
		
	}
	
	/**
	 * Customer initialized here
	 */
	public void initializeCustomers() 
	{	
		//Address a1 = new Address( "Dresden", "01324", "Müllerstraße", "5b");
		//Person p1 = new Person("Jürgens", "Dieter", "12345", a1);
		Customer cu1 = new Customer("Jürgens", "Dieter", "12345", "Dresden", "01324", "Müllerstraße", "5b");
		//Address a2 = new Address( "Dresden","01218","Sackgasse","42a");
		//Person p2 = new Person("Skywalker","Luke","23456", a1);
		Customer cu2 = new Customer("Skywalker","Luke","23456","Dresden","01218","Sackgasse","42a");
		/*
		cu2.addDeliveryAddress(new Address("Dresden","01218","Sumpfgasse","43a"));
		cu2.addDeliveryAddress(new Address("Dresden","01218","Sumpfgasse","43b"));
		cu2.addDeliveryAddress(new Address("Dresden","01218","Sumpfgasse","43c"));
		*/	
		tanManagement.confirmTan(tanManagement.generateNewTan(cu1.getPerson().getTelephoneNumber()));
		tanManagement.confirmTan(tanManagement.generateNewTan(cu2.getPerson().getTelephoneNumber()));
		cu2.setCutlery(new Cutlery(Money.of(15.0,EURO),businessTime.getTime()));
		customerRepository.save(cu1);
		customerRepository.save(cu2);
		System.out.println(tanManagement.getTan(customerRepository.save(cu1).getPerson().getTelephoneNumber()).getTanNumber());
		
	}
}
