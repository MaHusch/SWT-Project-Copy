package pizzaShop;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.Seller;
import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.Oven;
import pizzaShop.model.store.Pizzaqueue;
import pizzaShop.model.store.SalaryThread;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.TanManagement;

@Component
public class ApplicationDataInitializer implements DataInitializer {

	private final Accountancy accountancy;
	private final UserAccountManager userAccountManager;
	private final BusinessTime businessTime;
	private final CustomerRepository customerRepository;
	private final TanManagement tanManagement;

	@Autowired
	public ApplicationDataInitializer(Accountancy accountancy, UserAccountManager userAccountManager,
			BusinessTime businessTime, CustomerRepository customerRepository, TanManagement tanManagement) {
		this.accountancy = accountancy;
		this.userAccountManager = userAccountManager;
		this.businessTime = businessTime;
		this.customerRepository = customerRepository;
		this.tanManagement = tanManagement;
	}

	@Override
	public void initialize() {

		initializeCatalog(Store.itemCatalog);
		initializeAccountancy();
		initializeCustomers();
		initializeUser();	
	}
	
	private void initializeUser()
	{
		Baker Baker_Eduardo_Pienso = new Baker("Pienso", "Eduardo", "2341241212", "eddy", "pass");
		Deliverer Deliverer_Florentin_Dörre = new Deliverer("Doerre", "Florentin", "015123456", "flo", "123");
		Seller Seller_Hans_Bergstein = new Seller("Bergstein", "Hans", "492161268", "hans123", "qwe");
			
		Store.staffMemberList.add(Seller_Hans_Bergstein);
		Store.staffMemberList.add(Deliverer_Florentin_Dörre);
		Store.staffMemberList.add(Baker_Eduardo_Pienso);
	}

	private void initializeCatalog(ItemCatalog itemCatalog) {

		if (Store.itemCatalog.findAll().iterator().hasNext()) {
			return;
		}

		Ingredient cheese = new Ingredient("Käse", Money.of(0.50, EURO));
		Ingredient mushroom = new Ingredient("Pilze", Money.of(1.00, EURO));
		Ingredient pineapple = new Ingredient("Ananas", Money.of(1.00, EURO));
		Ingredient oniens = new Ingredient("Zwiebeln", Money.of(0.50, EURO));
		
		Pizza pizza1 = new Pizza("pizza1", Money.of(2.50, EURO));
		Pizza pizza2 = new Pizza("pizza2", Money.of(2.50, EURO));
		Pizza pizza3 = new Pizza("pizza3", Money.of(2.50, EURO));
		Pizza custom = new Pizza("Basis",Money.of(2.0, EURO));
		Item beer = new Item("Beer", Money.of(1.60, EURO), ItemType.DRINK);
		Item freebeer = new Item("Beer", Money.of(0.0, EURO), ItemType.FREEDRINK); // extra
																					// FreeDrink
																					// class?
		Item salat = new Item("Salad", Money.of(2.0, EURO), ItemType.SALAD);
		pizza1.addIngredient(mushroom);
		pizza1.addIngredient(cheese);

		Pizzaqueue pizzaQueue = Store.getInstance().getPizzaQueue();

		pizzaQueue.add(pizza1);
		pizzaQueue.add(pizza2);
		pizzaQueue.add(pizza3);

		Store.itemCatalog.save(cheese);
		Store.itemCatalog.save(mushroom);
		Store.itemCatalog.save(pizza1);
		Store.itemCatalog.save(beer);
		Store.itemCatalog.save(freebeer);
		Store.itemCatalog.save(salat);
		Store.itemCatalog.save(pineapple);
		Store.itemCatalog.save(oniens);
		Store.itemCatalog.save(custom);

		/*Iterable<Item> test = Store.itemCatalog.findByType(ItemType.INGREDIENT);
		
		for(Item i : test)
		{
			System.out.println(i.getName());
		}*/
	}

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

	public void initializeCustomers() 
	{
		tanManagement.generateNewTan("2223333");
		tanManagement.generateNewTan("4445555");
		
		Customer cu1 = new Customer("Jürgens", "Dieter", "12345");
		tanManagement.confirmTan(tanManagement.generateNewTan(cu1.getTelephoneNumber()));
		customerRepository.save(cu1);
		System.out.println(tanManagement.getTan(customerRepository.save(cu1).getTelephoneNumber()).getTanNumber());
	}
}