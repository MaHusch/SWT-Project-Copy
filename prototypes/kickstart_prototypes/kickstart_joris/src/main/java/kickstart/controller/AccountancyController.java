package kickstart.controller;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class AccountancyController {
	
	private final Accountancy accountancy;

	@Autowired
	public AccountancyController(Accountancy accountancy) {
		this.accountancy = accountancy;
	}
	
	@RequestMapping("/finances")
	public String finances(Model model){
		model.addAttribute("entries", accountancy.findAll());
		
		return "finances";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createEntry(@RequestParam("value") Integer value, @RequestParam("description") String description){
		
		accountancy.add(new AccountancyEntry(Money.of(value, "EUR"), description));
		return "redirect:finances";
	}
	
	@RequestMapping("/orders")
	public String orders(){
		return "orders";
	}
	
}
