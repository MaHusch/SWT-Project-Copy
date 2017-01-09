package pizzaShop.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.catalog.ItemType;


public class CatalogControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired CatalogController controller;

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
}
