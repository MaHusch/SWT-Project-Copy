package pizzaShop.model.store;


import java.util.*;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.catalog_item.Pizza;

public class Oven {

	private static int ID = 1;
	private int ovenID;
	private Pizza currentPizza = null;
	private Timer myTimer;
	private boolean empty = true;
	
	
	public Oven(Store store){
		this.ovenID = this.ID++;	
		store.getOvens().add(this);
		
	}
	
	@SuppressWarnings("unused")
	public Oven(){}		
	
	
	public int getId(){
		return this.ovenID;
	}
	
	public void resetTime(){
		myTimer.cancel();
	}
	
	public boolean isEmpty(){
		return empty;
	}
		
	public boolean notifyObservers(Pizza pizza){
		if(!pizza.equals(null)){
			Store.getInstance().updatePizzaOrder(pizza);
			return true;
		}
		else{
			System.out.println("pizza ist null");
			return false;
		}
	}
		
	public boolean fill(Pizza pizza){
		if(!pizza.equals(null)){
			currentPizza = pizza;
			empty = false;
			myTimer = new Timer();
			myTimer.schedule(new MyTimerTask(myTimer, pizza, this), 0);		
			return true;
		}
		else {
			return false;
		}
	}
	
	public void clear(){
		currentPizza = null;
		empty = true;
	}
	
	public Pizza getPizza(){
		try{
			if(!currentPizza.equals(null)){
				return currentPizza;
			}
			else{
				return null;
			}
		}
		catch (Exception e){
			return null;
		}
	}
	
	public String getPizzaName(){
		if(!this.isEmpty()){
			return currentPizza.getName();
		}
		else{ 
			return null;
		}
	}
	
}



