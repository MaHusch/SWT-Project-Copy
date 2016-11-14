package kickstart.model;


import java.util.*;

public class Oven {

	private int id;
	private ArrayList<Baker> Observers = new ArrayList<Baker>();
	private Pizza currentPizza = null;
	private Timer myTimer;
	private boolean empty = true;
	
	//Konstruktor
	public Oven(int id){
		this.id = id;
	}
	
	
	public void resetTime(){
		myTimer.cancel();
	}
	
	
	// Überprüfen ob Ofen leer ist
	public boolean isEmpty(){
		if(empty){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getId(){
		return id;
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
		
		myTimer = new Timer();
		myTimer.schedule(new MyTimerTask(myTimer, pizza), 0);
		
		/*
		
		System.out.println(currentPizza.toString());
		try {
			Thread.sleep(6000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		//System.out.println(currentPizza.toString());
		notifyObservers();
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
	
	
	
}



