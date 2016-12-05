package pizzaShop.model.catalog_item;

import java.util.Comparator;

/**
 * class to compare 2 {@link Item} based on their name
 * @author Florentin
 *
 */
public class NameComparator implements Comparator<Item> {
	int ascending;
	
	/**
	 * 
	 * @param ascending to decide whether the items are compared ascending or descending
	 */
	public NameComparator(boolean ascending) {
		if(ascending)
		this.ascending = 1;
		else
		this.ascending = -1;
	}

	@Override
	public int compare(Item o1, Item o2) {
		return ascending * o1.getName().compareToIgnoreCase(o2.getName());
		
	}

}
