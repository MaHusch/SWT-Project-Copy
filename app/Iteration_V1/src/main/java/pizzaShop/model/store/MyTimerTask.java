package pizzaShop.model.store;


import java.util.*;

import pizzaShop.model.catalog_item.Pizza;

public class MyTimerTask extends TimerTask {
	
	//simuliert den Backvorgang

	private Timer myTimer;
	private Pizza myPizza;
	private Oven myOven;
	
	public MyTimerTask(Timer timer, Pizza pizza, Oven oven){
		
		myTimer =  timer;
		myPizza = pizza;
		myOven = oven;
	}
	
	@Override
	public void run() {
		//System.out.println("baking....");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//myPizza.setStatus(true);
		
		myTimer.cancel();
		myPizza.setStatus(true);
		myOven.notifyObservers();
		
		
	}
	
	
	
}
