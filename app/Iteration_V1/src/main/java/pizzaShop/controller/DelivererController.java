package pizzaShop.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.store.Store;

@Controller
public class DelivererController {

	private Deliverer currentDeliverer;
	String username;
	
	public void DelivererController()
	{
		
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
		
		
		return "redirect:index";
	}
	
	@RequestMapping("/checkIn")
	public String checkIn(Principal principal)
	{
		username = principal.getName();
		System.out.println(username);
		// startpage for deliverer as extra template ?!
		currentDeliverer = (Deliverer) Store.getInstance().getStaffMemberByName(username);
		
		currentDeliverer.checkIn();
		System.out.println(currentDeliverer.getAvailable());
		
		return "redirect:index";
	}
}
