package pizzaShop.model.store;

public class ErrorClass {
	
	private boolean error;
	private boolean message;
	
	public ErrorClass(boolean error){
		
		this.error = error;
	}
	
	public void setError(boolean error){
		this.error=error;
	}
	
	public boolean getError(){
		
		return error;
	}

	public boolean getMessage() {
		return message;
	}

	public void setMessage(boolean message) {
		this.message = message;
	}

}
