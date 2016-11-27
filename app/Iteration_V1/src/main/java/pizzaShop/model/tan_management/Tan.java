package pizzaShop.model.tan_management;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.core.AbstractEntity;
import org.salespointframework.core.SalespointIdentifier;


@Entity
public class Tan extends AbstractEntity<SalespointIdentifier>{
	
	@Id private SalespointIdentifier ID;
	
	private String number;
	
	private TanStatus status;
	
	public Tan(){}
	
	public Tan(String number, TanStatus status)
	{
		this.status = status;
		this.number = number;
		
		ID = new SalespointIdentifier(number);
		
		//System.out.println(this.number);
		
	}
	
	public String getTanNumber()
	{
		return this.number;
	}
	
	public TanStatus getStatus()
	{
		
		return this.status;
	}
	
	public void setStatus(TanStatus newStatus)
	{
		
		this.status = newStatus;
	}

	@Override
	public SalespointIdentifier getId() {
		// TODO Auto-generated method stub
		return this.ID;
	}

}
