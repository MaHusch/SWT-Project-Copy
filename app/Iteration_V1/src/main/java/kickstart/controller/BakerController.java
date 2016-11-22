package kickstart.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kickstart.model.actor.Baker;
import kickstart.model.actor.StaffMember;
import kickstart.model.catalog_item.Pizza;
import kickstart.model.store.*;

@Controller
public class BakerController {
	
	private ListIterator<Pizza> it;
	private ArrayList<Pizza> list;
	private Baker currentBaker;
	private ArrayList<Oven> myOvens = new ArrayList<Oven>();
	
	public BakerController(){
		
	}
	
	@RequestMapping("/ovens")
	public String ovenView(Model model, Principal principal){
		
		currentBaker = (Baker)Store.getInstance().getStaffMemberByName(principal.getName());
		myOvens = currentBaker.getOvens();
		
		if(currentBaker != null)
		{
			model.addAttribute("baker",currentBaker.getOvens());
			
			model.addAttribute("queue", Store.getInstance().getPizzaQueue());
		}
		

		
		return "ovens";
	}
	
	@RequestMapping(value = "/getNextPizza", method = RequestMethod.POST)
	public String getNextPizza(Model model, @ModelAttribute Oven oven){
		
		
		
		currentBaker.getNextPizza();
		currentBaker.putPizzaIntoOven(oven);
		
		
		return "redirect:ovens";
		
		 	
	}
	

}
