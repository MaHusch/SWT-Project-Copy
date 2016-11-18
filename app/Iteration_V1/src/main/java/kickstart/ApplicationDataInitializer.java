package kickstart;

import java.time.Duration;
import java.util.ArrayList;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.salespointframework.core.Currencies.*;

import kickstart.model.actor.Admin;
import kickstart.model.actor.Baker;
import kickstart.model.actor.Seller;
import kickstart.model.catalog_item.Drink;
import kickstart.model.catalog_item.FreeDrink;
import kickstart.model.catalog_item.Ingredient;
import kickstart.model.catalog_item.Pizza;
import kickstart.model.catalog_item.Salad;
import kickstart.model.store.Oven;
import kickstart.model.store.Pizzaqueue;
import kickstart.model.store.SalaryThread;
import kickstart.model.store.StaffMemberRepository;
import kickstart.model.store.Store;

@Component
public class ApplicationDataInitializer implements DataInitializer {

	private final Accountancy accountancy;
	private final UserAccountManager userAccountManager;
	private final BusinessTime businessTime;

	@Autowired
	public ApplicationDataInitializer(Accountancy accountancy, UserAccountManager userAccountManager,
			BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.userAccountManager = userAccountManager;
		this.businessTime = businessTime;
	}

	@Override
	public void initialize() {
		
		
		Seller Seller_Hans_Bergstein = new Seller("Bergstein","Hans","492161268","hans123", "qwe", Role.of("ROLE_SELLER"));
		
		/*************************************BAKER************************************/
		Baker Baker_Eduardo_Pienso = new Baker("Pienso","Eduardo","2341241212","eddy","pass",Role.of("ROLE_BAKER"));
		
		
		ArrayList<Oven> ovenList = Store.getInstance().getOvens();
		for(Oven oven : ovenList){			
			Baker_Eduardo_Pienso.addOven(oven);
		}
		/*****************************************************************************/
		
		
		/*********************************ACCOUNTANY************************************/
		AccountancyEntry ace1 = new AccountancyEntry(Money.of(50, "EUR"), "Einkauf");
		AccountancyEntry ace2 = new AccountancyEntry(Money.of(-200, "EUR"), "Diebstahl");
		AccountancyEntry ace3 = new AccountancyEntry(Money.of(536, "EUR"), "Großbestellung");
		accountancy.add(ace1);
		accountancy.add(ace2);
		accountancy.add(ace3);

		(new Thread(new SalaryThread(accountancy, businessTime))).start();
		/*****************************************************************************/
		
		
		/*********************************ITEM_CATALOG************************************/
		if (Store.itemCatalog.findAll().iterator().hasNext()) {
			return;
		}
		
		Ingredient cheese = new Ingredient("Cheese",Money.of(0.50, EURO));
		Ingredient mushroom = new Ingredient("mushrooms",Money.of(1.00, EURO));
		Pizza pizza1 = new Pizza("pizza1",Money.of(2.50, EURO),cheese);
		Drink beer = new Drink("Beer",Money.of(1.60, EURO));
		FreeDrink freebeer = new FreeDrink("Beer");
		Salad salat = new Salad("Salad",Money.of(2.0, EURO));
		pizza1.addIngredient(mushroom);
		
		Pizzaqueue pizzaQueue = Store.getInstance().getPizzaQueue();
		
		pizzaQueue.add(pizza1);
		
		Store.itemCatalog.save(cheese);
		Store.itemCatalog.save(mushroom);
		Store.itemCatalog.save(pizza1);
		Store.itemCatalog.save(beer);
		Store.itemCatalog.save(freebeer);
		Store.itemCatalog.save(salat);
		
		
		/*****************************************************************************/
		// TODO Auto-generated method stub

	}

}
