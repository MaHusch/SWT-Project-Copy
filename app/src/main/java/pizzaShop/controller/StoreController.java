package pizzaShop.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderStatus;
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

import pizzaShop.model.actor.Address;
import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.Person;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog.Cutlery;
import pizzaShop.model.catalog.Ingredient;
import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemCatalog;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.catalog.Pizza;
import pizzaShop.model.catalog.CatalogHelper;
import pizzaShop.model.store.AddressRepository;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.PizzaOrder;
import pizzaShop.model.store.PizzaOrderRepository;
import pizzaShop.model.store.PizzaOrderStatus;
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
	private final CatalogHelper catalogHelper;
	private final AddressRepository addressRepository;

	private final Store store;
	private ErrorClass error;

	@Autowired 
	public StoreController(CatalogHelper catalogHelper,ItemCatalog itemCatalog, TanManagement tanManagement, 
			CustomerRepository customerRepository, PizzaOrderRepository pOR, Store store, 
			StaffMemberRepository staffMemberRepository, AddressRepository addressRepo) {


		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pOR;
		this.staffMemberRepository = staffMemberRepository;
		this.addressRepository = addressRepo;
		this.store = store;
		this.catalogHelper = catalogHelper;
		error = new ErrorClass(false);
	}

	@RequestMapping("/sBaker")
	public String sBaker() // direct to baker dashboard(after login)
	{
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
						else
							System.out.println("Zutat ist keine Ingredient");
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
		System.out.println("Custom pizza name " + pizzaName);
		Pizza newPizza;

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

		store.checkCutleries();
		model.addAttribute("customer", customerRepository.findAll());

		return "customer_display";
	}

	@RequestMapping("/editEmployee")
	public String directToEditStaffMember(Model model, @RequestParam("StaffMemberName") String name,
			RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("name", name).addFlashAttribute("message", "StaffMember");
		model.addAttribute("error", error);
		return "redirect:register_staffmember";

	}

	@RequestMapping("/editCustomer")
	public String editCustomer(Model model, @RequestParam("cid") long id, RedirectAttributes redirectAttrs) {
		redirectAttrs.addAttribute("cid", id).addFlashAttribute("message", "Customer");
		model.addAttribute("error", error);
		return "redirect:register_customer";

	}
	
	@RequestMapping("/deleteCustomer") 
	public String deleteCustomer(Model model,@RequestParam("cid") long id) {
		model.addAttribute("error",error);
		
		store.deleteCustomer(model, id);
		
		return "redirect:customer_display";

	}

	@RequestMapping(value = "/updateCustomer")
	public String updateStaffMember(Model model, @RequestParam("surname") String surname, @RequestParam("forename") String forename,
			@RequestParam("telnumber") String telephonenumber, @RequestParam("local") String local,
			@RequestParam("postcode") String postcode, @RequestParam("street") String street,
			@RequestParam("housenumber") String housenumber, @RequestParam("cid") long id) {

		Customer oldCustomer = customerRepository.findOne(id);
		
		if (surname == "" || forename == "" || telephonenumber == "" || local == "" || street == "" || housenumber == ""
				|| postcode == "") {
			error.setError(true);
			error.setMessage("Eingabefelder überprüfen!");
			model.addAttribute("error", error);
			model.addAttribute("existingCustomer", oldCustomer);
			model.addAttribute(housenumber);
			model.addAttribute("street", street);
			model.addAttribute(postcode);
			model.addAttribute(local);
			model.addAttribute(telephonenumber);
			model.addAttribute(forename);
			model.addAttribute(surname);
			
			return "register_customer";
		}
		
		Cutlery oldCutlery = oldCustomer.getCutlery();

		String oldTelephoneNumber = oldCustomer.getPerson().getTelephoneNumber();

		if (!oldTelephoneNumber.equals(telephonenumber)) {
			tanManagement.updateTelephoneNumber(oldTelephoneNumber, telephonenumber);
		}
		
		Address newAddress = new Address(local, postcode, street, housenumber);
		
		boolean addressAlreadyExists = false;
		
		for(Address address : this.addressRepository.findAll())
		{
			if(address.equals(newAddress)){
				newAddress = address;
				addressAlreadyExists = true;
				break;
			}
		}
		
		if(!addressAlreadyExists)
		{
			newAddress = this.addressRepository.save(newAddress);
		}
	
		Person updatedPerson = new Person(surname,forename, telephonenumber, newAddress);
			
		Customer updatedCustomer = new Customer(updatedPerson);
		
		if(oldCutlery != null)
		{
			updatedCustomer.setCutlery(oldCutlery);
		}

		Iterable<PizzaOrder> allPizzaOrders = pizzaOrderRepository.findAll();

		for (PizzaOrder pizzaOrder : allPizzaOrders) {
			Customer customer = pizzaOrder.getCustomer();
			if (customer != null) {
				if (customer.getId() == id) {
					pizzaOrder.setCustomer(updatedCustomer);
				}
			}
		}

		customerRepository.save(updatedCustomer);
		customerRepository.delete(id);

		return "redirect:customer_display";
	}

	@RequestMapping("returnCutlery")
	public String returnCutlery(@RequestParam("lost") String lostStr, @RequestParam("cid") long id) {
		String cutleryStatus = "lost";
		if (lostStr.equals("0"))
			cutleryStatus = "returned";

		try {
			store.returnCutlery(cutleryStatus, this.customerRepository.findOne(id));
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
			;
		}

		return "redirect:customer_display";
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

}