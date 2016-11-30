package pizzaShop.controller;

import java.time.Duration;
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
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.store.AccountingMethods;

@Controller
public class AccountingController {

	private final Accountancy accountancy;
	private final AccountingMethods accountingMethods;
	private final BusinessTime businessTime;

	@Autowired
	public AccountingController(Accountancy accountancy, AccountingMethods accountingMethods, BusinessTime businessTime) {
		this.accountancy = accountancy;
		this.accountingMethods = accountingMethods;
		this.businessTime = businessTime;
	}

	@RequestMapping("/finances")
	public String finances(Model model) {
		model.addAttribute("entries", accountancy.findAll());
		model.addAttribute("totalGain", accountingMethods.total());

		return "finances";
	}

	@RequestMapping(value = "/createAccountancyEntry", method = RequestMethod.POST)
	public String createEntry(@RequestParam("value") Integer value, @RequestParam("description") String description) {

		accountancy.add(new AccountancyEntry(Money.of(value, "EUR"), description));
		return "redirect:finances";
	}


	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	public String payOrder(@RequestParam("value") Integer value, @LoggedIn Optional<UserAccount> userAccount) {

		if (!userAccount.isPresent())
			return "redirect:login";
		Order order = new Order(userAccount.get(), Cash.CASH);
		order.add(new OrderLine(new Product("test", Money.of(value, "EUR")), Quantity.of(1)));
		accountancy.add(ProductPaymentEntry.of(order, "Order by " + userAccount.get().getId() + ": " + order.getId()));
		return "redirect:finances";
	}

	@RequestMapping(value = "/forward", method = RequestMethod.POST)
	public String forward(@RequestParam("days") Integer days) {
		businessTime.forward(Duration.ofDays(days));
		return "redirect:finances";
	}

}
