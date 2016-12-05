package pizzaShop.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.Store;

@Controller
public class BakerController {
	
	private Baker currentBaker;
	private ErrorClass error = new ErrorClass(false);
	
	private final Store store;
	
	@Autowired
	public BakerController(Store store){
		this.store = store;
	}
	
	@RequestMapping("/ovens")
	public String ovenView(Model model, Principal principal){
			
			currentBaker = (Baker) store.getStaffMemberByName(principal.getName());
			
			model.addAttribute("ovens",store.getOvens());
			model.addAttribute("queue", store.getPizzaQueue());
			model.addAttribute("error", error );
		return "ovens";
	}
	
	@RequestMapping(value = "/getNextPizza", method = RequestMethod.POST)
	public String getNextPizza(Model model, @RequestParam int ovenID){
		
		for(int i = 0; i < store.getOvens().size(); i++){
			
			if(store.getOvens().get(i).getId() == ovenID){
				
				if(store.getOvens().get(i).isEmpty()){
					try{
					store.getNextPizza();
					error.setError(false);
					store.putPizzaIntoOven(store.getOvens().get(i));
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
