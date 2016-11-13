package kickstart.model;

import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<StaffMember, Long> {
	
}
