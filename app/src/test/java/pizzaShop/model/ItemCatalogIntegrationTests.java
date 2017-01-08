package pizzaShop.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.store.ItemCatalog;


public class ItemCatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired ItemCatalog catalog;
	
	@Test
	public void findbyItemType() {
		Iterable<Item> result = catalog.findByType(ItemType.FREEDRINK);
		assertThat(result, is(iterableWithSize(2)));	
	}

}
