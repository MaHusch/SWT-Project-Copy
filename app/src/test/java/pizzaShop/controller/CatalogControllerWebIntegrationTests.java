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

public class CatalogControllerWebIntegrationTests extends AbstractWebIntegrationTests {
	@Autowired CatalogController controller;

	/**
	 * Sample integration test using fake HTTP requests to the system and using the expectations API to define
	 * constraints.
	 */
	@Test
	public void catalogViewMvcIntegrationTest() throws Exception {

		mvc.perform(get("/catalog")). 
				andExpect(status().isOk()).
				andExpect(model().attribute("catalog", is(not(emptyIterable()))));
	}
}
