package pizzaShop.controller;

import java.security.Principal;
import java.time.Duration;

import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.Store;

@Controller
public class BakerController {

	private ErrorClass error = new ErrorClass(false);

	private final Store store;
	private final BusinessTime businessTime;

	@Autowired
	public BakerController(Store store, BusinessTime businessTime) {
		this.store = store;
		this.businessTime = businessTime;
	}

	@RequestMapping("/ovens")
	public String ovenView(Model model, Principal principal) {

		model.addAttribute("ovens", store.getOvens());
		model.addAttribute("queue", store.getPizzaQueue());
		model.addAttribute("error", error);
		return "ovens";
	}

	@RequestMapping(value = "/getNextPizza", method = RequestMethod.POST)
	public String getNextPizza(Model model, @RequestParam int ovenID) {

		for (int i = 0; i < store.getOvens().size(); i++) {

			if (store.getOvens().get(i).getId() == ovenID) {

				if (store.getOvens().get(i).isEmpty()) {
					try {
						store.getNextPizza();
						error.setError(false);
						store.putPizzaIntoOven(store.getOvens().get(i));
						return "redirect:ovens";
					} catch (Exception e) {
						e.printStackTrace();
						error.setError(true);
						return "redirect:ovens";
					}
				}
			}
		}

		return "redirect:ovens";
	}
	
	@RequestMapping(value ="/forward2", method = RequestMethod.POST)
	public String forward(@RequestParam("minutes") Integer minutes){
		
		businessTime.forward(Duration.ofMinutes(minutes));
		
		
		return "redirect:ovens";
	}

}
