package kickstart.controller;

import java.util.Optional;

import org.javamoney.moneta.Money;
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

import kickstart.model.catalog_item.Item;
import kickstart.model.catalog_item.ItemType;
import kickstart.model.store.ItemCatalog;
import kickstart.model.store.PizzaOrder;

@Controller
@SessionAttributes("cart")
public class CartController {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;

	@Autowired
	public CartController(OrderManager<Order> orderManger, ItemCatalog itemCatalog) {
		this.orderManager = orderManger;
		this.itemCatalog = itemCatalog;
	}

	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
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
		return "redirect:orders";

	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart, @LoggedIn Optional<UserAccount> userAccount) {
		if (!userAccount.isPresent()) {
			return "redirect:login";
		}
		Order order = new Order(userAccount.get(), Cash.CASH);
		cart.addItemsTo(order);
		orderManager.save(order);
		cart.clear();
		return "redirect:orders";

	}
}
