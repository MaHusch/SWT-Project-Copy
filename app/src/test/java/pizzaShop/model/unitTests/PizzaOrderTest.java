package pizzaShop.model.unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanStatus;
import pizzaShop.model.OrderSystem.PizzaOrder;
import pizzaShop.model.OrderSystem.PizzaOrderStatus;

public class PizzaOrderTest {
	
	PizzaOrder po1;
	UserAccount ua1;
	Tan t1;
	UserAccountManager uam;
	Customer c1;
	
	@Before
	public void setUp()  
	{
		c1 = new Customer();
		ua1 = new UserAccount();
		t1 = new Tan("11293", TanStatus.VALID);
		po1 = new PizzaOrder(ua1, Cash.CASH, t1, false,c1); 
	}
	
	@Test
	public void testConstructor(){
		assertEquals(t1, po1.getTan());
		assertEquals(po1.getOrder().getId(), po1.getId());
	}
	
	@Test
	public void testUnbaked(){
		assertTrue(po1.getUnbakedPizzas() == 0);
		assertTrue(po1.addAsUnbaked() == 1);
		assertTrue(po1.markAsBaked() == 0 && po1.getOrderStatus().equals(PizzaOrderStatus.READY));
		try{
			po1.markAsBaked();
		}catch(AssertionError err){
			assertEquals("No unbaked Pizza left!", err.getMessage());
		}
	}
	
	@Test
	public void testOrderStatus(){
		assertEquals(po1.getOrderStatus(), PizzaOrderStatus.OPEN);
		po1.completeOrder();
		assertEquals(po1.getOrderStatus(), PizzaOrderStatus.COMPLETED);
		po1.deliverOrder();
		assertEquals(po1.getOrderStatus(), PizzaOrderStatus.DELIVERING);
		po1.cancelOrder();
		assertEquals(po1.getOrderStatus(), PizzaOrderStatus.CANCELLED);
	}
	
	@Test
	public void testRemark()
	{
		assertEquals("-",po1.getRemark());
		po1.setRemark("test");
		assertEquals("test",po1.getRemark());
		
	}
	
	
	
}
