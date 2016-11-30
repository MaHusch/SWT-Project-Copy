package unitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;
import pizzaShop.model.tan_management.TanStatus;

public class TanManagementTest {
	
	TanManagement TanMan;
	
	@Before
	public void setUp()  
	{
	
		TanMan = new TanManagement();
	}

	@Test
	public void test1() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		TanMan.confirmTan(newTan);
		
		assertEquals(newTan, TanMan.getTan(testTelephoneNumber));
		
		assertEquals(testTelephoneNumber, TanMan.getTelephoneNumber(newTan));
		
		Tan newTan2 = TanMan.getTan("222222");
		
		assertEquals(TanMan.NOT_FOUND_TAN, newTan2);
	}
	
	@Test
	public void checkTanTest() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		TanMan.confirmTan(newTan);
		
		assertTrue(TanMan.checkTan(newTan, testTelephoneNumber)); //check Tan either takes a Tan as a the first Parameter or a String 
		
		assertTrue(TanMan.checkTan(newTan.getTanNumber(), testTelephoneNumber));
		
		assertFalse(TanMan.checkTan(newTan.getTanNumber(), "222222"));
		
		assertFalse(TanMan.checkTan("00000", "123456"));
					
	}
	
	@Test
	public void invalidateTanTest() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);
		
		TanMan.confirmTan(newTan);
		
		TanMan.invalidateTan(newTan);
		
		assertEquals(TanStatus.USED,newTan.getStatus());
				
	}
	
	public void deleteNotConfirmedTanTest() {
		
		String testTelephoneNumber = "123456";
		
		Tan newTan = TanMan.generateNewTan(testTelephoneNumber);		
		
		assertTrue(TanMan.deleteNotConfirmedTan(newTan));
				
	}

}
