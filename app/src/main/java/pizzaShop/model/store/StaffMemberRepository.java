package pizzaShop.model.store;

import org.springframework.data.repository.CrudRepository;

import pizzaShop.model.actor.StaffMember;

public interface StaffMemberRepository extends CrudRepository<StaffMember, Long> {
	
}
