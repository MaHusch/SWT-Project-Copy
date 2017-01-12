package pizzaShop.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.DataBaseSystem.StaffMemberRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.CartHelper;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.PizzaOrder;

@Controller
@SessionAttributes("cart")
public class CartController {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final StaffMemberRepository staffMemberRepository;
	private final BusinessTime businesstime;
	private final CartHelper cartHelper;
	private Optional<Customer> customer = Optional.empty();
	private final Store store;
	private ErrorClass cartError;

	@Autowired
	public CartController(OrderManager<Order> orderManager, ItemCatalog itemCatalog, TanManagement tanManagement,
			CustomerRepository customerRepository, PizzaOrderRepository pizzaOrderRepository,
			StaffMemberRepository staffMemberRepository, Store store, BusinessTime businesstime,
			CartHelper cartHelper) {
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.staffMemberRepository = staffMemberRepository;
		this.store = store;
		this.cartHelper = cartHelper;
		this.businesstime = businesstime;
		cartError = new ErrorClass(false);
	}

	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
	}

	@RequestMapping("/cart")
	public String pizzaCart(Model model, @ModelAttribute Cart cart) {
		model.addAttribute("items", itemCatalog.findAll());

		ArrayList<Item> freeDrinks = new ArrayList<Item>();
		// TODO: use itemCatalog.findByType(ItemType.FREEDRINK)
		for (Item i : itemCatalog.findAll()) {
			if (i.getType().equals(ItemType.FREEDRINK))
				freeDrinks.add(i);
		}
		boolean freeDrink = false;
		Iterator<CartItem> ci = cart.iterator();
		while (ci.hasNext()) {
			if (((Item) ci.next().getProduct()).getType().equals(ItemType.FREEDRINK)) {
				freeDrink = true;
				break;
			}
		}
		customer = (customer.isPresent()) ? Optional.of(customerRepository.findOne(customer.get().getId())) : Optional.empty();
	

		model.addAttribute("freeDrinks", freeDrinks);
		model.addAttribute("error", cartError);
		model.addAttribute("customer", customer);
		model.addAttribute("freeDrink", freeDrink);
		return "cart";

	}

	@RequestMapping(value = "/addCartItem", method = RequestMethod.POST)
	public String addItem(@RequestParam("pid") ProductIdentifier id, @RequestParam("number") int number,
			@ModelAttribute Cart cart) {
		// Assert.notNull(id, "ID must not be null!");
		// System.out.println(id + itemCatalog.findOne(id).toString());

		if (itemCatalog.findOne(id).isPresent()) {
			cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(number));
		}
		return "redirect:catalog";

	}

	@RequestMapping(value = "/removeCartItem", method = RequestMethod.POST)
	public String removeItem(@RequestParam("ciid") String cartId, @ModelAttribute Cart cart) {
		cartHelper.removeItem(cartId, cart);
		return "redirect:cart";

	}

	@RequestMapping(value = "/changeQuantity", method = RequestMethod.POST)
	public String changeQuantity(	@RequestParam("ciid") String cartId, 
									@RequestParam("amount") int amount,
									@RequestParam("quantity") int quantity, 
									@RequestParam("pid") ProductIdentifier id,
									@ModelAttribute Cart cart) {
		cartError.setError(false);
		Item item = itemCatalog.findOne(id).orElse(null);
		if (quantity + amount == 0) {
			cartHelper.removeItem(cartId, cart);

		} else {

			try {
				cartHelper.changeQuantity(item, amount, cart);
			} catch (Exception e) {
				cartError.setError(true);
				cartError.setMessage(e.getMessage());
				cartHelper.removeItem(cartId, cart);

			}
		}
		return "redirect:cart";

	}

	@RequestMapping(value = "/addFreeDrink", method = RequestMethod.POST)
	public String addFreeDrink(	@RequestParam("iid") ProductIdentifier id, @ModelAttribute Cart cart) {
		cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(1));
		return "redirect:cart";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(	@ModelAttribute Cart cart, 
						@RequestParam("onSite") String onSiteStr,
						@RequestParam("cutlery") String cutleryStr, 
						@LoggedIn Optional<UserAccount> userAccount) {

		System.out.println("cutlery ist:" + cutleryStr);

		cartError.setError(false);

		boolean onSite = onSiteStr.equals("0,1") ? true : false;
		boolean cutlery = cutleryStr.equals("0") ? false : true;

		try {
			cartHelper.createPizzaOrder(cutlery, onSite, userAccount.orElse(null), cart, customer.orElse(null));
		} catch (Exception e) {
			cartError.setError(true);
			cartError.setMessage(e.getMessage());
		}

		return "redirect:cart";
	}

	@RequestMapping(value = "/checkTan", method = RequestMethod.POST)
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue) {

		Tan tan = tanManagement.getTan(telephoneNumber);

		cartError.setError(false);

		if (tan.getTanNumber().equals(tanValue)) {
			customer = cartHelper.checkTan(tan);
		} else {
			cartError.setError(true);
			cartError.setMessage("Fehler bei der TAN-Überprüfung! Erneut eingeben!");
		}

		return "redirect:cart";

	}

	@RequestMapping(value = "/logoutCustomer", method = RequestMethod.POST)
	public String logoutCustomer() {
		customer = Optional.empty();
		return "redirect:cart";
	}
}
