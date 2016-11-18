package kickstart.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kickstart.model.actor.Baker;
import kickstart.model.actor.StaffMember;
import kickstart.model.catalog_item.Pizza;
import kickstart.model.store.*;

@Controller
public class BakerController {
	
	private ListIterator<Pizza> it;
	private ArrayList<Pizza> list;	
	
	public BakerController(){
		
	}
	
	@RequestMapping("/ovens")
	public String ovenView(Model model, Principal principal){
		
		Baker currentBaker = (Baker)Store.getInstance().getStaffMemberByName(principal.getName());
		
		if(currentBaker != null)
		{
			model.addAttribute("baker",currentBaker.getOvens());
			
			model.addAttribute("queue", Store.getInstance().getPizzaQueue());
		}
		

		
		return "ovens";
	}
	

}
