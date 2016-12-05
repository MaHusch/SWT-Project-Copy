package pizzaShop.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
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

import junit.framework.Assert;
import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.PizzaOrder;
import pizzaShop.model.store.PizzaOrderRepository;
import pizzaShop.model.store.StaffMemberRepository;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;

@Controller
@SessionAttributes("cart")
public class CartController {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final StaffMemberRepository staffMemberRepository;
	private Optional<Customer> customer = Optional.empty();

	@Autowired
	public CartController(OrderManager<Order> orderManager, ItemCatalog itemCatalog, TanManagement tanManagement,
			CustomerRepository customerRepository, PizzaOrderRepository pizzaOrderRepository,
			StaffMemberRepository staffMemberRepository) {
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.staffMemberRepository = staffMemberRepository;
	}

	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
	}

	@RequestMapping("/cart")
	public String pizzaCart(Model model) {
		model.addAttribute("items", itemCatalog.findAll());

		model.addAttribute("customer", customer);
		return "cart";
	}

	@RequestMapping("/orders")
	public String pizzaOrder(Model model) {
		/*
		 * for(Customer ca : customerRepository.findAll()){
		 * System.out.println("ID: "+ca.getId()+" tel: "+ca.getTelephoneNumber()
		 * +" Name: "+ca.getForename()); }
		 */
		// System.out.println("test"+customerRepository.findOne((long)
		// 1).getTelephoneNumber());

		model.addAttribute("orders", pizzaOrderRepository.findAll());

		ArrayList<StaffMember> deliverers = new ArrayList<StaffMember>();

		for (StaffMember staff : Store.staffMemberList) {
			if (staff.getRole().getName().contains("DELIVERER")) {
				Deliverer deliverer = (Deliverer) staff;
				if (deliverer.getAvailable()) {
					deliverers.add(staff);
				}
			}
		}

		model.addAttribute("deliverers", deliverers);

		return "orders";
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
	public String addItem(@RequestParam("ciid") String cartId, @ModelAttribute Cart cart) {

		cart.removeItem(cartId);
		return "redirect:cart";

	}

	@RequestMapping(value = "/checkTan", method = RequestMethod.POST)
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue) {

		Tan tan = tanManagement.getTan(telephoneNumber);
		if (tan.getTanNumber().equals(tanValue)) {
			for (Customer c : customerRepository.findAll()) {
				System.out.println("test" + c.getTelephoneNumber());
				if (telephoneNumber.equals(c.getTelephoneNumber())) {
					customer = Optional.of(c);
					System.out.println("valid: " + tanValue + " tel: " + customer.get().getTelephoneNumber());
				}
			}
		} else {
			System.out.println("fail");
		}

		return "redirect:cart";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart, @RequestParam("onSite") String onSiteStr,
			@LoggedIn Optional<UserAccount> userAccount) {
		if (!userAccount.isPresent()) {
			return "redirect:login";
		}
		assertTrue("Checkbox liefert anderen Wert als 0 oder 1! nämlich" + onSiteStr,
				onSiteStr.equals("0,1") | onSiteStr.equals("0"));
		if (customer.isPresent()) {
			boolean onSite = false;
			System.out.println(onSiteStr + " onSite");
			if (onSiteStr.equals("0,1")) {
				onSite = true;
			}

			PizzaOrder pizzaOrder = new PizzaOrder(userAccount.get(), Cash.CASH,
					tanManagement.generateNewTan(customer.get().getTelephoneNumber()), onSite, customer.get());// tanManagement.getTan(customer.getTelephoneNumber()));
			cart.addItemsTo(orderManager.save(pizzaOrder.getOrder()));
			Store.getInstance().analyzeOrder(pizzaOrderRepository.save(pizzaOrder));
			cart.clear();
			// customer = Optional.empty(); disabled for testing purposes
		}
		return "redirect:cart";

	}

	@RequestMapping(value = "/assignDeliverer", method = RequestMethod.POST)
	public String assignDeliverer(Model model, @RequestParam("delivererName") String name,
			@RequestParam("orderID") OrderIdentifier orderID) {// @RequestParam
																// OrderIdentifier
																// orderId)
		Deliverer deliverer = (Deliverer) Store.getInstance().getStaffMemberByForename(name);

		deliverer.addOrder(orderID);
		return "redirect:orders";
	}
}
