package pizzaShop.model.OrderSystem;

import java.util.Iterator;

import javax.money.MonetaryAmount;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import org.junit.Assert;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;

@Entity
public class PizzaOrder {

	// @Id @GeneratedValue private long orderID;

	@EmbeddedId
	private OrderIdentifier orderIdentifier;
	private boolean freeDrink;
	private boolean pickUp;
	private String deliverername;

	@OneToOne(cascade = { CascadeType.ALL })
	private Tan newTan;

	@Enumerated(EnumType.STRING)
	private PizzaOrderStatus pizzaOrderStatus = PizzaOrderStatus.OPEN;

	@OneToOne(cascade = { CascadeType.ALL })
	private Order order;

	private int unbakedPizzas = 0;
	
	private String remark = "-";

	@OneToOne
	private Customer customer;

	public PizzaOrder() {
	}

	public PizzaOrder(UserAccount userAccount, Tan newTan, boolean pickUp, Customer customer) {
		this.order = new Order(userAccount);
		this.newTan = newTan;
		orderIdentifier = order.getId();
		this.customer = customer;
		this.pickUp = pickUp;
		// TODO Auto-generated constructor stub
	}

	public PizzaOrder(UserAccount userAccount, PaymentMethod paymentMethod, Tan newTan, boolean pickUp,
			Customer customer) {
		this.order = new Order(userAccount, paymentMethod);
		this.newTan = newTan;
		orderIdentifier = order.getId();
		this.customer = customer;
		this.pickUp = pickUp;
		// TODO Auto-generated constructor stub
	}

	public int markAsBaked() {
		System.out.println("Unbaked Pizzas: " + unbakedPizzas);
		Assert.assertTrue("No unbaked Pizza left!", unbakedPizzas >= 0);
		unbakedPizzas--;
		if (unbakedPizzas == 0) {
			this.readyOrder();
			System.out.println("ready");

		}

		return unbakedPizzas;
	}

	public int addAsUnbaked() {
		unbakedPizzas++;
		return unbakedPizzas;
	}

	public int getUnbakedPizzas() {
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

	private void setOrderStatus(PizzaOrderStatus status) {
		this.pizzaOrderStatus = status;
	}

	public PizzaOrderStatus getOrderStatus() {
		return this.pizzaOrderStatus;
	}

	public void completeOrder() // TODO: creaty accountancyentry
	{
		this.setOrderStatus(PizzaOrderStatus.COMPLETED);
	}

	public Tan getTan() {
		return this.newTan;
	}

	public void deliverOrder() {
		this.setOrderStatus(PizzaOrderStatus.DELIVERING);
	}

	public void readyOrder() {
		this.setOrderStatus(PizzaOrderStatus.READY);
	}
	
	public void cancelOrder() {
		this.setOrderStatus(PizzaOrderStatus.CANCELLED);
	}
	
	public void assignDeliverer(Deliverer d){
		this.setOrderStatus(PizzaOrderStatus.PENDING);
		this.deliverername = d.getUsername();
	}

	public boolean getPickUp() {
		return pickUp;
	}
	
	public String getDelivererName()
	{
		return this.deliverername;
	}

	public MonetaryAmount getTotalPrice() {
		MonetaryAmount tmpPrice = order.getTotalPrice();
		if (this.getPickUp()) {
			tmpPrice = tmpPrice.multiply(0.9);
		}
		return tmpPrice;
	}
	
	public Customer getCustomer(){
		return customer;
	}
	
	public void setCustomer(Customer newCustomer){
		customer = newCustomer;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
	
	public String getRemark(){
		return this.remark;
	}
	
	@Override
	public String toString(){
		
		Iterator<OrderLine> it = order.getOrderLines().iterator();
		
		String result = "";
		
		while(it.hasNext()){
			
			result = result + (" " + it.next().getProductName() + ";");
		}
		
		return result;
		
		
	}

}
