package pizzaShop.model.store;

import org.springframework.data.repository.CrudRepository;


public interface PizzaOrderRepository extends CrudRepository<PizzaOrder, Long> {
	
}
