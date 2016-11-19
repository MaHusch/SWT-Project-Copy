package unitTest;

import static org.junit.Assert.*;
import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import kickstart.model.catalog_item.Item;
import kickstart.model.catalog_item.ItemType;

public class ItemTest {

	    Item i1,i2;
		
		@Before
		public void setUp()
		{
			i1 = new Item("Beer",Money.of(3.44, EURO),ItemType.DRINK);
			i2 = new Item("FreeBeer",Money.of(3.44, EURO),ItemType.FREEDRINK); //should turn into 0.0
		}
		
		@Test
		public void testContructor() {
			assertEquals(i2.getPrice(),Money.of(0.0, EURO));
			//TODO: illegalArgument bei Name = "" oder price < 0 
		}
		
		@Test
		public void testGetter()
		{
			assertEquals(i1.getType(),ItemType.DRINK);
			assertEquals(i2.getType(),ItemType.FREEDRINK);
		}
		
}



