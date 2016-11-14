package kickstart.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import model.ItemCatalog;




@Controller
public class CatalogController {
	
	private final ItemCatalog itemCatalog;
	
	@Autowired
	public CatalogController(ItemCatalog itemCatalog)
	{
		this.itemCatalog = itemCatalog;
	}
	
	@RequestMapping("/catalog")
	public String showCatalog(Model model)
	{
		model.addAttribute("items",itemCatalog.findAll());
		
		return "catalog";
	}
}
