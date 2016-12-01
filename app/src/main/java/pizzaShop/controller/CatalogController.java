package pizzaShop.controller;


import static org.salespointframework.core.Currencies.EURO;

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

import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.ItemCatalog;




@Controller
public class CatalogController {
	
	private final ItemCatalog itemCatalog;
	private ErrorClass error = new ErrorClass(false);
	
	@Autowired
	public CatalogController(ItemCatalog itemCatalog)
	{
		this.itemCatalog = itemCatalog;
	}
	
		
	@RequestMapping("/catalog")
	public String showCatalog(Model model)
	{
		model.addAttribute("items",itemCatalog.findAll());
		model.addAttribute("ItemType",ItemType.values());
		return "catalog";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("pid") ProductIdentifier id) {
		if(!itemCatalog.findOne(id).get().getType().equals(ItemType.INGREDIENT))
				itemCatalog.delete(id);
		// um Ingredients zu löschen muss noch durch Pizzen interiert werden.
		// was wenn noch im cart?
		return "redirect:catalog";

	}
	
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
	
	@RequestMapping("/editItem") 
	public String editItem(Model model,@RequestParam("pid") ProductIdentifier id) {
		
		Optional<Item> i = itemCatalog.findOne(id);
		model.addAttribute("item",i.get());
		model.addAttribute("ItemTypes",ItemType.values());
		model.addAttribute("error",error);
		return "addItem";

	}
	
	@RequestMapping("addItem")
	public String addItem(Model model)
	{
		model.addAttribute("error",error);
		return "addItem";
	}
	
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

}
