package pizzaShop.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderIdentifier;
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

import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog_item.Ingredient;
import pizzaShop.model.catalog_item.Item;
import pizzaShop.model.catalog_item.ItemType;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.PizzaOrder;
import pizzaShop.model.store.PizzaOrderRepository;
import pizzaShop.model.store.StaffMemberRepository;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;
import pizzaShop.model.tan_management.TanStatus;

@Controller
public class StoreController {

	ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final StaffMemberRepository staffMemberRepository;
	private final Store store;
	private ErrorClass error;
	
	
	@Autowired 
	public StoreController(ItemCatalog itemCatalog, TanManagement tanManagement, CustomerRepository customerRepository, PizzaOrderRepository pOR, Store store, StaffMemberRepository staffMemberRepository) {

		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pOR;
		this.staffMemberRepository = staffMemberRepository;
		this.store = store;
		error = new ErrorClass(false);
	}

	@RequestMapping("/sBaker")
	public String sBaker() // direct to baker dashboard(after login)
	{
		return "sBaker";
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
			Pizza pizza = (Pizza) (store.findItemByIdentifier(itemID, null));

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
						else
							System.out.println("Zutat ist keine Ingredient");
					}
				}
				model.addAttribute("ingredients", ingredients);

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
	public String addIngredientsToPizza(@RequestParam("id_transmit") String ids[], @ModelAttribute Cart cart) {

		Pizza newPizza;

		if (ids == null || ids.length == 0) {
			error.setError(true);
			return "redirect:pizza_configurator";
		} else
			error.setError(false);
			newPizza = new Pizza("custom", Money.of(2, "EUR"));

		for (int i = 0; i < ids.length; i++) {

			Item foundItem = store.findItemByIdentifier(ids[i], ItemType.INGREDIENT);

			if (foundItem != null) {
				MonetaryAmount itemPrice = foundItem.getPrice();
				String itemName = foundItem.getName();

				Ingredient newIngredient = new Ingredient(itemName, itemPrice);
				newPizza.addIngredient(newIngredient);
			}

		}

		Pizza savedPizza = itemCatalog.save(newPizza);
		cart.addOrUpdateItem(savedPizza, Quantity.of(1));

		return "redirect:catalog";
	}

	@RequestMapping("/tan")
	public String tan(Model model) {

		model.addAttribute("tan", tanManagement.getAllTans());

		model.addAttribute("notConfirmedTans", tanManagement.getAllNotConfirmedTans());

		return "tan";
	}

	@RequestMapping("/staffmember_display")
	public String staffmember_display(Model model) {

		model.addAttribute("staffmember", store.getStaffMemberList());

		return "staffmember_display";
	}

	@RequestMapping("/customer_display")
	public String customer_display(Model model) {

		model.addAttribute("customer", customerRepository.findAll());

		return "customer_display";
	}

	
	@RequestMapping("/editEmployee") 
	public String directToEditItem(Model model,@RequestParam("sid") long id,RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("sid", id).addFlashAttribute("message", "StaffMember");
		model.addAttribute("error",error);
		return "redirect:register_staffmember";

	}
	

}