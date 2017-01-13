package pizzaShop.controller;

import java.security.Principal;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ProductionSystem.OvenHelper;

@Controller
public class OvenController {

	private ErrorClass error = new ErrorClass(false);

	private final Store store;
	private final BusinessTime businessTime;
	private final OvenHelper ovenHelper;

	@Autowired
	public OvenController(Store store, BusinessTime businessTime, OvenHelper ovenHelper) {
		this.store = store;
		this.businessTime = businessTime;
		this.ovenHelper = ovenHelper;
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
		error.setError(false);
		try {
			ovenHelper.putPizzaIntoOven(ovenID);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
		}
		return "redirect:ovens";
	}

	@RequestMapping(value = "/forward2", method = RequestMethod.POST)
	public String forward(@RequestParam("minutes") Integer minutes) {
		error.setError(false);
		try {
			ovenHelper.forward(minutes);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
		}

		return "redirect:ovens";
	}

	@RequestMapping(value = "/addOven", method = RequestMethod.POST)
	public String addOven(Model model) {
		ovenHelper.addOven();
		return "redirect:ovens";

	}

	@RequestMapping(value = "/deleteOven", method = RequestMethod.POST)
	public String deleteOven(Model model, @RequestParam("ovenID") int id) {
		error.setError(false);
		try {
			ovenHelper.deleteOven(id);
		} catch (Exception e) {
			error.setError(true);
			error.setMessage(e.getMessage());
		}
		return "redirect:ovens";
	}

}
