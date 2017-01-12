package pizzaShop.controller;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pizzaShop.AbstractWebIntegrationTests;
import pizzaShop.controller.CatalogController;
import pizzaShop.model.DataBaseSystem.ItemCatalog;

public class CatalogControllerWebIntegrationTests extends AbstractWebIntegrationTests {
	@Autowired CatalogController controller;
	@Autowired ItemCatalog catalog;

	
	@Test
	public void catalogViewMvcIntegrationTest() throws Exception {

		mvc.perform(get("/catalog")). 
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}
	
	/*@Test
	public void editViewMvcIntegrationTest() throws Exception {
		ProductIdentifier item = catalog.findAll().iterator().next().getId();
		Model model = new ExtendedModelMap();
		
		mvc.perform(post("/editedItem").param("pid", item.toString()).
				param("model", model.toString())).andExpect(status().isOk()).
				andExpect(model().attribute("item", is(not(null))));
		
	}*/
}
