package pizzaShop.controller;

import java.util.ArrayList;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.OrderSystem.PizzaOrder;
import pizzaShop.model.OrderSystem.PizzaOrderStatus;

@Controller
@SessionAttributes("cart")
public class OrderController {

	private final OrderManager<Order> orderManager;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final Store store;
	private ErrorClass error;

	@Autowired
	public OrderController(OrderManager<Order> orderManager, PizzaOrderRepository pizzaOrderRepository, Store store) {
		this.orderManager = orderManager;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.store = store;
		error = new ErrorClass(false);
	}

	@RequestMapping("/orders")
	public String pizzaOrder(Model model) {

		ArrayList<StaffMember> deliverers = new ArrayList<StaffMember>();

		for (StaffMember staff : store.getStaffMemberList()) {
			if (staff.getRole().getName().contains("DELIVERER")) {
				Deliverer deliverer = (Deliverer) staff;
				if (deliverer.getAvailable()) {
					deliverers.add(staff);
				}
			}
		}

		ArrayList<PizzaOrder> uncompletedOrders = new ArrayList<PizzaOrder>();
		ArrayList<PizzaOrder> completedOrders = new ArrayList<PizzaOrder>();
		// TODO: pizzaOrderRepository can have a method ...
		// findbyPizzaOrderStatus
		for (PizzaOrder po : pizzaOrderRepository.findAll()) {
			if (po.getOrderStatus().equals(PizzaOrderStatus.COMPLETED)) {
				completedOrders.add(po);
			} else {
				uncompletedOrders.add(po);
			}
		}

		model.addAttribute("uncompletedOrders", uncompletedOrders);
		model.addAttribute("completedOrders", completedOrders);
		model.addAttribute("deliverers", deliverers);
		model.addAttribute("error", error);

		return "orders";
	}

	@RequestMapping(value = "/confirmCollection", method = RequestMethod.POST)
	public String cofirmLocalOrder(@RequestParam("orderID") OrderIdentifier id) {
		PizzaOrder p = pizzaOrderRepository.findOne(id);
		if (p.getOrderStatus().equals(PizzaOrderStatus.READY)) {
			error.setError(false);
			store.completeOrder(pizzaOrderRepository.findOne(id), "mitgenommen", null);
			// picked up order, no deliverer needed
		} else {
			error.setError(true);
			error.setMessage("Order ist nicht bereit!");
		}
		return "redirect:orders";
	}

	@RequestMapping(value = "/assignDeliverer", method = RequestMethod.POST)
	public String assignDeliverer(Model model, @RequestParam("delivererList") String username,
			@RequestParam("orderID") OrderIdentifier orderID) {
		if (username == null || username.equals("")) {
			error.setError(true);
			error.setMessage("Keinen Lieferanten ausgewählt!");
		} else {
			Deliverer deliverer = (Deliverer) store.getStaffMemberByName(username);

			deliverer.addOrder(orderID);
			PizzaOrder p = pizzaOrderRepository.findOne(orderID);
			p.assignDeliverer(deliverer);
			pizzaOrderRepository.save(p);
		}
		return "redirect:orders";
	}

	/**
	 * Method to unassign the {@link Deliverer} from a {@link PizzaOrder}
	 * @param model model 
	 * @param orderID orderID of the pizzaOrder
	 * @return redirects to orders
	 */
	@RequestMapping(value = "changeDeliverer", method = RequestMethod.POST)
	public String assignDeliverer(Model model,@RequestParam("orderID") OrderIdentifier orderID) {
		
		PizzaOrder p = pizzaOrderRepository.findOne(orderID);
		Deliverer deliverer = (Deliverer) store.getStaffMemberByName(p.getDelivererName());
		deliverer.removeOrder(orderID);	
		p.readyOrder();
		pizzaOrderRepository.save(p);
		return"redirect:orders";
}}
