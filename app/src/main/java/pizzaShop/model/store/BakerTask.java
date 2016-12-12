package pizzaShop.model.store;

import java.time.LocalDate;
import java.util.*;

import pizzaShop.model.catalog_item.Pizza;

public class BakerTask extends TimerTask {

	// simuliert den Backvorgang

	private BakerTimer myTimer;
	private Pizza myPizza;
	private Oven myOven;
	private LocalDate date;

	public BakerTask(BakerTimer timer, Pizza pizza, Oven oven, LocalDate date) {

		myTimer = timer;
		myPizza = pizza;
		myOven = oven;
		this.date = date;
	}

	@Override
	public void run() {
		// System.out.println("baking....");
		
		
		
		
		
		
		
		int i = myTimer.getCounter();
		System.out.println(i);
		i--;
		myTimer.setCounter(i);

		if (myTimer.getCounter() == 0) {

			myTimer.cancel();
			myPizza.setStatus(true);
			myOven.notifyObservers(myPizza);
			myOven.clear();
		}

	}

}
