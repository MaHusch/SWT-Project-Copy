package pizzaShop.controller;


import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.Store;


@Controller
public class StoreController {
	
	ItemCatalog itemCatalog;

	
	@Autowired 
	public StoreController(ItemCatalog itemCatalog) {
		this.itemCatalog = itemCatalog;
	}	
	
	
	@RequestMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	
	@RequestMapping({"pizza_configurator"})
	public String pizza_config(Model model){
		model.addAttribute("items",itemCatalog.findByType(ItemType.INGREDIENT));
		return "pizza_configurator";
	}
	
	@RequestMapping(value = "/addIngredientToPizza", method = RequestMethod.POST)
	public String addIngredientToPizza(@RequestParam("id_transmit") String ids[], @ModelAttribute Cart cart){
		
		Pizza newPizza;
		
		if (ids == null || ids.length == 0)
			return "redirect:pizza_configurator";
		else
			newPizza = new Pizza("custom",Money.of(7, "EUR"));
		
		
		for(int i = 0; i < ids.length; i++ ){
			
			Iterable<Item> items = this.itemCatalog.findAll();

			for(Item item : items){
				if(item.getId().getIdentifier().equals(ids[i])){
		
					MonetaryAmount itemPrice = item.getPrice();
					String itemName = item.getName();
					
					Ingredient newIngredient = new Ingredient(itemName,itemPrice);
					
					newPizza.addIngredient(newIngredient);
					newPizza.setPrice(newPizza.getPrice().add(itemPrice));
				}
			}
		}
		
		cart.addOrUpdateItem(newPizza, Quantity.of(1));
		
		return "redirect:index";
	}
	
	

}