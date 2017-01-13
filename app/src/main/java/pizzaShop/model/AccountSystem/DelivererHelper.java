package pizzaShop.model.AccountSystem;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.ManagementSystem.Tan_Management.Tan;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.PizzaOrder;

@Component
public class DelivererHelper {
	String username;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final TanManagement tanManagement;
	private final Store store;

	@Autowired
	public DelivererHelper(PizzaOrderRepository pizzaOrderRepository, TanManagement tanManagement, Store store) {
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.tanManagement = tanManagement;
		this.store = store;

	}

	public void checkOut(Deliverer d) {

		d.checkOut();
		for (OrderIdentifier oi : d.getOrders()) {

			PizzaOrder p = pizzaOrderRepository.findOne(oi);
			if (p != null) {
				System.out.println(p.getOrderStatus());
				p.deliverOrder();
				pizzaOrderRepository.save(p);
				System.out.println(p.getOrderStatus());
			}

		}

	}

	public void checkIn(Deliverer d) {
		d.checkIn();
		System.out.println(d.getAvailable());

		for (OrderIdentifier oi : d.getOrders()) {
			PizzaOrder p = pizzaOrderRepository.findOne(oi);
			
			if (p != null) {
				Tan toOrderAsignedTan = p.getTan();
				// this might seem redundant but needs to be done because the
				// Tan object
				// saved in the PizzaOrder gets changed by saving it in the
				// Repository so
				// we need to get the original Tan object.
				Tan foundTan = tanManagement.getNotConfirmedTanByTanNumber(toOrderAsignedTan.getTanNumber());

				tanManagement.confirmTan(foundTan);
				store.completeOrder(p, "ausgeliefert", d);
			}
		}
		d.clearOrders();
	}
}
