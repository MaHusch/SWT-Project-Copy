package kickstart.model.store;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import kickstart.model.actor.Customer;


public interface CustomerRepository extends CrudRepository<Customer, Long> {
	
}
