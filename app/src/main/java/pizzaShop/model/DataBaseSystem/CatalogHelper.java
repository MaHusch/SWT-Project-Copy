package pizzaShop.model.DataBaseSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.controller.NameComparator;
import pizzaShop.controller.PriceComparator;
import pizzaShop.model.OrderSystem.Ingredient;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.Pizza;

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
		List<String> ingredients = new ArrayList<String>();

		if (editedItem.getType().equals(newType)) {
			if (editedItem.getType().equals(ItemType.PIZZA))
				ingredients.addAll(((Pizza) editedItem).getIngredients());

			Ingredient oldItem = new Ingredient(editedItem.getName(), editedItem.getPrice());

			editedItem.setName(name);
			editedItem.setPrice(Money.of(price, EURO));

			// add ingredients to new Pizza
			if (!ingredients.isEmpty()) {

				for (String ing_name : ingredients) {
					Item i = null;
					Iterator<Item> it = itemCatalog.findByName(ing_name).iterator();
					if (it.hasNext())
						i = it.next();
					if (i != null && i.getType().equals(ItemType.INGREDIENT))
						((Pizza) editedItem).addIngredient((Ingredient) i);

				}
			}

			// add new ingredient price to pizza containing ingredient
			if (newType.equals(ItemType.INGREDIENT)) {
				for (Item i : itemCatalog.findByType(ItemType.PIZZA)) {
					Pizza p = (Pizza) i;
					if (p.getIngredients().contains(oldItem.getName())) {
						itemCatalog.delete(p);
						p.removeIngredient(oldItem);
						p.addIngredient((Ingredient) editedItem);
						itemCatalog.save(p);
					}
				}

			}

			itemCatalog.save(editedItem);

		}

		else {
			this.removeItem(editedItem);
			this.createNewItem(name, type, price);
		}

	}

	/**
	 * removes {@link Item} from catalog (if {@link Ingredient} --> remove from
	 * pizza too)
	 * 
	 * @param i
	 *            {@link Item} to be deleted
	 */
	public void removeItem(Item i) {
		itemCatalog.delete(i.getId());
		if (i.getType().equals(ItemType.INGREDIENT)) {
			Pizza p1;
			for (Item x : itemCatalog.findByType(ItemType.PIZZA)) {
				p1 = (Pizza) x;
				if (p1.getIngredients().contains(i.getName())) {
					p1.removeIngredient((Ingredient) i);
					itemCatalog.save(p1);
				}
			}

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
	
	/**
	 * Method for filtering the itemCatalog
	 * @param selection what kind of items
	 * @param filter kind of sorting (by price or name + asc/desc)
	 * @return
	 */
	public ArrayList<Item> filterCatalog(String selection, String filter)
	{
		ArrayList<Item> filteredItems = new ArrayList<Item>();
		switch (selection) {
		case "Getränke":
			for (Item i : itemCatalog.findByType(ItemType.DRINK))
				filteredItems.add(i);
			for (Item i : itemCatalog.findByType(ItemType.FREEDRINK))
				filteredItems.add(i);

			break;
		case "Essen":
			for (Item i : itemCatalog.findByType(ItemType.PIZZA))
				filteredItems.add(i);
			for (Item i : itemCatalog.findByType(ItemType.SALAD))
				filteredItems.add(i);
			for (Item i : itemCatalog.findByType(ItemType.INGREDIENT))
				filteredItems.add(i);

			break;
		default: // alles ist default
			for (Item i : itemCatalog.findAll())
				filteredItems.add(i);	
		}
		
		switch (filter) {
		default: // "hoechster Preis zuerst"
			Collections.sort(filteredItems, new PriceComparator(false));

			break;
		case "niedrigster Preis zuerst":
			Collections.sort(filteredItems, new PriceComparator(true));

			break;
		case "von A bis Z":

			Collections.sort(filteredItems, new NameComparator(true));
			break;
		case "von Z bis A":

			Collections.sort(filteredItems, new NameComparator(false));
		}
		
		return filteredItems;


	}

}
