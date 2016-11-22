package kickstart.controller;


import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kickstart.model.store.ItemCatalog;




@Controller
public class CatalogController {
	
	private final ItemCatalog itemCatalog;
	
	@Autowired
	public CatalogController(ItemCatalog itemCatalog)
	{
		this.itemCatalog = itemCatalog;
	}
	
	@ModelAttribute("cart")			//nötig um auf catalog.html anzuzeigen?
	public Cart initializeCart() {
		return new Cart();
	}
	
	@RequestMapping("/catalog")
	public String showCatalog(Model model)
	{
		model.addAttribute("items",itemCatalog.findAll());
		return "catalog";
	}
	
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("pid") ProductIdentifier id) {
		itemCatalog.delete(id);
		// was wenn noch im cart?
		return "redirect:catalog";

	}
	
	@RequestMapping("/editItem")
	public String editItem(@RequestParam("pid") ProductIdentifier id) {
		
		return "redirect:catalog";

	}

}
