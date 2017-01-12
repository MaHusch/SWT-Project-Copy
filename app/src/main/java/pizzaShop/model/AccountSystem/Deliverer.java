package pizzaShop.model.AccountSystem; //TODO: Store should be tested before

import static org.salespointframework.core.Currencies.EURO;

import java.util.LinkedList;
import java.util.List;

import org.javamoney.moneta.Money;
import org.salespointframework.order.OrderIdentifier;

/**
 * DelivererClass for representing a Deliverer
 * 
 * @author Martin Huschenbett
 *
 */

public class Deliverer extends StaffMember {

	/**
	 * whether or not the Deliverer is available
	 */
	Boolean available;

	/**
	 * List of Orders that the Deliverer has to deliver
	 */
	List<OrderIdentifier> orderToDeliver;

	/**
	 * Cunstructor
	 * 
	 * @param surname
	 * @param forename
	 * @param telephonenumber
	 */
	public Deliverer(String surname, String forename, String telephonenumber) {
		super(surname, forename, telephonenumber);
		salary = Money.of(200, EURO);
		this.setAvailable(false);
		this.orderToDeliver = new LinkedList<OrderIdentifier>();
		
	}

	/**
	 * setter for available
	 * 
	 * @param available
	 */
	private void setAvailable(Boolean available) {
		this.available = available;
	}

	/**
	 * getter for available
	 * 
	 * @return available
	 */
	public Boolean getAvailable() {
		return available;
	}

	/**
	 * getter for List of Orders which should be delivered
	 * 
	 * @return orderToDeliver
	 */

	public List<OrderIdentifier> getOrders() {
		return orderToDeliver;
	}

	/**
	 * add an Order to the OrderList
	 * 
	 * @param orderIdentifier
	 * @return false if OrderList already contains the given Order
	 */

	public boolean addOrder(OrderIdentifier o) {
		if (this.orderToDeliver.contains(o))
			return false;

		this.orderToDeliver.add(o);
		return true;
	}

	/**
	 * removes an Order from the OrderList
	 * 
	 * @param orderIdentifier
	 * @return null if the given Order was not in the OrderList
	 */
	public OrderIdentifier removeOrder(OrderIdentifier o) {
		if (this.orderToDeliver.contains(o)) {
			orderToDeliver.remove(o);
			return o;
		}

		return null;
	}

	/**
	 * clears the List of Orders
	 */
	public void clearOrders() {
		this.orderToDeliver.clear();
	}

	/**
	 * sets available = true
	 */
	public void checkIn() {
		this.setAvailable(true);
	}
	
	/**
	 * sets available = false
	 */

	public void checkOut() {
		this.setAvailable(false);
	}
}
