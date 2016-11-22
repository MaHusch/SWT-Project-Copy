package kickstart.controller;

import java.util.Optional;

import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.order.OrderStatus;
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

import kickstart.model.actor.Customer;
import kickstart.model.store.CustomerRepository;
import kickstart.model.store.ItemCatalog;
import kickstart.model.store.PizzaOrder;
import kickstart.model.tan_management.Tan;
import kickstart.model.tan_management.TanManagement;
import kickstart.model.tan_management.TanStatus;

@Controller
@SessionAttributes({ "cart", "customer" })
public class CartController {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;

	@Autowired
	public CartController(OrderManager<Order> orderManager, ItemCatalog itemCatalog, TanManagement tanManagement, CustomerRepository customerRepository) {
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
	}

	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
	}
	
	@ModelAttribute("customer")
	public Optional<Customer> initializeCustomer() {
		return Optional.empty();
	}

	@RequestMapping("/orders")
	public String pizzaCart(Model model, @ModelAttribute Cart cart) {
		model.addAttribute("items", itemCatalog.findAll());
		model.addAttribute("total", cart.getPrice());
		model.addAttribute("orders", orderManager.findBy(OrderStatus.OPEN));
		return "orders";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addItem(@RequestParam("pid") ProductIdentifier id, @RequestParam("number") int number, @ModelAttribute Cart cart) {
		cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(number));
		return "redirect:catalog";

	}

	@RequestMapping(value = "/checkTan", method = RequestMethod.POST)
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue, @ModelAttribute Optional<Customer> customer) {
		Tan tan = tanManagement.getTan(telephoneNumber);
		if (tan.getTanNumber().equals(tanValue)) {
			for (Customer c : customerRepository.findAll()) {
				/*if (telephoneNumber == c.getTelephoneNumber()) {
					//customer = Optional.of(c);
					System.out.println("valid");
				}*/
			}
		}
		else{System.out.println("fail");}

		return "redirect:orders";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount, @ModelAttribute Optional<Customer> customer) {
		if (!userAccount.isPresent()) {
			return "redirect:login";
		}
		else if(!customer.isPresent()){
			return "redirect:orders";
		}
		PizzaOrder order = new PizzaOrder(userAccount.get(), Cash.CASH, tanManagement.getTan(customer.get().getTelephoneNumber()));
		cart.addItemsTo(order.getOrder());
		orderManager.save(order.getOrder());
		cart.clear();
		return "redirect:orders";

	}
}
