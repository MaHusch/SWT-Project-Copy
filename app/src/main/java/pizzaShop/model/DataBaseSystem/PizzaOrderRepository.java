package pizzaShop.model.DataBaseSystem;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.data.repository.CrudRepository;

import pizzaShop.model.OrderSystem.PizzaOrder;


public interface PizzaOrderRepository extends CrudRepository<PizzaOrder, OrderIdentifier> {
	
}
