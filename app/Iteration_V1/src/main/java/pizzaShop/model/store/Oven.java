package pizzaShop.model.store;


import java.util.*;

import pizzaShop.model.actor.Baker;
import pizzaShop.model.catalog_item.Pizza;

public class Oven {

	private static int ID = 1;
	private int ovenID;
	private ArrayList<Baker> Observers = new ArrayList<Baker>();
	private Pizza currentPizza = null;
	private Timer myTimer;
	private boolean empty = true;
	
	
	public Oven(Store store){
		this.ovenID = this.ID++;	
		store.getOvens().add(this);
		
	}
	
	@SuppressWarnings("unused")
	public Oven(){}		//braucht man anscheinend für Thymeleaf, aber warum?!
	
	
	public int getId(){
		return this.ovenID;
	}
	
	public void resetTime(){
		myTimer.cancel();
	}
	
	public boolean isEmpty(){
		return empty;
	}
		
	public boolean notifyObservers(){
		//System.out.println("informing bakers");
		for(Baker baker : Observers){
			baker.update(this, isEmpty());
		}
		return true;
	}
	
	public boolean registerObserver(Baker Observer){
		Observers.add(Observer);
		return true;
	}
	
	public boolean unregisterObserver(Baker Observer) {
		Observers.remove(Observer);
		return true;
	}
	
	public boolean fill(Pizza pizza){
		currentPizza = pizza;
		empty = false;
		
		notifyObservers();
		
		myTimer = new Timer();
		myTimer.schedule(new MyTimerTask(myTimer, pizza, this), 0);

		//notifyObservers();		ist im TimerTask drin, da nach ablauf des Timers erst der Bäker informiert werden soll
		
		return true;
		 
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
	
	public ArrayList<Baker> getObservers(){
		return Observers;
	}
	
	
	
}



