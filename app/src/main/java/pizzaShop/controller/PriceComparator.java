package pizzaShop.controller;

import java.util.Comparator;

import pizzaShop.model.OrderSystem.Item;

/**
 * class to compare 2 {@link Item} based on their price
 * 
 * @author Florentin
 *
 */
public class PriceComparator implements Comparator<Item> {
	int ascending;

	/**
	 * 
	 * @param ascending
	 *            whether the items should be compared ascending or descending
	 */
	public PriceComparator(boolean ascending) {
		if (ascending)
			this.ascending = 1;
		else
			this.ascending = -1;
	}

	@Override
	public int compare(Item o1, Item o2) {

		if (o1.getPrice().isGreaterThan(o2.getPrice()))
			return ascending;
		else if (o1.getPrice().equals(o2.getPrice()))
			return 0;
		return -1 * ascending;
	}

}
