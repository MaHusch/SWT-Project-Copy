package pizzaShop.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.salespointframework.core.Currencies.EURO;

import java.util.Map;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import pizzaShop.AbstractIntegrationTests;

public class AccountingControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired
	AccountingController controller;
	@Autowired
	Accountancy accountancy;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void financesViewtest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.finances(model);

		assertThat(returnedView, is("finances"));

		Map<String, Object> map = model.asMap();

		assertNotNull(map.get("entries"));
		assertNotNull(map.get("currentDisplay"));
		assertNotNull(map.get("displayInterval"));
		assertNotNull(map.get("currentWeek"));
		assertNotNull(map.get("currentTime"));
		assertNotNull(map.get("weeklyGain"));
	}

	/*@Test
	public void createAccountancyEntryTest() {
		Integer i = null;
		String returnedView = controller.createEntry(i, "t");
		assertThat(returnedView, is("redirect:finances"));
		assertFalse(accountancyEntryfoundbyPrice(i));

		((Object) controller).createEntry(i, "");
		assertThat(returnedView, is("redirect:finances"));
		assertFalse(accountancyEntryfoundbyPrice(i));

		i = 3333333;
		controller.createEntry(i, "t");
		assertTrue(accountancyEntryfoundbyPrice(i));

	}*/

	public boolean accountancyEntryfoundbyPrice(Number i) {
		boolean contains = false;
		for (AccountancyEntry a : accountancy.findAll()) {
			if (a.getValue().getNumber().intValue() == 3333333)
				contains = true;
		}

		return contains;
	}

}
