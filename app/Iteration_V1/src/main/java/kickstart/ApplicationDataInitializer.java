package kickstart;

import java.time.Duration;

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
import kickstart.model.actor.Seller;
import kickstart.model.catalog_item.Drink;
import kickstart.model.catalog_item.FreeDrink;
import kickstart.model.catalog_item.Ingredient;
import kickstart.model.catalog_item.Pizza;
import kickstart.model.catalog_item.Salad;
import kickstart.model.store.SalaryThread;
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
		
		Seller Hans_Bergstein_Seller = new Seller("Bergstein","Hans","492161268","hans123", "qwe", Role.of("ROLE_SELLER"));
		
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
