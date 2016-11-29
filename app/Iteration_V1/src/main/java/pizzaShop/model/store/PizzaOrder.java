package pizzaShop.model.store;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.junit.Assert;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.tan_management.Tan;

@Entity
public class PizzaOrder {

	// @Id @GeneratedValue private long orderID;

	@EmbeddedId
	private OrderIdentifier orderIdentifier;
	private boolean freeDrink;
	private boolean pickUp;
	@OneToOne(cascade = {CascadeType.ALL})
	private Tan newTan;
	private PizzaOrderStatus pizzaOrderStatus = PizzaOrderStatus.OPEN;
	@OneToOne
	private Order order;
	private int unbakedPizzas = 0;

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
		System.out.println("Unbaked Pizzas: " + unbakedPizzas);
		Assert.assertTrue("No unbaked Pizza left!", unbakedPizzas >= 0);
		unbakedPizzas--;
		if (unbakedPizzas == 0) {
			pizzaOrderStatus = PizzaOrderStatus.READY;
			System.out.println("ready");

		}
		
		return unbakedPizzas;
	}

	public int addAsUnbaked() {
		unbakedPizzas++;
		return unbakedPizzas;
	}
	
	public int getUnbakedPizzas(){
		return unbakedPizzas;
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
	
	private void setOrderStatus(PizzaOrderStatus status)
	{
		this.pizzaOrderStatus = status;
	}
	
	public PizzaOrderStatus getOrderStatus()
	{
		return this.pizzaOrderStatus;
	}
	
	public void completeOrder() // TODO: creaty accountancyentry
	{
		this.setOrderStatus(PizzaOrderStatus.COMPLETED);
	}
	
	public void deliverOrder()
	{
		this.setOrderStatus(PizzaOrderStatus.DELIVERING);
	}
}
