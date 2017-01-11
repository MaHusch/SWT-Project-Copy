package pizzaShop.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemCatalog;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.catalog.NameComparator;
import pizzaShop.model.store.ErrorClass;

public class CatalogControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired
	CatalogController controller;
	@Autowired
	ItemCatalog catalog;

	/**
	 * Integration test for an individual controller.
	 */
	@Test
	@SuppressWarnings("unchecked")
	public void catalogViewTest() {

		Model model = new ExtendedModelMap();

		String returnedView = controller.showCatalog(model);

		assertThat(returnedView, is("catalog"));

		Iterable<Object> items = (Iterable<Object>) model.asMap().get("items");
		ItemType[] itemTypes = (ItemType[]) model.asMap().get("ItemType");

		assertTrue(itemTypes.length == 5);
		assertNotNull(items);
	}

	@Test
	public void edititemViewTest() {
		Model model = new ExtendedModelMap();
		Iterator<Item> it = catalog.findAll().iterator();

		if (it.hasNext()) {
			Item i = it.next();
			ProductIdentifier id = i.getId();
			String returnedView = controller.directToEditItem(model, id);

			assertThat(returnedView, is("addItem"));

			Item model_item = (Item) model.asMap().get("item");
			ErrorClass e = (ErrorClass) model.asMap().get("error");

			assertThat(e.getError(), is(false));
			assertThat(i, is(model_item));
		}
	}

	@Test
	public void createItemTest() {
		Number price = 3.4;
		String returnedView = controller.createItem("name", price, "INGREDIENT");

		assertThat(returnedView, is("redirect:catalog"));
		assertNotNull(catalog.findByName("name").iterator().next());

		// false inputs
		returnedView = controller.createItem("", price, "INGREDIENT");
		assertThat(returnedView, is("redirect:addItem"));
		assertThat(catalog.findByName("").iterator().hasNext(), is(false));

	}

	@Test
	public void saveItemTest() {
		Model model = new ExtendedModelMap();
		Item i = catalog.findAll().iterator().next();
		ProductIdentifier id = i.getId();
		String name = "newName";
		Number price = 5.5;
		String type = i.getType().toString();

		String returnedView = controller.saveItem(model, id, name, price, type);

		assertThat(returnedView, is("redirect:catalog"));

		Item new_Item = catalog.findOne(id).orElse(null);
		assertTrue(new_Item != null);
		assertThat(new_Item.getName(), is(name));
		assertThat(new_Item.getPrice().getNumber().doubleValueExact(), is(price));
		assertTrue(new_Item.getType().toString().equals(type));

		// false input
		returnedView = controller.saveItem(model, id, "", price, type);
		assertThat(returnedView, is("addItem"));

		Map<String, Object> map = model.asMap();
		ErrorClass e = (ErrorClass) map.get("error");
		Item item = (Item) map.get("item");
		assertThat(e.getError(), is(true));
		assertThat(item, is(i));
	}

	// filtered Items werden nicht überprüft
	@Test
	public void filterCatalogTest() {
		Model model = new ExtendedModelMap();
		String selection = "Getränke";
		String filter = "von A bis Z";

		String returnedView = controller.filterCatalog(model, selection, filter);
		assertThat(returnedView, is("catalog"));

		Map<String, Object> map = model.asMap();
		ArrayList<Item> map_items = (ArrayList<Item>) map.get("items");
		NameComparator comp = new NameComparator(true);

		if (map_items.size() >= 2) {
			for (int i = 0; i+1 < map_items.size(); i++) {
				Item i1 = map_items.get(i);
				Item i2 = map_items.get(i+1);
				assertTrue(comp.compare(i1, i2) <= 0);
			}
		}

		assertThat(map.get("lastfilter"), is(filter));
		assertThat(map.get("lastselection"), is(selection));

	}
}
