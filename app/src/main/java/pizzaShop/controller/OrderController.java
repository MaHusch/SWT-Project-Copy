package pizzaShop.controller;

import java.util.ArrayList;

import org.salespointframework.order.OrderIdentifier;
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
import pizzaShop.model.OrderSystem.OrderHelper;
import pizzaShop.model.OrderSystem.PizzaOrder;
import pizzaShop.model.OrderSystem.PizzaOrderStatus;

@Controller
@SessionAttributes("cart")
public class OrderController {


	private final PizzaOrderRepository pizzaOrderRepository;
	private final Store store;
	private final OrderHelper orderHelper;
	private ErrorClass error;

	@Autowired
	public OrderController(PizzaOrderRepository pizzaOrderRepository, Store store,
			OrderHelper orderHelper) {
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.store = store;
		this.orderHelper = orderHelper;
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
		error.setError(false);
		try {
			orderHelper.pickUpOrder(p);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
		}
		return "redirect:orders";
	}

	@RequestMapping(value = "/assignDeliverer", method = RequestMethod.POST)
	public String assignDeliverer(Model model, @RequestParam("delivererList") String username,
			@RequestParam("orderID") OrderIdentifier orderID) {

		try {
			orderHelper.assignDeliverer(username, orderID);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
		}
		return "redirect:orders";
	}

	/**
	 * Method to unassign the {@link Deliverer} from a {@link PizzaOrder}
	 * 
	 * @param model
	 *            model
	 * @param orderID
	 *            orderID of the pizzaOrder
	 * @return redirects to orders
	 */
	@RequestMapping(value = "/changeDeliverer", method = RequestMethod.POST)
	public String changeDeliverer(Model model, @RequestParam("orderID") OrderIdentifier orderID) {
		orderHelper.unassignDeliverer(orderID);
		return "redirect:orders";
	}
}
