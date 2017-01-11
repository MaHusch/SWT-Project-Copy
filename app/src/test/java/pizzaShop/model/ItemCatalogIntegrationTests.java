package pizzaShop.model;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.*;

import java.util.Iterator;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.DataBaseSystem.CatalogHelper;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;

public class ItemCatalogIntegrationTests extends AbstractIntegrationTests {

	@Autowired
	ItemCatalog catalog;
	CatalogHelper helper;

	@Before
	public void setUp() {
		helper = new CatalogHelper(catalog);
	}

	@Test
	public void findbyItemTypeTest() {
		Iterable<Item> result = catalog.findByType(ItemType.FREEDRINK);
		assertThat(result, is(iterableWithSize(2)));
	}

	@Test
	public void stringtoItemtypeTest() {
		assertTrue(CatalogHelper.StringtoItemtype("PIZZA").equals(ItemType.PIZZA));
		assertTrue(CatalogHelper.StringtoItemtype("DRINK").equals(ItemType.DRINK));
		assertTrue(CatalogHelper.StringtoItemtype("FREEDRINK").equals(ItemType.FREEDRINK));
		assertTrue(CatalogHelper.StringtoItemtype("INGREDIENT").equals(ItemType.INGREDIENT));
		assertTrue(CatalogHelper.StringtoItemtype("SALAD").equals(ItemType.SALAD));
	}

	@Test
	public void creatNewItemTest() throws Exception {
		helper.createNewItem("test", "PIZZA", 3.4);
		Iterator<Item> it = catalog.findByName("test").iterator();
		assertTrue(it.hasNext());

		Item i = it.next();
		assertTrue(i.getName().equals("test"));
		assertTrue(i.getType().equals(ItemType.PIZZA));
		assertTrue(i.getPrice().getNumber().doubleValue() == 3.4);

		// empty String
		try {
			helper.createNewItem("", "DRINK", 3.4);
		} catch (Exception e) {
			assertTrue(e.getClass().toString().equals("class java.lang.IllegalArgumentException"));
		}

		// price lower zero
		try {
			helper.createNewItem("tes", "DRINK", -0.1);
		} catch (Exception e) {
			assertTrue(e.getClass().toString().equals("class java.lang.IllegalArgumentException"));
		}
	}

}
