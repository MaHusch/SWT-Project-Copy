package pizzaShop.controller;


	/**
	 * Class for MVC-Pattern: Error-Display on html  
	 * @author Martin Huschenbett
	 */

public class ErrorClass {
	
	private boolean error;
	private String message;
	
	/**
	 * Constructor
	 * @param error whether or not the error is present
	 */
	
	public ErrorClass(boolean error){
		
		this.error = error;
	}
	
	
	/**
	 * setter for error
	 * @param error
	 */
	
	public void setError(boolean error){
		this.error=error;
	}
	
	/**
	 * getter for error
	 * @return error
	 */
	
	public boolean getError(){
		
		return error;
	}

	
	/**
	 * getter for errormessage
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	
	/**
	 * setter for errormessage
	 * @param message Message of Error
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
