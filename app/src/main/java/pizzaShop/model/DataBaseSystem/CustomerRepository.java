package pizzaShop.model.DataBaseSystem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pizzaShop.model.AccountSystem.Customer;


public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
}
