package kickstart.model;


import java.util.*;

public class MyTimerTask extends TimerTask {

	private Timer myTimer;
	private Pizza myPizza;
	
	public MyTimerTask(Timer timer, Pizza pizza){
		
		myTimer =  timer;
		myPizza = pizza;
	}
	
	@Override
	public void run() {
		System.out.println("baking....");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		myPizza.setStatus(true);
		
		returnPizza(myPizza);
		myTimer.cancel();
		
	}
	
	private Pizza returnPizza(Pizza pizza){
		return pizza;
	}

	
	
}
