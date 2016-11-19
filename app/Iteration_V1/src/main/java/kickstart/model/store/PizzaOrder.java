package kickstart.model.store;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.stereotype.Component;

import kickstart.model.tan_management.Tan;


public class PizzaOrder extends Order{
	

	private boolean freeDrink;
	private boolean pickUp;
	private Tan newTan;
	private PizzaOrderStatus pizzaOrderStatus;
	
	public PizzaOrder(UserAccount userAccount) {
		super(userAccount);
		// TODO Auto-generated constructor stub
	}
		
	public PizzaOrder(UserAccount userAccount, PaymentMethod paymentMethod) {
		super(userAccount, paymentMethod);
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
	
	

}
