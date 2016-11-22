package pizzaShop.model.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pizzaShop.model.actor.Customer;


public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
}
