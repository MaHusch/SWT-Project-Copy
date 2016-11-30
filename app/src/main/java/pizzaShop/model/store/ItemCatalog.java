package pizzaShop.model.store;


import org.salespointframework.catalog.Catalog;

import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;


public interface ItemCatalog extends Catalog<Item> {
	
	public Iterable<Item> findByType(ItemType type); //TODO: use in Controller
}
