package pizzaShop.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.DataBaseSystem.CatalogHelper;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;

/**
 * Controller to create views regarding {@link Item}s and {@link ItemCatalog}
 * 
 * @author Florentin
 *
 */

@Controller
public class CatalogController {

	private final ItemCatalog itemCatalog;
	private ErrorClass error = new ErrorClass(false);
	private ArrayList<Item> filteredItems;
	private final CatalogHelper catalogHelper;
	private String filter = "Von A bis Z";
	private String selection = "Alles";

	/**
	 * on creation spring searches the itemCatalog and allocates it to the local
	 * variable
	 * 
	 * @param itemCatalog
	 *            the itemCatalog of the shop
	 */
	@Autowired
	public CatalogController(CatalogHelper catalogHelper, ItemCatalog itemCatalog, Store store) {
		this.itemCatalog = itemCatalog;
		this.catalogHelper = catalogHelper;
	}

	/**
	 * on /catalog the itemCatalog is shown
	 * 
	 * @param model
	 *            for the html view
	 */
	@RequestMapping("/catalog")
	public String showCatalog(Model model) {
		return this.filterCatalog(model, selection, filter);
	}

	/**
	 * on /remove a given {@link Item} will be removed from the catalog
	 * 
	 * @param id
	 *            the productidentifier of the {@link Item} which will be
	 *            removed
	 * @return redirects to the catalog template
	 */
	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("pid") ProductIdentifier id) {
		Item item = itemCatalog.findOne(id).orElse(null);
		if (item == null) {
			return "redirect:catalog";
		}

		this.catalogHelper.removeItem(item);

		return "redirect:catalog";
	}

	/**
	 * after a item is edited the new {@link Item} will be saved in the
	 * itemCatalog
	 * 
	 * @param id
	 *            productidentifier of {@link Item} which shall be altered
	 * @param name
	 *            new name of the {@link Item} (not empty)
	 * @param price
	 *            new price of the {@link Item} (greater or equal 0)
	 * @param type
	 *            new type of the {@link Item}
	 * @return redirects to the catalog page
	 */
	@RequestMapping("/saveItem")
	public String saveItem(Model model, @RequestParam("pid") ProductIdentifier id,
			@RequestParam("itemname") String name, @RequestParam("itemprice") Number price,
			@RequestParam("itemtype") String type) {
		Item i = itemCatalog.findOne(id).orElse(null);

		try {
			catalogHelper.saveEditedItem(i, name, type, price);
			error.setError(false);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
			model.addAttribute("error", error);
			model.addAttribute("ItemTypes", ItemType.values());
			model.addAttribute("item", i);
			return "addItem";
		}

		return "redirect:catalog";
	}

	/**
	 * directs to the addItem template with an optional {@link Item} searched by
	 * the parameter id
	 * 
	 * @param model
	 *            model for the addItem template
	 * @param id
	 *            the id of the {@link Item} to be edited
	 * @return directs to the addItem template
	 */
	@RequestMapping("/editItem")
	public String directToEditItem(Model model, @RequestParam("pid") ProductIdentifier id) {

		Optional<Item> i = itemCatalog.findOne(id);
		model.addAttribute("item", i.get());
		model.addAttribute("ItemTypes", ItemType.values());
		model.addAttribute("error", error);
		return "addItem";

	}

	/**
	 * adds an {@link ErrorClass} variable to the model (to catch errors)
	 * 
	 * @param model
	 *            for generating the addItem template
	 * @return directs to the addItem template
	 */
	@RequestMapping("addItem")
	public String directToAddItem(Model model) {
		model.addAttribute("error", error);
		return "addItem";
	}

	/**
	 * checks if the inputs are valid and creates an new {@link Item} and adds
	 * it the itemCatalog
	 * 
	 * @param name
	 *            name of the new {@link Item} (not empty)
	 * @param price
	 *            price of the new {@link Item} (greater or equal 0)
	 * @param type
	 *            {@link ItemType} of the new {@link Item}
	 * @return redirects to the catalog template if successful otherwise to the
	 *         addItem template with error description
	 */
	@RequestMapping("/createItem")
	public String createItem(@RequestParam("itemname") String name, @RequestParam("itemprice") Number price,
			@RequestParam("itemtype") String type) {
		try {
			catalogHelper.createNewItem(name, type, price);
			error.setError(false);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
			return "redirect:addItem";
		}

		return "redirect:catalog";
	}

	/**
	 * filters the Catalogs shown on the template
	 * 
	 * @param model
	 *            model given to the template
	 * @param selection
	 *            which kind of {@link Item}s shall be shown
	 * @param filter
	 *            how they shall be sorted (f.i. highest price first)
	 * @return
	 */
	@RequestMapping("/filterCatalog")
	public String filterCatalog(Model model, @RequestParam("selection") String selection,
			@RequestParam("filter") String filter) {

		this.selection = selection;
		this.filter = filter;
		
		filteredItems = this.catalogHelper.filterCatalog(selection, filter);

		model.addAttribute("lastfilter", filter);
		model.addAttribute("lastselection", selection);
		model.addAttribute("items", filteredItems);
		model.addAttribute("ItemType", ItemType.values());
		return "catalog";
	}

}
