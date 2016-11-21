package kickstart.model.store;

import javax.persistence.Entity;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import kickstart.model.tan_management.Tan;

@Entity
public class PizzaOrder{
	
	
	/**
	 * 
	 */
	private boolean freeDrink;
	private boolean pickUp;
	private Tan newTan;
	private PizzaOrderStatus pizzaOrderStatus;
	private Order order;
	
	
	public PizzaOrder(UserAccount userAccount) {
		setOrder(new Order(userAccount));
		// TODO Auto-generated constructor stub
	}
		
	public PizzaOrder(UserAccount userAccount, PaymentMethod paymentMethod) {
		setOrder(new Order(userAccount, paymentMethod));
		// TODO Auto-generated constructor stub
	}
	
	public Tan setTan(Tan TAN){
		newTan = TAN;
		return TAN;
	}
	
	public Tan getTan(){
		return newTan;
	}
	
	public Boolean getFreeDrink(){
		return freeDrink;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	

}
