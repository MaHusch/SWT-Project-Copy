package kickstart.model;


import java.util.*;

public class MyTimerTask extends TimerTask {
	
	//simuliert den Backvorgang

	private Timer myTimer;
	private Pizza myPizza;
	
	public MyTimerTask(Timer timer, Pizza pizza){
		
		myTimer =  timer;
		myPizza = pizza;
	}
	
	@Override
	public void run() {
		//System.out.println("baking....");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		myPizza.setStatus(true);
		
		myTimer.cancel();
		
	}
	
	
	
}
