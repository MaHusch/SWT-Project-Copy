package pizzaShop.model.DataBaseSystem;

import org.springframework.data.repository.CrudRepository;

import pizzaShop.model.AccountSystem.Address;


public interface AddressRepository extends CrudRepository<Address, Long> {
	
}