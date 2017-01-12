package pizzaShop.model.OrderSystem;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;

@Component
public class OrderHelper {

	private final PizzaOrderRepository pizzaOrderRepository;
	private final Store store;

	@Autowired
	public OrderHelper(PizzaOrderRepository pizzaOrderRepository, Store store) {
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.store = store;

	}

	public void pickUpOrder(PizzaOrder p) throws Exception {
		if (p.getOrderStatus().equals(PizzaOrderStatus.READY)) {
			store.completeOrder(p, "mitgenommen", null);
		} else {
			throw new IllegalArgumentException("Order noch nicht bereit zur Mitnahme!");
		}
	}

	public void assignDeliverer(String username, OrderIdentifier orderID) throws Exception {
		if (username == null || username.equals("")) {
			throw new IllegalArgumentException("Kein Lieferanten ausgewählt!");
		}
		Deliverer deliverer = (Deliverer) store.getStaffMemberByName(username);
		deliverer.addOrder(orderID);
		PizzaOrder p = pizzaOrderRepository.findOne(orderID);
		p.assignDeliverer(deliverer);
		pizzaOrderRepository.save(p);

	}

	public void unassignDeliverer(OrderIdentifier orderID) {
		PizzaOrder p = pizzaOrderRepository.findOne(orderID);
		Deliverer deliverer = (Deliverer) store.getStaffMemberByName(p.getDelivererName());
		deliverer.removeOrder(orderID);
		p.readyOrder();
		pizzaOrderRepository.save(p);

	}
}