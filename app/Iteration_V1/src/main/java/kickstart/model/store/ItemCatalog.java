package kickstart.model.store;


import org.salespointframework.catalog.Catalog;

import kickstart.model.catalog_item.Item;
import kickstart.model.catalog_item.ItemType;


public interface ItemCatalog extends Catalog<Item> {
	
	public Iterable<Item> findByType(ItemType type); //TODO: use in Controller
}
