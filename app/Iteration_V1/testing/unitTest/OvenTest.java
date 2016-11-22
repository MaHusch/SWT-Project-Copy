package unitTest;

import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.junit.*;
import org.salespointframework.useraccount.Role;

import kickstart.model.actor.Baker;
import kickstart.model.catalog_item.Pizza;
import kickstart.model.store.Oven;
import kickstart.model.store.Store;

public class OvenTest {
	
	Oven o1;
	
	Pizza p1;
	Pizza p2;
	
	Baker b1;
	
	@Before
	public void setUp() throws Exception{
		
		o1 = new Oven(Store.getInstance());
		
		p1 = new Pizza("p1",Money.of(2.50, EURO),cheese);
		p2 = new Pizza("p2",Money.of(2.50, EURO),cheese);
		
		b1 = new Baker("Pienso","Eduardo","2341241212","eddy","pass",Role.of("ROLE_BAKER"));
		
		
	}
	
	@Test
	public void testGetId(){
		
		assertEquals("ID should be 1", 1, o1.getId());
		
	}
	
	@Test
	public void testEmpty(){
		
		assertTrue("Oven should be Empty", true, o1.isEmpty());
		
		o1.fill(p1);
		
		assertTrue("Oven should not be Empty", false, o1.isEmpty());
		
	}
	
	@Test 	
	public void testRegisterObserver(){
		
		o1.registerObserver(b1);
		
		assertTrue("List should not be empty!", true, o1.getObservers().isEmpty());
		
		assertTrue("List should contain Baker Eduardo", true, o1.getObservers().get(0));
		
		
		
	}
	
	
	
	
	
	

}
