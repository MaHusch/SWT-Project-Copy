package pizzaShop.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.PizzaOrder;
import pizzaShop.model.store.PizzaOrderRepository;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.TanManagement;

@Controller
public class DelivererController {

	private Deliverer currentDeliverer;
	String username;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final TanManagement tanManagement;
	private final Store store;

	@Autowired
	public DelivererController(PizzaOrderRepository pizzaOrderRepository, TanManagement tanManagement, Store store) {
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.tanManagement = tanManagement;
		this.store = store;
	

	}

	@RequestMapping("/sDeliverer")
	public String sDeliverer(Principal prinicpal, Model model) {

		// TODO: what if not deliverer? (maybe check Class before
		currentDeliverer = (Deliverer) store.getStaffMemberByName(prinicpal.getName());
		
		model.addAttribute("available", currentDeliverer.getAvailable());

		ArrayList<PizzaOrder> delivererOrders = new ArrayList<PizzaOrder>();

		for (OrderIdentifier oId : currentDeliverer.getOrders()) {
			PizzaOrder pO = pizzaOrderRepository.findOne(oId);
			if (!pO.equals(null))
				delivererOrders.add(pO);

		}

		model.addAttribute("orders", delivererOrders);

		return "sDeliverer";
	}

	// TODO: remove redundancy
	@RequestMapping("/checkOut")
	public String checkOut(Model model, Principal principal) {

		currentDeliverer.checkOut();
		System.out.println(currentDeliverer.getAvailable());
		System.out.println(currentDeliverer.getOrders());
		// TODO: remove redundancy
		for (OrderIdentifier oi : currentDeliverer.getOrders()) {

			for (PizzaOrder p : pizzaOrderRepository.findAll()) {
				if (oi.equals(p.getId())) {
					System.out.println(p.getOrderStatus());
					p.deliverOrder();
					pizzaOrderRepository.save(p);
					System.out.println(p.getOrderStatus());
				}
			}

		}

		return "redirect:sDeliverer";
	}

	@RequestMapping("/checkIn")
	public String checkIn(Model model, Principal principal) // TODO: check
															// OrderStatus
															// change
	{
		username = principal.getName();
		System.out.println(username);
		// startpage for deliverer as extra template ?!
		currentDeliverer = (Deliverer) store.getStaffMemberByName(username);

		currentDeliverer.checkIn();
		System.out.println(currentDeliverer.getAvailable());

		
		for (OrderIdentifier oi : currentDeliverer.getOrders()) {

			for (PizzaOrder p : pizzaOrderRepository.findAll()) {
				if (oi.equals(p.getId())) {
					tanManagement.confirmTan(p.getTan()); // TODO: assign new
					// TAN?
					p.completeOrder();
					pizzaOrderRepository.save(p);
				}
			}

		}

		currentDeliverer.clearOrders();
		return "redirect:sDeliverer";
	}
}
