package pizzaShop.model.actor;  //TODO: Store should be tested before

import java.util.*;

import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.useraccount.Role;

public class Deliverer extends StaffMember {
	Boolean available;
	List<OrderIdentifier> orderToDeliver;
	
	public Deliverer(String surname, String forename, String telephonenumber){
		super(surname, forename,telephonenumber);
		this.setAvailable(false);
		this.orderToDeliver = new LinkedList<OrderIdentifier>();
	}
	
	private void setAvailable(Boolean a)
	{
		this.available = a;
	}
	
	public Boolean getAvailable()
	{
		return available;
	}
	
	public List<OrderIdentifier> getOrders()
	{
		return orderToDeliver;
	}
	
	public boolean addOrder(OrderIdentifier o)
	{
		if(this.orderToDeliver.contains(o)) return false;
		
		this.orderToDeliver.add(o);
		return true;
	}
	
	public OrderIdentifier removeOrder(OrderIdentifier o)
	{
		if(this.orderToDeliver.contains(o)) 
			{
			orderToDeliver.remove(o);
			return o;
			}
		
		return null;
	}
	
	public void clearOrders() //wenn er wieder zurückkommt
	{
		this.orderToDeliver.clear();
	}
	
	public void checkIn()
	{
		this.setAvailable(true);
	}
	
	public void checkOut()
	{
		this.setAvailable(false);
	}
}
