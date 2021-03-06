package pizzaShop.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.salespointframework.order.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzaShop.model.DataBaseSystem.CatalogHelper;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.ManagementSystem.Tan_Management.TanStatus;
import pizzaShop.model.OrderSystem.Ingredient;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.Pizza;

@Controller
public class StoreController {

	ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CatalogHelper catalogHelper;

	private final Store store;
	private ErrorClass error;

	@Autowired
	public StoreController(CatalogHelper catalogHelper, ItemCatalog itemCatalog, TanManagement tanManagement, PizzaOrderRepository pOR, Store store) {
		
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.store = store;
		this.catalogHelper = catalogHelper;
		error = new ErrorClass(false);
	}

	@RequestMapping("/sBaker")
	public String sBaker() {
		return "redirect:ovens";
	}

	@RequestMapping("/sAdmin")
	public String sAdmin() {
		return "sAdmin";
	}

	@RequestMapping("/sSeller")
	public String sSeller() {
		return "sSeller";
	}

	@RequestMapping({ "/", "/index" })
	public String index() {
		return "index";
	}

	@RequestMapping({ "pizza_configurator" })
	public String configurePizza(Model model, @RequestParam(value = "pid", required = false) String itemID) {
		model.addAttribute("items", itemCatalog.findByType(ItemType.INGREDIENT));
		model.addAttribute("error", error);
		if (itemID != null) {
			Pizza pizza = (Pizza) (catalogHelper.findItemByIdentifier(itemID, null));

			if (pizza == null) {
				return "redirect:pizza_configurator";
			} else {
				List<String> ingrNames = pizza.getIngredients();
				ArrayList<Item> ingredients = new ArrayList<Item>();

				for (String name : ingrNames) {
					Iterator<Item> i = itemCatalog.findByName(name).iterator();
					if (i.hasNext()) {
						Item x = i.next();
						if (x.getType().equals(ItemType.INGREDIENT))
							ingredients.add((Ingredient) x);
					}
				}
				model.addAttribute("ingredients", ingredients);
				model.addAttribute("pizzaName", pizza.getName());
				model.addAttribute("pid", itemID);
			}
		}
		return "pizza_configurator";
	}

	@RequestMapping(value = "/configurePizza", method = RequestMethod.POST)
	public String redirectPizzaAttrs(RedirectAttributes redirectAttrs, @RequestParam("pid") String id) {
		redirectAttrs.addAttribute("pid", id).addFlashAttribute("message", "Pizza verfeinern");
		return "redirect:pizza_configurator";
	}

	@RequestMapping(value = "/finishPizza", method = RequestMethod.POST)
	public String addIngredientsToPizza(Model model, @RequestParam("id_transmit") String ids[],
			@RequestParam("pizza_name") String pizzaName,
			@RequestParam(value = "admin_flag", required = false) String admin_flag,
			@RequestParam(value = "pid", required = false) String pizzaID, @ModelAttribute Cart cart) {
		if (ids == null || ids.length == 0) {
			error.setError(true);
			return "redirect:pizza_configurator";
		} else
			error.setError(false);

		store.configurePizza(model, ids, pizzaName, admin_flag, pizzaID, cart);

		return "redirect:catalog";
	}

	@RequestMapping("/tan")
	public String tan(Model model) {

		ArrayList<Map.Entry<Tan, String>> activeTans = new ArrayList<Map.Entry<Tan, String>>();
		ArrayList<Map.Entry<Tan, String>> usedTans = new ArrayList<Map.Entry<Tan, String>>();

		for (Entry<Tan, String> t : tanManagement.getAllTans()) {
			if (t.getKey().getStatus().equals(TanStatus.USED)) {
				usedTans.add(t);
			} else {
				activeTans.add(t);
			}
		}

		model.addAttribute("activeTans", activeTans);
		model.addAttribute("usedTans", usedTans);

		model.addAttribute("notConfirmedTans", tanManagement.getAllNotConfirmedTans());

		return "tan";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/newsletter")
	public String newsletter() {

		return "newsletter";

	}

	@RequestMapping("addEmail")
	public String addEmail(@RequestParam("email") String eMailAddress) {

		store.addEmailToMailingList(eMailAddress);

		return "newsletter";

	}

	@RequestMapping("removeEmail")
	public String removeEmail(@RequestParam("email") String eMailAddress) {

		store.removeEmailFromMailingList(eMailAddress);

		return "newsletter";

	}

	@RequestMapping("sendNewsletter")
	public String sendNewsletter(@RequestParam("newsletter_text") String newsletterText) {

		store.sendNewsletter(newsletterText);

		return "newsletter";

	}
	
	@RequestMapping("deleteUsedTans")
	public String sendNewsletter() {

		tanManagement.deleteUsedTans();

		return "redirect:tan";

	}

}