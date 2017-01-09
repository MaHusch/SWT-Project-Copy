package pizzaShop.model.catalog;


import org.salespointframework.catalog.Catalog;


public interface ItemCatalog extends Catalog<Item> {
	
	public Iterable<Item> findByType(ItemType type); //TODO: use in Controller
}
