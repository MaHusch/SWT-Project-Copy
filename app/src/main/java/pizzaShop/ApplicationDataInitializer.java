package pizzaShop;

import static org.salespointframework.core.Currencies.EURO;

import java.util.Arrays;

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
import pizzaShop.model.catalog_item.Cutlery;
import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ItemCatalog;
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
		Deliverer Deliverer_Florentin_Doerre = new Deliverer("Doerre", "Florentin", "015123456");
		store.updateUserAccount(Deliverer_Florentin_Doerre, "flo", "123", Role.of("ROLE_DELIVERER"));
		Deliverer Deliverer_Martin_Huschenbett = new Deliverer("Huschenbett", "Martin", "40918310");
		store.updateUserAccount(Deliverer_Martin_Huschenbett, "maddin", "qwe", Role.of("ROLE_DELIVERER"));
		Seller Seller_Hans_Bergstein = new Seller("Bergstein", "Hans", "492161268");
		store.updateUserAccount(Seller_Hans_Bergstein, "hans123", "qwe", Role.of("ROLE_SELLER"));
		
		store.getStaffMemberList().addAll(Arrays.asList(Seller_Hans_Bergstein, Deliverer_Florentin_Doerre, Deliverer_Martin_Huschenbett, Baker_Eduardo_Pienso));
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
		
		Pizza pizza1 = new Pizza("pizza1", Money.of(2.50, EURO));
		Pizza pizza2 = new Pizza("pizza2", Money.of(2.50, EURO));
		Pizza pizza3 = new Pizza("pizza3", Money.of(2.50, EURO));
		Pizza custom = new Pizza("Basis",Money.of(2.0, EURO));
		Item beer = new Item("Desperados", Money.of(1.60, EURO), ItemType.DRINK);
		Item freebeer = new Item("Sternburg", Money.of(0.0, EURO), ItemType.FREEDRINK); // extra
		Cutlery cutlery1 = new Cutlery("PapasBesteck",Money.of(15.0, EURO), businessTime.getTime());																			// FreeDrink
																					// class?
		Item salat = new Item("Ceasar-Salad", Money.of(2.0, EURO), ItemType.SALAD);
		pizza1.addIngredient(mushroom);
		pizza1.addIngredient(cheese);
		
		Pizzaqueue pizzaQueue = store.getPizzaQueue();

		/*pizzaQueue.add(pizza1);
		pizzaQueue.add(pizza2);
		pizzaQueue.add(pizza3);*/

		itemCatalog.save(Arrays.asList(cheese, mushroom, pizza1, beer, freebeer, salat, pineapple, onions, custom, cutlery1));
		
		
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

		(new Thread(new SalaryThread(accountancy, businessTime))).start();
		
	}
	
	/**
	 * Customer initialized here
	 */
	public void initializeCustomers() 
	{	
		Customer cu1 = new Customer("Jürgens", "Dieter", "12345", "Dresden", "01324", "Müllerstraße", "5b");
		tanManagement.confirmTan(tanManagement.generateNewTan(cu1.getTelephoneNumber()));
		customerRepository.save(cu1);
		System.out.println(tanManagement.getTan(customerRepository.save(cu1).getTelephoneNumber()).getTanNumber());
	}
}
