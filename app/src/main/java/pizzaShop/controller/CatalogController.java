package pizzaShop.controller;


import static org.salespointframework.core.Currencies.EURO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import groovyjarjarantlr.collections.List;
import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.NameComparator;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.catalog_item.PriceComparator;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.ItemCatalog;

/**
 * Controller to create the Catalog View
 * @author Florentin
 *
 */


@Controller
public class CatalogController {
	
	private final ItemCatalog itemCatalog;
	private ErrorClass error = new ErrorClass(false);
	private Iterable<Item> items;
	private ArrayList<Item> filteredItems;
	/**
	 * on creation spring searches the itemCatalog and allocates it to the local variabel
	 * @param itemCatalog the itemCatalog of the shop
	 */
	@Autowired
	public CatalogController(ItemCatalog itemCatalog)
	{
		this.itemCatalog = itemCatalog;
	}
	
	
	/**
	 * on /catalog the itemCatalog is shown
	 * @param model for the html view
	 * @return redirects to the catalog template
	 */
	@RequestMapping("/catalog")
	public String showCatalog(Model model)
	{
		items = itemCatalog.findAll();
		
		
		/*for(Item i : items)
		{
			ItemType type = i.getType();
			if(!(type.equals(ItemType.FREEDRINK) || type.equals(ItemType.INGREDIENT)))
				filteredItems.add(i); 
		}
		
		Collections.sort(filteredItems, new NameComparator(true)); */
		
		model.addAttribute("items", items);
		model.addAttribute("ItemType",ItemType.values());
		return "catalog";
	}
	
	/**
	 * on /remove a given item will be removed from the catalog
	 * @param id the productidentifier of the item which will be removed
	 * @return redirects to the catalog template
	 */
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("pid") ProductIdentifier id) {
		if(!itemCatalog.findOne(id).get().getType().equals(ItemType.INGREDIENT))
				itemCatalog.delete(id);
		// um Ingredients zu löschen muss noch durch Pizzen interiert werden.
		// was wenn noch im cart?
		return "redirect:catalog";

	}
	
	/**
	 * after a item is edited the new item will be saved in the itemCatalog
	 * @param id productidentifier of item which shall be altered
	 * @param name new name of the item (not empty)  
	 * @param price new price of the item (greater or equal 0)
	 * @param type new type of the item
	 * @return redirects to the catalog page
	 */
	@RequestMapping("/saveItem")
	public String saveItem(@RequestParam("pid") ProductIdentifier id, @RequestParam("itemname") String name, 
			 @RequestParam("itemprice") Number price, @RequestParam("itemtype") String type)
	{
		Item i = itemCatalog.findOne(id).orElse(null);
		ItemType ityp;
		// TODO: check Arguments
		
		
		switch(type)
		{
		default:
			ityp = ItemType.FREEDRINK;
			break;
		case "DRINK":
			ityp = ItemType.DRINK;
			break;
		case "INGREDIENT":
			ityp = ItemType.INGREDIENT;
			break;
		case "PIZZA":
			ityp = ItemType.PIZZA;
			break;
		case "SALAD":
			ityp = ItemType.SALAD;
			break;
		}
		
		if(!i.equals(null))
		{
			if(i.getType().equals(ityp))
			{
			i.setName(name);
			i.setPrice(Money.of(price, EURO));
			itemCatalog.save(i); // sonst wirds nicht auf den Catalog übertragen :O
			
			}
			else
			{
				itemCatalog.delete(i);
				this.createItem(name, price, type);
			}
		}
		System.out.println(ityp.name());
		System.out.println(i.getName());
		System.out.println(i.getPrice());
		return "redirect:catalog";
	}
	
	/**
	 * directs to the addItem template with an optional Item searched by the parameter id
	 * @param model model for the addItem template
	 * @param id the id of the item to be edited
	 * @return directs to the addItem template
	 */
	@RequestMapping("/editItem") 
	public String directToEditItem(Model model,@RequestParam("pid") ProductIdentifier id) {
		
		Optional<Item> i = itemCatalog.findOne(id);
		model.addAttribute("item",i.get());
		model.addAttribute("ItemTypes",ItemType.values());
		model.addAttribute("error",error);
		return "addItem";

	}
	
	/**
	 * adds an error variable to the model (to catch errors)
	 * @param model for generating the addItem template
	 * @return directs to the addItem template
	 */
	@RequestMapping("addItem")
	public String directToAddItem(Model model)
	{
		model.addAttribute("error",error);
		return "addItem";
	}
	
	/**
	 * checks if the inputs are valid and creates an new item and adds it the itemCatalog
	 * @param name name of the new item (not empty)
	 * @param price price of the new item (greater or equal 0)
	 * @param type ItemType of the new item
	 * @return redirects to the catalog template if successful otherwise to the addItem template with error description
	 */
	@RequestMapping("/createItem")
	public String createItem(@RequestParam("itemname") String name, 
							 @RequestParam("itemprice") Number price,
							 @RequestParam("itemtype") String type)
	{
		Item neu;
		if(name.isEmpty() || price.floatValue() < 0) 
			{
			//TODO: interact with frontend
			System.out.println("fehler");
			error.setError(true);
			return "redirect:addItem";
			}
	
		if(type.equals("PIZZA"))
		{
			neu = new Pizza(name,Money.of(price, EURO));
			
		}
		else if(type.equals("INGREDIENT"))
		{
			neu = new Ingredient(name,Money.of(price, EURO));
		}
		else
		{
			ItemType t = ItemType.SALAD;
			if(type.equals("DRINK")) t = ItemType.DRINK;
			if(type.equals("FREEDRINK")) t = ItemType.FREEDRINK;
			if(type.equals("SALAD")) t = ItemType.SALAD;
			
			neu = new Item(name,Money.of(price, EURO),t);
		}
		
		itemCatalog.save(neu);
		
		return "redirect:catalog";
	}
	
	@RequestMapping("/filterCatalog")
	public String filterCatalog(Model model, @RequestParam("selection") String selection,
								@RequestParam("filter") String filter)
	{
		filteredItems = new ArrayList<Item>();
		System.out.println(filter + ' ' + selection);
		
		switch(selection)
		{
		case "Getränke":
			for(Item i : itemCatalog.findByType(ItemType.DRINK)) filteredItems.add(i);
			break;
		case "Essen":
			for(Item i : itemCatalog.findByType(ItemType.PIZZA)) filteredItems.add(i);
			for(Item i : itemCatalog.findByType(ItemType.SALAD)) filteredItems.add(i);
			
		default: // alles ist default	
			for(Item i : itemCatalog.findAll()) filteredItems.add(i);
		}
		
		switch(filter)
		{
		case "hoechster Preis zuerst":
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
		
		model.addAttribute("items",filteredItems);
		model.addAttribute("ItemType",ItemType.values());
		return "catalog";
	}

}
