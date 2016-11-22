package pizzaShop.model.store;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.tan_management.Tan;

@Entity
public class PizzaOrder{

	//@Id @GeneratedValue private long orderID;
	
	@EmbeddedId private OrderIdentifier orderIdentifier;
	private boolean freeDrink;
	private boolean pickUp;
	@Transient private Tan newTan;
	//private PizzaOrderStatus pizzaOrderStatus;
	private final Order order;
	
	
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
	
	
	
	public Boolean getFreeDrink(){
		return freeDrink;
	}

	public Order getOrder() {
		return order;
	}
	
	

}
