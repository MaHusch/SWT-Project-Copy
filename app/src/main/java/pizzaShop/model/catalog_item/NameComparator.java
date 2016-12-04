package pizzaShop.model.catalog_item;

import java.util.Comparator;

public class NameComparator implements Comparator<Item> {

	public NameComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Item o1, Item o2) {
		return o1.getName().compareToIgnoreCase(o2.getName());
	}

}
