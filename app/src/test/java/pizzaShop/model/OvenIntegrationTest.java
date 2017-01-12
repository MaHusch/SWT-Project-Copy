package pizzaShop.model;

import static org.junit.Assert.*;
import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.junit.*;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.AbstractIntegrationTests;
import pizzaShop.model.AccountSystem.Baker;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.OrderSystem.Pizza;
import pizzaShop.model.ProductionSystem.Oven;

public class OvenIntegrationTest extends AbstractIntegrationTests{
	
	Oven o1;
	Oven o2;
	Oven o3;
	
	Pizza p1;
	Pizza p2;
	
	Baker b1;
	@Autowired Store store;
	@Autowired BusinessTime time;
	
	@Before
	public void setUp() throws Exception{
		
		
		o1 = new Oven(store);
		o2 = new Oven(store);
		o3 = new Oven(store);
		p1 = new Pizza("p1",Money.of(2.50, EURO));
		p2 = new Pizza("p2",Money.of(2.50, EURO));
		
		b1 = new Baker("Pienso","Eduardo","2341241212");
		
		
	}
	
	@Test
	public void constructorTest(){
		// 3 ovens via initializer 
		store.getOvens().clear();
		assertEquals(o1.getId(), 4);
		assertEquals(o2.getId(), 5);
		assertEquals(o3.getId(), 6);
		store.getOvens().add(o1);
		store.getOvens().add(o2);
		store.getOvens().add(o3);
		assertEquals(store.getOvens().size(), 3);
		assertTrue(o1.isEmpty());
		
		
	}
	
	@Test
	public void fillTest(){
		
		assertTrue(o1.fill(p1, time));
		assertFalse(o1.isEmpty());
		assertEquals(o1.getPizza(), p1);
		assertFalse(o1.fill(p2, time));
		assertTrue(o1.notifyObservers(p1));
	}
	
	
	

	
	
	
	
	
	

}
