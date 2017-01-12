package pizzaShop.model;		// test Store befor

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.OrderSystem.PizzaOrder;

public class DelivererIntegrationTest extends AbstractIntegrationTests {

	@Autowired PizzaOrderRepository pOR;
	Deliverer d1;
	
	
	@Before
	public void setUp() throws Exception 
	{
		d1 = new Deliverer("Horst", "Peter", "1234");
		
	}

	@Test
	public void testConstructor()
	{ 
		assertTrue(d1.getOrders().isEmpty());
		//assertNotNull(d1.getUserAccount());
	}
	
	@Test
	public void testGetter()
	{
		assertFalse(d1.getOrders().equals(null));
		assertFalse(d1.getAvailable());
	}
	
	@Test
	public void testSetter()
	{
		d1.checkIn();
		assertTrue(d1.getAvailable());
		d1.checkOut();
		assertFalse(d1.getAvailable());
		
	}
	
	@Test
	public void testaddOrderIdentifier()
	{
		PizzaOrder pO = new PizzaOrder();
		d1.addOrder(pO.getId());
		assertTrue(d1.getOrders().contains(pO.getId()));
	}

	@Test
	public void testremoveOrderIdentifier()
	{
		OrderIdentifier pOI = new PizzaOrder().getId();
		d1.addOrder(pOI);
		assertEquals(pOI,d1.removeOrder(pOI));
		assertFalse(d1.getOrders().contains(pOI));
		d1.clearOrders();
		assertTrue(d1.getOrders().isEmpty());
	}
	
}
