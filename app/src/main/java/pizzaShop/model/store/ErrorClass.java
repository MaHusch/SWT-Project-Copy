package pizzaShop.model.store;

public class ErrorClass {
	
	private boolean error;
	
	public ErrorClass(boolean error){
		
		this.error = error;
	}
	
	public void setError(boolean error){
		this.error=error;
	}
	
	public boolean getError(){
		
		return error;
	}

}
