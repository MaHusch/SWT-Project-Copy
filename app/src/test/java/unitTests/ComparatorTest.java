package unitTests;

import static org.junit.Assert.*;
import static org.salespointframework.core.Currencies.EURO;

import java.util.Comparator;

import org.javamoney.moneta.Money;
import org.junit.Before;
import org.junit.Test;

import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.catalog.NameComparator;
import pizzaShop.model.catalog.PriceComparator;

public class ComparatorTest {

	Comparator<Item> name_comparator_asc;
	Comparator<Item> name_comparator_desc;
	Comparator<Item> price_comparator_asc;
	Comparator<Item> price_comparator_desc;
	Item i1;
	Item i2 ;
	
	@Before
	public void setUp() throws Exception {
		 name_comparator_asc = new NameComparator(true);
		name_comparator_desc = new NameComparator(false);
		 price_comparator_asc = new PriceComparator(true);
		 price_comparator_desc = new PriceComparator(false);
		i1 = new Item("aaab", Money.of(3.4, EURO),ItemType.SALAD);
		i2= new Item("aaaa", Money.of(3.6, EURO),ItemType.DRINK);
	}

	@Test
	public void compareEqualItemstest() {
		assertTrue(name_comparator_asc.compare(i1, i1) == 0);
		assertTrue(name_comparator_desc.compare(i1, i1) == 0);
		assertTrue(price_comparator_desc.compare(i1, i1) == 0);
		assertTrue(name_comparator_asc.compare(i1, i1) == 0);
	}
	
	@Test
	public void compareTwoItemsTest()
	{
		assertTrue(name_comparator_asc.compare(i1, i2) > 0);
		assertTrue(name_comparator_desc.compare(i1, i2) < 0);
		assertTrue(price_comparator_desc.compare(i1, i2) > 0);
		assertTrue(price_comparator_asc.compare(i1, i2) < 0);
	}

}
