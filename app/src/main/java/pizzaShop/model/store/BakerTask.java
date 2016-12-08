package pizzaShop.model.store;

import java.util.*;

import pizzaShop.model.catalog_item.Pizza;

public class BakerTask extends TimerTask {

	// simuliert den Backvorgang

	private BakerTimer myTimer;
	private Pizza myPizza;
	private Oven myOven;

	public BakerTask(BakerTimer timer, Pizza pizza, Oven oven) {

		myTimer = timer;
		myPizza = pizza;
		myOven = oven;
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
