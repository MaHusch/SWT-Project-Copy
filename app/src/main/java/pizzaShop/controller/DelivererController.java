package pizzaShop.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.AccountSystem.DelivererHelper;
import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.PizzaOrder;

@Controller
public class DelivererController {

	String username;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final TanManagement tanManagement;
	private final Store store;
	private final DelivererHelper delivererHelper;

	@Autowired
	public DelivererController(PizzaOrderRepository pizzaOrderRepository, TanManagement tanManagement, Store store,
			DelivererHelper delivererHelper) {
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.tanManagement = tanManagement;
		this.store = store;
		this.delivererHelper = delivererHelper;

	}

	@RequestMapping("/sDeliverer")
	public String sDeliverer(Principal prinicpal, Model model) {

		StaffMember s = store.getStaffMemberByUsername(prinicpal.getName());
		if (!s.getRole().getName().contains("DELIVERER")) {
			return "index";
		}
		Deliverer currentDeliverer = (Deliverer) s;

		model.addAttribute("available", currentDeliverer.getAvailable());

		ArrayList<PizzaOrder> delivererOrders = new ArrayList<PizzaOrder>();

		for (OrderIdentifier oId : currentDeliverer.getOrders()) {
			PizzaOrder pO = pizzaOrderRepository.findOne(oId);
			if (pO != null)
				delivererOrders.add(pO);
		}

		model.addAttribute("orders", delivererOrders);

		return "sDeliverer";
	}

	// TODO: remove redundancy
	@RequestMapping("/checkOut")
	public String checkOut(Model model, Principal principal) {
		Deliverer currentDeliverer = (Deliverer) store.getStaffMemberByUsername(principal.getName());
		delivererHelper.checkOut(currentDeliverer);
		return "redirect:sDeliverer";
	}

	@RequestMapping("/checkIn")
	public String checkIn(Model model, Principal principal)	{ // TODO: check OrderStatus change
		Deliverer currentDeliverer = (Deliverer) store.getStaffMemberByUsername(principal.getName());
		delivererHelper.checkIn(currentDeliverer);
		return "redirect:sDeliverer";
	}
}
