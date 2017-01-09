package pizzaShop.model.catalog;

import static org.salespointframework.core.Currencies.EURO;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.Store;

@Component
public class CatalogHelper {
	private final ItemCatalog itemCatalog;

	@Autowired
	public CatalogHelper(ItemCatalog catalog) {
		this.itemCatalog = catalog;
	}

	/**
	 * ConverterClass String -> {@link ItemType}
	 * 
	 * @param type
	 *            {@link ItemType} given as a String
	 * @return type as an {@link ItemType} (by default FREEDRINK (could be
	 *         error)
	 */
	public static ItemType StringtoItemtype(String type) // use to remove
															// redundancy?!
	{
		switch (type) {
		default:
			return ItemType.FREEDRINK;
		case "DRINK":
			return ItemType.DRINK;
		case "INGREDIENT":
			return ItemType.INGREDIENT;
		case "PIZZA":
			return ItemType.PIZZA;
		case "SALAD":
			return ItemType.SALAD;
		}
	}

	/**
	 * Creates a new {@link Item}
	 * 
	 * @param name
	 *            name of the new {@link Item}
	 * @param type
	 *            {@link ItemType} as a String
	 * @param price
	 *            price of the new {@link Item}
	 * @throws Exception
	 *             when inputs are invalid
	 */
	public void createNewItem(String name, String type, Number price) throws Exception {
		Item newItem;

		ItemType itype = CatalogHelper.StringtoItemtype(type);

		if (name.isEmpty()) {
			throw new IllegalArgumentException("Name darf nicht leer sein");
		}

		if (price.floatValue() < 0) {
			throw new IllegalArgumentException("Preis darf nicht negativ sein");
		}

		if (itype.equals(ItemType.PIZZA)) {
			newItem = new Pizza(name, Money.of(price, EURO));
		} else if (type.equals(ItemType.INGREDIENT)) {
			newItem = new Ingredient(name, Money.of(price, EURO));
		} else {
			newItem = new Item(name, Money.of(price, EURO), itype);
		}

		itemCatalog.save(newItem);

	}

	/**
	 * function to save an edited {@link Item} in the itemCatalog
	 * 
	 * @param editedItem
	 * @param name
	 * @param type
	 * @param price
	 * @throws Exception
	 *             if inputs are invalid
	 */
	public void saveEditedItem(Item editedItem, String name, String type, Number price) throws Exception {
		if (editedItem.equals(null))
			throw new NullPointerException("zu editierendes Item existiert nicht");
		if (name.isEmpty())
			throw new IllegalArgumentException("Name darf nicht leer sein");
		if (price.floatValue() < 0)
			throw new IllegalArgumentException("Preis darf nicht negativ sein");

		ItemType newType = CatalogHelper.StringtoItemtype(type);

		if (editedItem.getType().equals(newType)) {
			itemCatalog.delete(editedItem); // altes Element rauslöschen
			editedItem.setName(name);
			System.out.println(editedItem.getName());
			editedItem.setPrice(Money.of(price, EURO));
			itemCatalog.save(editedItem);

		} else {
			System.out.println("anderer Itemtyp --> neues Item");
			itemCatalog.delete(editedItem);
			this.createNewItem(name, type, price);
		}

	}

	public Item findItemByIdentifier(String identifier, ItemType filter) {

		Iterable<Item> items;

		if (filter == null) {
			items = this.itemCatalog.findAll();
		} else {
			items = this.itemCatalog.findByType(filter);
		}

		for (Item item : items) {
			if (item.getId().getIdentifier().equals(identifier)) {
				return item;
			}
		}

		return null;
	}
	
	public void cleanUpItemCatalog() { // unused?
		Iterable<Item> items1 = itemCatalog.findAll();
		Iterable<Item> items2 = itemCatalog.findAll();

		for (Item item1 : items1) {
			for (Item item2 : items2) {
				if (item1.getName().equals(item2.getName()))
					itemCatalog.delete(item2);
			}

		}

	}

}
