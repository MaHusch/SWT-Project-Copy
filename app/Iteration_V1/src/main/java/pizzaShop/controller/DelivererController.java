package pizzaShop.controller;

import java.security.Principal;
import java.util.Iterator;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Deliverer;
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
	
	@Autowired
	public DelivererController(PizzaOrderRepository pizzaOrderRepository,TanManagement tanManagement)
	{
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.tanManagement = tanManagement;
	}
	
	//TODO: remove redundancy
	@RequestMapping("/checkOut")
	public String checkOut(Principal principal)
	{
		username = principal.getName();
		System.out.println(username);
		// startpage for deliverer as extra template ?!
		currentDeliverer = (Deliverer) Store.getInstance().getStaffMemberByName(username);
		
		currentDeliverer.checkOut();
		System.out.println(currentDeliverer.getAvailable());
		
		//TODO: remove redundancy
		Iterator<OrderIdentifier> i = currentDeliverer.getOrders().iterator();
		
		while(i.hasNext())
		{
			OrderIdentifier deliveredOrder = i.next();
			
			
			for(PizzaOrder p : pizzaOrderRepository.findAll())
			{
				if(p.getId().equals(deliveredOrder))
				{
					p.deliverOrder();
				}
			}
		}
		
		return "redirect:index";
	}
	
	@RequestMapping("/checkIn")
	public String checkIn(Principal principal) //TODO: check OrderStatus change
	{
		username = principal.getName();
		System.out.println(username);
		// startpage for deliverer as extra template ?!
		currentDeliverer = (Deliverer) Store.getInstance().getStaffMemberByName(username);
		
		currentDeliverer.checkIn();
		System.out.println(currentDeliverer.getAvailable());
		
		// when deliverer returns , every order he delivered is delivered --> change OrderStatus
		Iterator<OrderIdentifier> i = currentDeliverer.getOrders().iterator();
		
		while(i.hasNext())
		{
			OrderIdentifier deliveredOrder = i.next();
			
			
			for(PizzaOrder p : pizzaOrderRepository.findAll())
			{
				if(p.getId().equals(deliveredOrder))
				{
					tanManagement.confirmTan( p.getTan());
					p.completeOrder();
				}
			}
		}
		
		currentDeliverer.clearOrders();
		
		return "redirect:index";
	}
}
