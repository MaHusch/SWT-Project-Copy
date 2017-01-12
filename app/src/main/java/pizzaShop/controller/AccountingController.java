package pizzaShop.controller;

import java.time.LocalDateTime;

import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.AccountingSystem.AccountingHelper;
import pizzaShop.model.ManagementSystem.Store;

@Controller
public class AccountingController {

	private final Accountancy accountancy;
	private final AccountingHelper accountingHelper;
	private final BusinessTime businessTime;
	private final Store store;
	private int offsetW = 0;
	private int currentW = 0;
	private ErrorClass error = new ErrorClass(false);

	@Autowired
	public AccountingController(Store store, Accountancy accountancy, AccountingHelper accountingHelper,
			BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.accountingHelper = accountingHelper;
		this.businessTime = businessTime;
		this.store = store;
	}

	@RequestMapping("/finances")
	public String finances(Model model) {
		LocalDateTime frstMon = accountingHelper.getFirstMonday();
		LocalDateTime displayTime = businessTime.getTime().plusWeeks(offsetW);
		
		displayTime = displayTime.minusDays(displayTime.getDayOfWeek().getValue() - 1);
		currentW = ((displayTime.getDayOfYear() - frstMon.getDayOfYear() + 1) / 7) + 1;
		Interval i = Interval.from(displayTime.minusDays(1)).to(displayTime.minusDays(1).plusWeeks(1));
		model.addAttribute("entries", accountancy.findAll());
		model.addAttribute("currentDisplay", accountancy.find(i));
		model.addAttribute("displayInterval", i);// displayTime.getMonth().getDisplayName(TextStyle.FULL,
													// Locale.GERMAN)+"
													// "+displayTime.getYear());
		model.addAttribute("currentWeek", currentW);
		model.addAttribute("currentTime", businessTime.getTime());
		model.addAttribute("totalGain", accountingHelper.total());
		model.addAttribute("weeklyGain", accountingHelper.intervalTotal(i));
		model.addAttribute("error", error);
		store.checkCutleries();

		return "finances";
	}

	@RequestMapping(value = "/changeWeek", method = RequestMethod.POST)
	public String changeWeek(@RequestParam("amount") int amount) {
		offsetW += amount;
		return "redirect:finances";
	}

	@RequestMapping(value = "/resetWeek", method = RequestMethod.POST)
	public String increaseWeek() {
		offsetW = 0;
		return "redirect:finances";
	}

/*
	@RequestMapping(value = "/createAccountancyEntry", method = RequestMethod.POST)
	public String createEntry(@RequestParam("value") Integer value, @RequestParam("description") String description) {
		if(!description.isEmpty() && value != null) 
			accountancy.add(new AccountancyEntry(Money.of(value, EURO), description));
		else //errorMeldung ; 
		{}
		return "redirect:finances";
	}

*/

	@RequestMapping(value = "/forward", method = RequestMethod.POST)
	public String forward(@RequestParam("days") Integer days) {
		error.setError(false);
		if(days == null){
			error.setError(true);
			error.setMessage("Feld darf nicht leer sein!");
			
		}else{
			accountingHelper.forward(days);
		}
		return "redirect:finances";
	}

}
