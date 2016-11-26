package pizzaShop.model.store;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.junit.Assert;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.tan_management.Tan;

@Entity
public class PizzaOrder {

	// @Id @GeneratedValue private long orderID;

	@EmbeddedId
	private OrderIdentifier orderIdentifier;
	private boolean freeDrink;
	private boolean pickUp;
	@OneToOne
	private Tan newTan;
	private PizzaOrderStatus pizzaOrderStatus = PizzaOrderStatus.OPEN;
	@OneToOne
	private Order order;
	private int unbakedPizzas;

	public PizzaOrder() {
	}

	public PizzaOrder(UserAccount userAccount, Tan newTan) {
		this.order = new Order(userAccount);
		this.newTan = newTan;
		orderIdentifier = order.getId();
		// TODO Auto-generated constructor stub
	}

	public PizzaOrder(UserAccount userAccount, PaymentMethod paymentMethod, Tan newTan) {
		this.order = new Order(userAccount, paymentMethod);
		this.newTan = newTan;
		orderIdentifier = order.getId();
		// TODO Auto-generated constructor stub
	}

	public int markAsBaked() {
		unbakedPizzas--;
		Assert.assertTrue("No unbaked Pizza left!", unbakedPizzas >= 0);
		if (unbakedPizzas == 0) {
			pizzaOrderStatus = PizzaOrderStatus.READY;
		}

		return unbakedPizzas;
	}

	public int addAsUnbaked() {
		return unbakedPizzas++;
	}

	public Boolean getFreeDrink() {
		return freeDrink;
	}

	public Order getOrder() {
		return order;
	}

	public OrderIdentifier getId() {
		return orderIdentifier;
	}

}
