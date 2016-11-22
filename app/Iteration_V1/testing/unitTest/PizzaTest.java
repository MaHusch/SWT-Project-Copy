package unitTest;

import static org.junit.Assert.*;
import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;

public class PizzaTest {

	Pizza p1;
	Ingredient i1,i2;
	
	@Before
	public void setUp()  
	{
	
		i1 = new Ingredient("Tomato",Money.of(0.50, EURO));
		i2 = new Ingredient("Salami",Money.of(1.00, EURO));
		p1 = new Pizza("test",Money.of(1.10, EURO));
	}

	@Test
	public void testConstrutor() {
		p1 = new Pizza("Hawaii",Money.of(2.00, EURO));
		assertFalse(p1.getStatus());
		assertEquals(p1.getIngredients().size(),0);
		assertEquals(p1.getType(),ItemType.PIZZA);
		
	}
	
	@Test
	public void testSetter() 
	{
		
		p1.setStatus(true);
		assertTrue(p1.getStatus());
	}
	
	@Test
	public void testaddIngredient()
	{
		assertTrue(p1.addIngredient(i1));
		assertTrue(p1.getIngredients().contains(i1));
		assertFalse(p1.addIngredient(i1));
		
	}
	
	@Test
	public void testremoveIngredient()
	{
		p1.addIngredient(i1);
		p1.addIngredient(i2);
		assertEquals(p1.removeIngredient(i1), i1);
		assertFalse(p1.getIngredients().contains(i1));
		assertNull(p1.removeIngredient(i1));
		assertEquals(p1.removeIngredient(i2),i2);
		assertEquals(p1.getIngredients().size(),0);
	}
	

}
