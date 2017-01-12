package pizzaShop.model.OrderSystem;

import java.util.Iterator;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.DataBaseSystem.StaffMemberRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;

@Component
public class CartHelper {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final StaffMemberRepository staffMemberRepository;
	private final BusinessTime businesstime;
	private Optional<Customer> customer = Optional.empty();
	private final Store store;

	@Autowired
	public CartHelper(OrderManager<Order> orderManager, ItemCatalog itemCatalog, TanManagement tanManagement,
			CustomerRepository customerRepository, PizzaOrderRepository pizzaOrderRepository,
			StaffMemberRepository staffMemberRepository, Store store, BusinessTime businesstime) {
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.staffMemberRepository = staffMemberRepository;
		this.store = store;
		this.businesstime = businesstime;
	}

	public void removeItem(String cartId, Cart cart) {

		cart.removeItem(cartId);

	}

	public void changeQuantity(Item item, int amount, Cart cart) throws Exception{
		if(item.equals(null))
			throw new IllegalArgumentException("Produkt existiert nicht mehr!");
		
		cart.addOrUpdateItem(item, Quantity.of(amount));

		if (cart.getPrice().getNumber().intValue() < 30) {
			Iterator<CartItem> ci = cart.iterator();
			while (ci.hasNext()) {
				CartItem i = ci.next();
				if (((Item) i.getProduct()).getType().equals(ItemType.FREEDRINK)) {
					cart.removeItem(i.getId());
					break;
				}
			}

		}

	}

	public void createPizzaOrder(boolean cutlery, boolean onSite, UserAccount userAccount, Cart cart, Customer customer) throws Exception {
		if(userAccount.equals(null))
			throw new IllegalArgumentException("Nicht eingeloggt!");
		if(customer.equals(null))
			throw new IllegalArgumentException("Kein Kunde vorhanden!");
		if(cart.isEmpty())
			throw new Exception("Warenkorb ist leer!");
	
		// TODO: check if customer already has a cutlery --> throw error
		if (cutlery) {
			if(!customer.getCutlery().equals(null)){
				throw new Exception("Kunde hat schon ein Besteck ausgeliehen!");
			}
			// if false --> return error
			store.lentCutlery(customer, businesstime.getTime());
		}

		PizzaOrder pizzaOrder = new PizzaOrder(userAccount, Cash.CASH,
				tanManagement.generateNewTan(customer.getPerson().getTelephoneNumber()), onSite, customer);
		cart.addItemsTo(orderManager.save(pizzaOrder.getOrder()));
		store.analyzeOrder(pizzaOrder);
		cart.clear();

		// Bill bill = new Bill(customer.get(), pizzaOrder,
		// businesstime.getTime());
		// customer = Optional.empty(); disabled for testing purposes

	}

	public Optional<Customer> checkTan(Tan tan) {

		for (Customer c : customerRepository.findAll()) {
			if (tanManagement.getTelephoneNumber(tan).equals(c.getPerson().getTelephoneNumber())) {
				System.out.println(
						"valid: " + tan.getTanNumber() + " tel: " + c.getPerson().getTelephoneNumber());
				return Optional.of(c);
				
			}

		}
		Optional<Customer> c = Optional.empty();
		return c;

	}

}
