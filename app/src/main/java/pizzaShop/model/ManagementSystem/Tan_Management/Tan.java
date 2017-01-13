package pizzaShop.model.ManagementSystem.Tan_Management;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Tan {

	@Id
	@GeneratedValue
	private long TanID;

	private String number;

	private TanStatus status;

	public Tan() {
	}

	public Tan(String number, TanStatus status) {
		this.status = status;
		this.number = number;

	}

	public String getTanNumber() {
		return this.number;
	}

	public TanStatus getStatus() {

		return this.status;
	}

	public void setStatus(TanStatus newStatus) {

		this.status = newStatus;
	}

	// @Override
	// public SalespointIdentifier getId() {
	// TODO Auto-generated method stub
	// return this.ID;
	// }

}
