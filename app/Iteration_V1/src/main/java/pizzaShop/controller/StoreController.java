package pizzaShop.controller;


import java.util.Optional;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.catalog_item.ItemType;
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
	public String addIngredientToPizza(@RequestParam("id_transmit") String id, Model model){
		System.out.println("ID: " + id);
		return "redirect:pizza_configurator";
	}
	
	

}