package kickstart.model.store;

import org.springframework.data.repository.CrudRepository;

import kickstart.model.actor.StaffMember;

public interface EmployeeRepository extends CrudRepository<StaffMember, Long> {
	
}
