package pizzaShop.model.tan_management;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
public class Tan {
	

	
	@Id private String number;
	
	private TanStatus status;
	
	public Tan(){}
	
	public Tan(String number, TanStatus status)
	{
		this.status = status;
		this.number = number;
		
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

}
