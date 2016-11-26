package pizzaShop.controller;


import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;
import pizzaShop.model.tan_management.TanStatus;


@Controller
public class StoreController {
	
	ItemCatalog itemCatalog;
	private final TanManagement tanManagement;

	
	@Autowired 
	public StoreController(ItemCatalog itemCatalog, TanManagement tanManagement) {
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
	}	
	
	
	@RequestMapping("/sBaker")
	public String sBaker()   //direct to baker dashboard(after login)
	{
		return "sBaker";
	}
	
	@RequestMapping("/sAdmin")
	public String sAdmin()
	{
		return "sAdmin";
	}
	
	@RequestMapping("/sDeliverer")
	public String sDeliverer()
	{
		return "sDeliverer";
	}
	
	@RequestMapping("/sSeller")
	public String sSeller()
	{
		return "sSeller";
	}
	
	@RequestMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	
	@RequestMapping({"pizza_configurator"})
	public String configurePizza(Model model,@RequestParam(value = "pid", required = false) String itemID){
		model.addAttribute("items",itemCatalog.findByType(ItemType.INGREDIENT));
		
		if(itemID != null){
			Pizza pizza = (Pizza)(Store.getInstance().findItemByIdentifier(itemID,null));
			
			if (pizza == null){return "redirect:pizza_configurator";}
			else {model.addAttribute("ingredients",pizza.getIngredients());}
			
		}
	
		return "pizza_configurator";
	}
	
	@RequestMapping(value = "/configurePizza", method = RequestMethod.POST)
	public String redirectPizzaAttrs(RedirectAttributes redirectAttrs, @RequestParam("pid") String id){
		redirectAttrs.addAttribute("pid", id).addFlashAttribute("message", "Pizza verfeinern");
		return "redirect:pizza_configurator";
	}
	
	@RequestMapping(value = "/finishPizza", method = RequestMethod.POST)
	public String addIngredientsToPizza(@RequestParam("id_transmit") String ids[], @ModelAttribute Cart cart){
		
		Pizza newPizza;
		
		if (ids == null || ids.length == 0)
			return "redirect:pizza_configurator";
		else
			newPizza = new Pizza("custom",Money.of(7, "EUR"));
		
		
		for(int i = 0; i < ids.length; i++ ){
			
			Item foundItem = Store.getInstance().findItemByIdentifier(ids[i],ItemType.INGREDIENT);
			
			if (foundItem != null){
				MonetaryAmount itemPrice = foundItem.getPrice();
				String itemName = foundItem.getName();
			
				Ingredient newIngredient = new Ingredient(itemName,itemPrice);
				newPizza.addIngredient(newIngredient);
				newPizza.setPrice(newPizza.getPrice().add(itemPrice));
			}
			
		}
		
		cart.addOrUpdateItem(newPizza, Quantity.of(1));
		
		return "redirect:cart";
	}
	
	@RequestMapping("/tan")
	public String tan(Model model) {
		
		model.addAttribute("tan", tanManagement.getAllTans());
		
		model.addAttribute("notConfirmedTans", tanManagement.getAllNotConfirmedTans());
		
		return "tan";
	}
	
	@RequestMapping("/staffmember_display")
	public String staffmember_display(Model model) {
		
		model.addAttribute("staffmember", Store.staffMemberList);
		
		return "staffmember_display";
	}
	
	
}