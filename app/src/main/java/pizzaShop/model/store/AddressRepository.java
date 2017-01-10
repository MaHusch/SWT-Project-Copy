package pizzaShop.model.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pizzaShop.model.actor.Address;


public interface AddressRepository extends CrudRepository<Address, Long> {
	
}