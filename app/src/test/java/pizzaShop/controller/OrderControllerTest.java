package pizzaShop.controller;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;

public class OrderControllerTest extends AbstractIntegrationTests {
	@Autowired
	OrderController controller;
	@Autowired
	OrderManager<Order> orderManager;
	@Autowired
	PizzaOrderRepository pizzaOrderRepository;
	@Autowired
	Store store;

	@Test
	public void orderViewtest() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.pizzaOrder(model);

		assertEquals(returnedView, "orders");

		Map<String, Object> map = model.asMap();

		assertNotNull(map.get("uncompletedOrders"));
		assertNotNull(map.get("completedOrders"));
		assertNotNull(map.get("deliverers"));
		assertNotNull(map.get("error"));
	}

}
