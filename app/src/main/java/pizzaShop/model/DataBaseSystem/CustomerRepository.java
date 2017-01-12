package pizzaShop.model.DataBaseSystem;

import org.springframework.data.repository.CrudRepository;

import pizzaShop.model.AccountSystem.Customer;


public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
}
