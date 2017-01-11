package pizzaShop.controller;

import java.util.Comparator;

import pizzaShop.model.OrderSystem.Item;

/**
 * NameComparatorClass for comparing 2 {@link Item} based on their name
 * 
 * @author Florentin Dörre
 *
 */
public class NameComparator implements Comparator<Item> {
	int ascending;

	/**
	 * Constructor
	 * 
	 * @param ascending
	 *            to decide whether the {@link Item}s are compared ascending or
	 *            descending
	 */
	public NameComparator(boolean ascending) {
		if (ascending)
			this.ascending = 1;
		else
			this.ascending = -1;
	}

	/**
	 * main compare function
	 */
	@Override
	public int compare(Item o1, Item o2) {
		if ((o1.getName() == null) || (o2.getName() == null))
			return 0;
		return ascending * o1.getName().compareToIgnoreCase(o2.getName());

	}

}
