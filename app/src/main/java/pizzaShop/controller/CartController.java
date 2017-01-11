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
	private Optional<Customer> customer = Optional.empty();
	private final Store store;
	private ErrorClass cartError;
	private boolean freeDrink = false;

	@Autowired
	public CartController(OrderManager<Order> orderManager, ItemCatalog itemCatalog, TanManagement tanManagement,
			CustomerRepository customerRepository, PizzaOrderRepository pizzaOrderRepository,
			StaffMemberRepository staffMemberRepository, Store store, BusinessTime businesstime) {
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.staffMemberRepository = staffMemberRepository;
		this.store = store;
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
		if (cart.getItem(cartId).get().getPrice().isZero())
			freeDrink = false;
		cart.removeItem(cartId);

		return "redirect:cart";

	}

	@RequestMapping(value = "/changeQuantity", method = RequestMethod.POST)
	public String changeQuantity(	@RequestParam("ciid") String cartId, 
									@RequestParam("amount") int amount,
									@RequestParam("quantity") int quantity, 
									@RequestParam("pid") ProductIdentifier id,
									@ModelAttribute Cart cart) {
		cartError.setError(false);
		Optional<Item> item = itemCatalog.findOne(id);
		if (!item.isPresent()) {
			cartError.setError(true);
			cartError.setMessage("Produkt existiert nicht mehr!");
			return removeItem(cartId, cart);
		}
		if (quantity + amount == 0) {

			return removeItem(cartId, cart);
		} else {
			cart.addOrUpdateItem(item.get(), Quantity.of(amount));
		}
		if (cart.getPrice().getNumber().intValue() < 30) {
			Iterator<CartItem> ci = cart.iterator();
			while (ci.hasNext()) {
				CartItem i = ci.next();
				if (((Item) i.getProduct()).getType().equals(ItemType.FREEDRINK)) {
					cart.removeItem(i.getId());
					break;
				}
			}
			freeDrink = false;
		}
		return "redirect:cart";
	}

	@RequestMapping(value = "/addFreeDrink", method = RequestMethod.POST)
	public String addFreeDrink(@RequestParam("iid") ProductIdentifier id, @ModelAttribute Cart cart) {
		cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(1));
		freeDrink = true;
		return "redirect:cart";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(	@ModelAttribute Cart cart, 
						@RequestParam("onSite") String onSiteStr,
						@RequestParam("cutlery") String cutleryStr, 
						@LoggedIn Optional<UserAccount> userAccount) {
		if (!userAccount.isPresent()) {
			return "redirect:login";
		}
		assertTrue("Checkbox liefert anderen Wert als 0 oder 1! nämlich" + onSiteStr,
				onSiteStr.equals("0,1") | onSiteStr.equals("0"));
		System.out.println("cutlery ist:" + cutleryStr);
		if (customer.isPresent()) {
			cartError.setError(false);
			if (cart.isEmpty()) {
				cartError.setError(true);
				cartError.setMessage("Warenkorb ist leer!");
				return "redirect:cart";
			}

			boolean onSite = false;
			boolean cutlery = true;
			System.out.println(onSiteStr + " onSite");
			if (onSiteStr.equals("0,1")) {
				onSite = true;
			}
			if (cutleryStr.equals("0"))
				cutlery = false;

			// TODO: check if customer already has a cutlery --> throw error
			if (cutlery) {
				// if false --> return error
				store.lentCutlery(customer.get(), businesstime.getTime());
			}

			PizzaOrder pizzaOrder = new PizzaOrder(userAccount.get(), Cash.CASH,
					tanManagement.generateNewTan(customer.get().getPerson().getTelephoneNumber()), onSite, customer.get());
			cart.addItemsTo(orderManager.save(pizzaOrder.getOrder()));
			store.analyzeOrder(pizzaOrder);
			cart.clear();

			// Bill bill = new Bill(customer.get(), pizzaOrder,
			// businesstime.getTime());
			// customer = Optional.empty(); disabled for testing purposes
		}
		return "redirect:cart";
	}

	@RequestMapping(value = "/checkTan", method = RequestMethod.POST)
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue) {

		Tan tan = tanManagement.getTan(telephoneNumber);
		if (tan.getTanNumber().equals(tanValue)) {
			cartError.setError(false);
			for (Customer c : customerRepository.findAll()) {
				System.out.println("test" + c.getPerson().getTelephoneNumber());
				if (telephoneNumber.equals(c.getPerson().getTelephoneNumber())) {
					customer = Optional.of(c);
					System.out
							.println("valid: " + tanValue + " tel: " + customer.get().getPerson().getTelephoneNumber());
				}
			}
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
