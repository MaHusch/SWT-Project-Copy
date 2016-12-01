package unitTests;		// test Store befor

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.order.OrderIdentifier;

import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.store.PizzaOrder;

public class DelivererTest {

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
	
	/*@Test
	public void testaddOrderIdentifier()
	{
		d1.addOrder(oi1);
		assertTrue(d1.getOrders().contains(o1));
	}

	@Test
	public void testremoveOrderIdentifier()
	{
		assertEquals(oi1,d1.removeOrder(oi1));
		assertFalse(d1.getOrders().contains(o1));
		d1.clearOrders();
		assertTrue(d1.getOrders().isEmpty());
	}*/
	
}
