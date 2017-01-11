package pizzaShop.model.DataBaseSystem;


import org.salespointframework.catalog.Catalog;

import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;


public interface ItemCatalog extends Catalog<Item> {
	
	public Iterable<Item> findByType(ItemType type); //TODO: use in Controller
}
