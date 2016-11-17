package kickstart.model.tan_management;

public class Tan {
	
	private String number;
	
	private TanStatus status;
	
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
