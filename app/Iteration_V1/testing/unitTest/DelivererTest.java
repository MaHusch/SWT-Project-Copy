package unitTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kickstart.model.actor.Deliverer;
import kickstart.model.store.PizzaOrder;

public class DelivererTest {

	Deliverer d1;
	PizzaOrder o1; // TODO : test
	
	@Before
	public void setUp() throws Exception 
	{
		d1 = new Deliverer("Horst", "Peter", "1234");
		
	}

	/*@Test
	public void testConstructor() { // TODO
		
	}*/
	
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

	//TODO : test List<OrderIdentifier>
}
