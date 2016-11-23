package pizzaShop.controller;

import java.util.Optional;

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

import pizzaShop.model.actor.Customer;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.PizzaOrder;
import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;

@Controller
@SessionAttributes("cart")
public class CartController {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private Customer customer;

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
	

	@RequestMapping("/orders")
	public String pizzaCart(Model model, @ModelAttribute Cart cart) {
		Customer c = new Customer("a", "b", "1");
		customerRepository.save(c);
		Customer temp;
		for(Customer ca : customerRepository.findAll()){
			temp = ca;
			System.out.println("test"+temp.getId());
		}
		System.out.println("test"+customerRepository.findOne((long) 1).getTelephoneNumber());
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
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue) {
		Tan tan = tanManagement.getTan(telephoneNumber);
		if (tan.getTanNumber().equals(tanValue)) {
			for(Customer c : customerRepository.findAll()) {
				System.out.println("test"+c.getTelephoneNumber());
				if (telephoneNumber == c.getTelephoneNumber()) {
					customer = c;
					System.out.println("valid"+customer.getTelephoneNumber());
				}
			}
		}
		else{System.out.println("fail");}

		return "redirect:orders";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {
		if (!userAccount.isPresent()) {
			return "redirect:login";
		}
		/*else if(!customer.isPresent()){
			return "redirect:orders";
		}*/
		PizzaOrder order = new PizzaOrder(userAccount.get(), Cash.CASH, tanManagement.getTan(customer.getTelephoneNumber()));
		cart.addItemsTo(order.getOrder());
		orderManager.save(order.getOrder());
		cart.clear();
		return "redirect:orders";

	}
}

