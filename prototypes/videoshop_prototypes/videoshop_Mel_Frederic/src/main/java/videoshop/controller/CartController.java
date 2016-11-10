/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package videoshop.controller;

import videoshop.model.Disc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.money.MonetaryAmount;
import javax.money.NumberValue;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.core.AbstractEntity;
import org.salespointframework.inventory.Inventory;
import org.salespointframework.inventory.InventoryItem;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.CreditCard;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * A Spring MVC controller to manage the {@link Cart}. {@link Cart} instances are held in the session as they're
 * specific to a certain user. That's also why the entire controller is secured by a {@code hasRole(…)} clause.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
class CartController {

	private final OrderManager<Order> orderManager;
	
	/**
	 * Creates a new {@link CartController} with the given {@link OrderManager}.
	 * 
	 * @param orderManager must not be {@literal null}.
	 */
	@Autowired
	public CartController(OrderManager<Order> orderManager) {
		
		Assert.notNull(orderManager, "OrderManager must not be null!");
		this.orderManager = orderManager;
	}

	/**
	 * Creates a new {@link Cart} instance to be stored in the session (see the class-level {@link SessionAttributes}
	 * annotation).
	 * 
	 * @return a new {@link Cart} instance.
	 */
	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
	}

	/**
	 * Adds a {@link Disc} to the {@link Cart}. Note how the type of the parameter taking the request parameter
	 * {@code pid} is {@link Disc}. For all domain types extening {@link AbstractEntity} (directly or indirectly) a tiny
	 * Salespoint extension will directly load the object instance from the database. If the identifier provided is
	 * invalid (invalid format or no {@link Product} with the id found), {@literal null} will be handed into the method.
	 * 
	 * @param disc
	 * @param number
	 * @param session
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/cart", method = RequestMethod.POST)
	public String addDisc(@RequestParam("pid") Disc disc, @RequestParam("number") int number, @ModelAttribute Cart cart) {
		
		// (｡◕‿◕｡)
		// Das Inputfeld im View ist eigentlich begrenz, allerdings sollte man immer Clientseitig validieren

		int amount = number <= 0 || number > 5 ? 1 : number;
		
	
		// (｡◕‿◕｡)
		// Eine OrderLine besteht aus einem Produkt und einer Quantity, diese kann auch direkt in eine Order eingefügt
		// werden
		cart.addOrUpdateItem(disc, Quantity.of(amount));			
		// (｡◕‿◕｡)
		// Je nachdem ob disc eine Dvd oder eine Bluray ist, leiten wir auf die richtige Seite weiter

		switch (disc.getType()) {
			case DVD:
				return "redirect:dvdCatalog";
			case BLURAY:
			default:
				return "redirect:blurayCatalog";
		}
	}

	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public String basket() {
		return "cart";
	}

	/**
	 * Checks out the current state of the {@link Cart}. Using a method parameter of type {@code Optional<UserAccount>}
	 * annotated with {@link LoggedIn} you can access the {@link UserAccount} of the currently logged in user.
	 * 
	 * @param cart will never be {@literal null}.
	 * @param userAccount will never be {@literal null}.
	 * @return
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart,
			@RequestParam("card associate name") 			String 			cardAssociationName,
			@RequestParam("card number") 					String 			cardNumber,
			@RequestParam("card name on card") 				String 			nameOnCard,
			@RequestParam("card billing adress") 			String 			billingAddress,
			@RequestParam("card valid from") 				String 			validFrom,
			@RequestParam("card expiration date") 		    String 			expirationDate,
			@RequestParam("card verification code") 		String 			cardVerificationCode,
			@RequestParam("card withdrawal limit") 			int 			withdrawalLimit,
			@RequestParam("card credit limit") 				int 			creditLimit,
			@LoggedIn Optional<UserAccount> userAccount, Model model) {

		model.addAttribute("warning", "missing information required");
		return userAccount.map(account -> {
			
			
			// (｡◕‿◕｡)
			// Mit commit wird der Warenkorb in die Order überführt, diese wird dann bezahlt und abgeschlossen.
			// Orders können nur abgeschlossen werden, wenn diese vorher bezahlt wurden.
			
			
			if ( (cardAssociationName == "") || (cardNumber == "") || (nameOnCard == "") || 
			     (billingAddress == "") || (cardVerificationCode == "")) {
				return "redirect:/cart";
			}
			
			
			// Parsed den input der html Seite da das Datum als String wiedergeben wird aber der Contructor  
			// von Credit Card das Datum von Typ "LocalDateTime" braucht 
			
			LocalDateTime isvalidFrom = null;
			LocalDateTime expiresAt = null;
			if (expirationDate != "" && validFrom != ""){
				String unformatedExpirationDate = expirationDate.concat(" 00:00");
				String unformatedValidFrom = validFrom.concat(" 00:00");
				
				DateTimeFormatter date_format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				
				isvalidFrom = LocalDateTime.parse(unformatedValidFrom, date_format);
				expiresAt = LocalDateTime.parse(unformatedExpirationDate, date_format);
			}
			else {
				model.addAttribute("warning", "missing information required");
				return "redirect:/cart";
			}

			
			
			// konvertiert die credit Card limits in "Monetarymounts"
			MonetaryAmount cardWithdrawalLimit = null;
			MonetaryAmount cardCreditLimit = null;
			if (withdrawalLimit > 0 && creditLimit > 0) {
				 cardWithdrawalLimit = Money.of(withdrawalLimit, "EUR");
				 cardCreditLimit = Money.of(creditLimit, "EUR");
			}
			else {
				return "redirect:/cart";
			}
			
			CreditCard userCreditCard = new CreditCard(cardAssociationName, cardNumber, nameOnCard, billingAddress, 
					isvalidFrom, expiresAt, cardVerificationCode, cardWithdrawalLimit, cardCreditLimit);
			
			
			
			Order order = new Order(account, userCreditCard);
			
			cart.addItemsTo(order);

			orderManager.payOrder(order);
			orderManager.completeOrder(order);
			
			cart.clear();

			return "redirect:/";
		}).orElse("redirect:/cart");
	}
}
