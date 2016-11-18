package kickstart.model.store;

import org.springframework.data.repository.CrudRepository;

import kickstart.model.actor.StaffMember;

public interface StaffMemberRepository extends CrudRepository<StaffMember, Long> {
	
}
