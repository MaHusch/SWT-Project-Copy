package pizzaShop.controller;

import java.security.Principal;
import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.*;

@Controller
public class BakerController {
	
	private Baker currentBaker;
	private ErrorClass error = new ErrorClass(false);
	
	public BakerController(){}
	
	@RequestMapping("/ovens")
	public String ovenView(Model model, Principal principal){
			
			currentBaker = (Baker) Store.getInstance().getStaffMemberByName(principal.getName());
			
			model.addAttribute("ovens",Store.getInstance().getOvens());
			model.addAttribute("queue", Store.getInstance().getPizzaQueue());
			model.addAttribute("error", error );
		return "ovens";
	}
	
	@RequestMapping(value = "/getNextPizza", method = RequestMethod.POST)
	public String getNextPizza(Model model, @RequestParam int ovenID){
		
		for(int i = 0; i < Store.getInstance().getOvens().size(); i++){
			
			if(Store.getInstance().getOvens().get(i).getId() == ovenID){
				
				if(Store.getInstance().getOvens().get(i).isEmpty()){
					try{
					currentBaker.getNextPizza();
					error.setError(false);
					currentBaker.putPizzaIntoOven(Store.getInstance().getOvens().get(i));
					return "redirect:ovens";
					}
					catch (Exception e){
						e.printStackTrace();
						error.setError(true);
						return "redirect:ovens";
					}
				}
			}
		}
		
		return "redirect:ovens";		 	
	}
	

}
