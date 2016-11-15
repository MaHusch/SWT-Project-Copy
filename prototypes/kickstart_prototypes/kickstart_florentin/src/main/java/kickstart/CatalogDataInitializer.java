package kickstart;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import kickstart.model.*;

import static org.salespointframework.core.Currencies.*;

@Component
public class CatalogDataInitializer implements DataInitializer {
	
	private final ItemCatalog itemCatalog;
	
	@Autowired
	public CatalogDataInitializer(ItemCatalog itemCatalog)
	{
		Assert.notNull(itemCatalog, "ItemCatalog must not be null!");
		this.itemCatalog = itemCatalog;
	}

	@Override
	public void initialize() {
		initializeCatalog(itemCatalog);
		
	}
	
	private void initializeCatalog(ItemCatalog itemCatalog)
	{
		if (itemCatalog.findAll().iterator().hasNext()) {
			return;
		}
		
		Ingredient cheese = new Ingredient("Cheese",Money.of(0.50, EURO));
		Ingredient mushroom = new Ingredient("mushrooms",Money.of(1.00, EURO));
		Pizza pizza1 = new Pizza("pizza1",Money.of(2.50, EURO),cheese);
		Drink beer = new Drink("Beer",Money.of(1.60, EURO));
		FreeDrink freebeer = new FreeDrink("Beer");
		Salad salat = new Salad("Salad",Money.of(2.0, EURO));
		pizza1.addIngredient(mushroom);
		
		itemCatalog.save(cheese);
		itemCatalog.save(mushroom);
		itemCatalog.save(pizza1);
		itemCatalog.save(beer);
		itemCatalog.save(freebeer);
		itemCatalog.save(salat);
	}

}
