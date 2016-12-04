package pizzaShop.model.catalog_item;

import java.util.Comparator;

public class PriceComparator implements Comparator<Item> {

	public PriceComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Item o1, Item o2) {
		
		if(o1.getPrice().isGreaterThan(o2.getPrice())) return 1;
		else if(o1.getPrice().equals(o2.getPrice())) return 0;
		// TODO Auto-generated method stub
		return -1;
	}

}
