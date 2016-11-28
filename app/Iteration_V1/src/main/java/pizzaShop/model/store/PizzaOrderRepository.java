package pizzaShop.model.store;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.data.repository.CrudRepository;


public interface PizzaOrderRepository extends CrudRepository<PizzaOrder, OrderIdentifier> {
	
}
