package kickstart.model;


public class Pizza {

	private String name;
	private String status;
	private boolean isFinished = false;
	
	public Pizza(String name){
		this.name=name;
	}
	
	public String getName(){
		return name;
	}
	public void setStatus(boolean status){
		
		isFinished = status;
		
	}
	private void booleanString(){
		if(isFinished){
			status = "finished";
		}
		else {
			status = "not finished";
		}
	}
	public String toString(){
		booleanString();
		
		return ("Pizza " + name + " is: " + status); 
	}
	
}
