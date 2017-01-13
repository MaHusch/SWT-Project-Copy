package pizzaShop.model.OrderSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.util.Iterator;
import java.util.Optional;

import org.javamoney.moneta.Money;
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

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.ProductionSystem.Oven;

@Component
public class CartHelper {

	private final OrderManager<Order> orderManager;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final BusinessTime businessTime;
	private final Store store;

	@Autowired
	public CartHelper(OrderManager<Order> orderManager, TanManagement tanManagement,
			CustomerRepository customerRepository, Store store, BusinessTime businesstime) {
		this.orderManager = orderManager;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.store = store;
		this.businessTime = businesstime;
	}

	/**
	 * changes quantity of a {@link CartItem} by +- 1
	 * 
	 * @param item
	 *            {@link Item} in the changed {@link CartItem}
	 * @param amount
	 *            amount the quantity is altered by
	 * @param cart
	 *            the {@link Cart} in which the {@link CartItem} is saved
	 * @throws Exception
	 *             if the {@link Item} is null
	 */
	public void changeQuantity(Item item, int amount, Cart cart) throws Exception {
		if (item == null)
			throw new IllegalArgumentException("Produkt existiert nicht mehr!");

		cart.addOrUpdateItem(item, Quantity.of(amount));
		updateFreeDrink(cart);
		

	}
	
	public void updateFreeDrink(Cart cart){
		final int FREE_DRINK_PRICE_THRESHOLD = 30;
		if (cart.getPrice().getNumber().intValue() < FREE_DRINK_PRICE_THRESHOLD) {
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

	/**
	 * creates {@link PizzaOrder} from the contents of the {@link Cart}
	 * 
	 * @param cutlery
	 *            boolean: is a cutlery being lent?
	 * @param onSite
	 *            boolean: is the order picked up by the customer?
	 * @param userAccount
	 *            currently logged in {@link UserAccount}
	 * @param cart
	 *            the {@link Cart} from which the {@link PizzaOrder} is created
	 * @param customer
	 *            {@link Customer} behind the order
	 * @throws Exception
	 *             if either userAccount or customer are null; if cart is empty
	 * 
	 */
	public void createPizzaOrder(boolean cutlery, boolean onSite, UserAccount userAccount, Cart cart, Customer customer, String remark)
			throws Exception {
		if (userAccount == null)
			throw new IllegalArgumentException("Nicht eingeloggt!");
		if (customer == null)
			throw new IllegalArgumentException("Kein Kunde vorhanden!");
		if (cart.isEmpty())
			throw new IllegalArgumentException("Warenkorb ist leer!");

		// TODO: check if customer already has a cutlery --> throw error
		if (cutlery) {
			
			if (customer.getCutlery() != null){
				throw new IllegalArgumentException("Kunde hat schon ein Besteck ausgeliehen!");
			}
			// if false --> return error
			
			customer.setCutlery(new Cutlery(Money.of(15.0, EURO), businessTime.getTime()));
			customer = customerRepository.save(customer);

		}

		PizzaOrder pizzaOrder = new PizzaOrder(userAccount, Cash.CASH,
				tanManagement.generateNewTan(customer.getPerson().getTelephoneNumber()), onSite, customer);
		pizzaOrder.setRemark(remark);
		cart.addItemsTo(orderManager.save(pizzaOrder.getOrder()));
		store.analyzePizzaOrder(pizzaOrder);
		cart.clear();

		// Bill bill = new Bill(customer.get(), pizzaOrder,
		// businesstime.getTime());
		// customer = Optional.empty(); disabled for testing purposes

	}

	/**
	 * checks if the given {@link Tan} has an attached {@link Customer}
	 * 
	 * @param tan
	 *            {@link Tan} to be checked
	 * @return Optional of {@link Customer} or Optional.empty is no customer is
	 *         found
	 */
	public Optional<Customer> checkTan(Tan tan) {

		for (Customer c : customerRepository.findAll()) {
			if (tanManagement.getTelephoneNumber(tan).equals(c.getPerson().getTelephoneNumber())) {
				return Optional.of(c);
			}
		}
		return Optional.empty();
	}

	/**
	 * calculates time until all {@link Pizza}'s in the {@link PizzaQueue} are
	 * finished
	 * 
	 * @return the time in seconds
	 */
	public int pizzaQueueTime() {

		final int SECONDS_TO_BAKE = 300;
		int timeLeftInQueue = store.getPizzaQueue().size() * SECONDS_TO_BAKE;

		for (Oven o : store.getOvens()) {
			timeLeftInQueue +=  (o.getBakerTimer() == null) ? 0 : o.getBakerTimer().getCounter();
					
		}

		return timeLeftInQueue;
	}

}
