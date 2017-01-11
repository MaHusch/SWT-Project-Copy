package pizzaShop.model.DataBaseSystem;

import org.springframework.data.repository.CrudRepository;

import pizzaShop.model.AccountSystem.StaffMember;

public interface StaffMemberRepository extends CrudRepository<StaffMember, Long> {
	
}
