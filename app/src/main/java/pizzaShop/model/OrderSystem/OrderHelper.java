package pizzaShop.model.OrderSystem;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;

@Component
public class OrderHelper {

	private final PizzaOrderRepository pizzaOrderRepository;
	private final Store store;
	private final TanManagement tanManagement;

	@Autowired
	public OrderHelper(PizzaOrderRepository pizzaOrderRepository, Store store, TanManagement tanManagement) {
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.store = store;
		this.tanManagement = tanManagement;

	}

	public void pickUpOrder(PizzaOrder p) throws Exception {
		if (p.getOrderStatus().equals(PizzaOrderStatus.READY)) {
			Tan toOrderAsignedTan = p.getTan();
			// this might seem redundant but needs to be done because the Tan object
			// saved in the PizzaOrder gets changed by saving it in the Repository so 
			// we need to get the original Tan object.
			Tan foundTan = tanManagement.getNotConfirmedTanByTanNumber(toOrderAsignedTan.getTanNumber());			
			tanManagement.confirmTan(foundTan);
			store.completeOrder(p, "mitgenommen", null);
		} else {
			throw new IllegalArgumentException("Order noch nicht bereit zur Mitnahme!");
		}
	}

	public void assignDeliverer(String username, OrderIdentifier orderID) throws Exception {
		if (username == null || username.equals("")) {
			throw new IllegalArgumentException("Kein Lieferanten ausgewählt!");
		}
		Deliverer deliverer = (Deliverer) store.getStaffMemberByUsername(username);
		deliverer.addOrder(orderID);
		PizzaOrder p = pizzaOrderRepository.findOne(orderID);
		p.assignDeliverer(deliverer);
		pizzaOrderRepository.save(p);

	}

	public void unassignDeliverer(OrderIdentifier orderID) {
		PizzaOrder p = pizzaOrderRepository.findOne(orderID);
		Deliverer deliverer = (Deliverer) store.getStaffMemberByUsername(p.getDelivererName());
		deliverer.removeOrder(orderID);
		p.readyOrder();
		pizzaOrderRepository.save(p);

	}
}