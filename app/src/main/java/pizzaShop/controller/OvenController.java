package pizzaShop.controller;

import static org.salespointframework.core.Currencies.EURO;

import java.security.Principal;
import java.time.Duration;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ProductionSystem.Oven;

@Controller
public class OvenController {

	private ErrorClass error = new ErrorClass(false);

	private final Store store;
	private final BusinessTime businessTime;
	private final Accountancy accountancy;

	@Autowired
	public OvenController(Store store, BusinessTime businessTime, Accountancy accountancy) {
		this.store = store;
		this.businessTime = businessTime;
		this.accountancy = accountancy;
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
		error.setError(false);
		if(minutes == null){
			error.setError(true);
			error.setMessage("Feld darf nicht leer sein!");
			
		}else{
			businessTime.forward(Duration.ofMinutes(minutes));
		}
		return "redirect:ovens";
	}

	@RequestMapping(value = "/addOven", method = RequestMethod.POST)
	public String addOven(Model model) {
	
		store.getOvens().add(new Oven(store));
		accountancy.add(new AccountancyEntry(Money.of(-1000, EURO), "Neuer Ofen gekauft"));

	
		return "redirect:ovens";
	
	}

	@RequestMapping(value = "/deleteOven", method = RequestMethod.POST)
	public String deleteOven(Model model,@RequestParam("ovenID") int id) {
		
		for(Oven o : store.getOvens())
		{
			if(o.getId() == id && !o.isEmpty())
			{
				error.setError(true);
				error.setMessage("Ofen ist nicht leer");

				return "redirect:ovens";
			}
		}
		store.deleteOven(id);
	
		return "redirect:ovens";
	}

}
