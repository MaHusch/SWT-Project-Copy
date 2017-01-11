package pizzaShop.controller;

import static org.salespointframework.core.Currencies.EURO;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
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

	@Autowired
	public AccountingController(Store store,Accountancy accountancy, AccountingHelper accountingHelper, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.accountingHelper = accountingHelper;
		this.businessTime = businessTime;
		this.store = store;
	}

	@RequestMapping("/finances")
	public String finances(Model model) {
		LocalDateTime frstMon = businessTime.getTime();
		frstMon = frstMon.minusDays(frstMon.getDayOfYear()-1);
		while(true){
			if(frstMon.getDayOfWeek().equals(DayOfWeek.MONDAY))
				break;
			frstMon = frstMon.plusDays(1);
		}
		
		LocalDateTime displayTime = businessTime.getTime().plusWeeks(offsetW);
		displayTime = displayTime.minusDays(displayTime.getDayOfWeek().getValue()-1);
		currentW = ((displayTime.getDayOfYear()-frstMon.getDayOfYear()+1) / 7)+1;
		Interval i = Interval.from(displayTime.minusDays(1)).to(displayTime.minusDays(1).plusWeeks(1));
		model.addAttribute("entries", accountancy.findAll());
		model.addAttribute("currentDisplay", accountancy.find(i));	
		model.addAttribute("displayInterval", i);//displayTime.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN)+" "+displayTime.getYear());
		model.addAttribute("currentWeek", currentW);
		model.addAttribute("currentTime", businessTime.getTime());
		model.addAttribute("totalGain", accountingHelper.total());
		model.addAttribute("weeklyGain", accountingHelper.intervalTotal(i)); 
		store.checkCutleries();
		
		return "finances";
	}
	
	@RequestMapping(value = "/decreaseWeek", method = RequestMethod.POST)
	public String decreaseWeek() {
		offsetW -= 1;
		return "redirect:finances";
	}
	
	
	@RequestMapping(value = "/increaseWeek", method = RequestMethod.POST)
	public String increaseWeek() {
		offsetW += 1;
		return "redirect:finances";
	}
	
	

	@RequestMapping(value = "/createAccountancyEntry", method = RequestMethod.POST)
	public String createEntry(@RequestParam("value") Integer value, @RequestParam("description") String description) {

		accountancy.add(new AccountancyEntry(Money.of(value, EURO), description));
		return "redirect:finances";
	}


	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public String payOrder(@RequestParam("value") Integer value, @LoggedIn Optional<UserAccount> userAccount) {

		if (!userAccount.isPresent())
			return "redirect:login";
		Order order = new Order(userAccount.get(), Cash.CASH);
		order.add(new OrderLine(new Product("test", Money.of(value, EURO)), Quantity.of(1)));
		accountancy.add(ProductPaymentEntry.of(order, "Order by " + userAccount.get().getId() + ": " + order.getId()));
		return "redirect:finances";
	}

	@RequestMapping(value = "/forward", method = RequestMethod.POST)
	public String forward(@RequestParam("days") Integer days) {
		//accountingMethods.
		
		int tDays = 0;
		int cDays = 0;
		LocalDateTime cTime = businessTime.getTime();
		while(tDays < days){
			int cMonth = cTime.getMonthValue(); 
			cTime = cTime.plusDays(1);
			tDays++;
			cDays++;
			if(cTime.getMonthValue() > cMonth){
				businessTime.forward(Duration.ofDays(cDays));
				cDays = 0;
				try {
					
					Thread.sleep(store.getStaffMemberList().size()*15);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(tDays == days){
				businessTime.forward(Duration.ofDays(cDays));
			}
		}
		
		//businessTime.forward(Duration.ofDays(days));
		
		
		return "redirect:finances";
	}

}
