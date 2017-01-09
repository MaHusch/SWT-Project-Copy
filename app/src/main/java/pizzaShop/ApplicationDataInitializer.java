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

import pizzaShop.model.actor.Admin;
import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.Seller;
import pizzaShop.model.catalog.Cutlery;
import pizzaShop.model.catalog.Ingredient;
import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemCatalog;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.catalog.Pizza;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.Pizzaqueue;
import pizzaShop.model.store.SalaryThread;
import pizzaShop.model.store.StaffMemberRepository;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.TanManagement;

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
		
		Pizza custom = new Pizza("Basis",Money.of(2.00, EURO));
		Pizza pizza1 = new Pizza("Barbecue", Money.of(2.00, EURO));
		pizza1.addIngredient(Arrays.asList(bacon,onions,paprika,cheese));
	
		Pizza pizza2 = new Pizza("BigApple", Money.of(2.00, EURO));
		pizza2.addIngredient(Arrays.asList(chicken_stripes,spinach,cheese));
	
		Pizza pizza3 = new Pizza("Mediteran", Money.of(2.00, EURO));
		pizza3.addIngredient(Arrays.asList(chicken_stripes,olive,cheese));
		
		
		Item beer = new Item("Desperados", Money.of(1.60, EURO), ItemType.DRINK);
		Item cola = new Item("Coca Cola", Money.of(2.50, EURO), ItemType.DRINK);
		Item water = new Item("BonAqua", Money.of(1.50, EURO), ItemType.DRINK);
		Item wine = new Item("Spätlese", Money.of(5.00, EURO),ItemType.DRINK);
		Item apple_spritzer = new Item("Lift-Apfelschorle",Money.of(1.50, EURO), ItemType.DRINK);
		Item freebeer = new Item("Sternburg", Money.of(0.0, EURO), ItemType.FREEDRINK); 
		Item freewine = new Item("Aldi-Wein", Money.of(0.0, EURO),ItemType.FREEDRINK);
		
																					
		Item salat1 = new Item("Ceasar-Salat", Money.of(2.0, EURO), ItemType.SALAD);
		Item salat2 = new Item("Chef-Salat",Money.of(3.0, EURO),ItemType.SALAD);
		
		

		itemCatalog.save(Arrays.asList(pizza1,pizza2,pizza3,spinach,wine,freewine,apple_spritzer,water,cola,
						cheese, mushroom,chicken_stripes,bacon,paprika, 
						beer, freebeer, salat1,salat2, pineapple, onions, custom, olive));
		
		
	}
	
	/**
	 * Accountancy initialized here
	 */
	public void initializeAccountancy() 
	{
		
		AccountancyEntry ace1 = new AccountancyEntry(Money.of(50, "EUR"), "Einkauf");
		AccountancyEntry ace2 = new AccountancyEntry(Money.of(-200, "EUR"), "Diebstahl");
		AccountancyEntry ace3 = new AccountancyEntry(Money.of(536, "EUR"), "Großbestellung");
		accountancy.add(ace1);
		accountancy.add(ace2);
		accountancy.add(ace3);
		
		HashMap<Role, Integer> incomeMap = new HashMap<Role, Integer>();
		incomeMap.put(Role.of("ROLE_ADMIN"), 400);
		incomeMap.put(Role.of("ROLE_BAKER"), 250);
		incomeMap.put(Role.of("ROLE_SELLER"), 300);
		incomeMap.put(Role.of("ROLE_DELIVERER"), 200);
		
		(new Thread(new SalaryThread(accountancy, businessTime, store, incomeMap))).start();
		
	}
	
	/**
	 * Customer initialized here
	 */
	public void initializeCustomers() 
	{	
		Customer cu1 = new Customer("Jürgens", "Dieter", "12345", "Dresden", "01324", "Müllerstraße", "5b");
		Customer cu2 = new Customer("Skywalker","Luke","23456","Dresden","01218","Sackgasse","42a");
		tanManagement.confirmTan(tanManagement.generateNewTan(cu1.getPerson().getTelephoneNumber()));
		tanManagement.confirmTan(tanManagement.generateNewTan(cu2.getPerson().getTelephoneNumber()));
		cu2.setCutlery(new Cutlery(Money.of(15.0,EURO),businessTime.getTime()));
		customerRepository.save(cu1);
		customerRepository.save(cu2);
		System.out.println(tanManagement.getTan(customerRepository.save(cu1).getPerson().getTelephoneNumber()).getTanNumber());
	}
}
