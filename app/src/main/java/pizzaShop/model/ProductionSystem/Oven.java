package pizzaShop.model.ProductionSystem;


import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.OrderSystem.Pizza;

public class Oven {

	private static int ID = 1;
	private int ovenID;
	private Pizza currentPizza = null;
	private BakerTimer myTimer = null;
	private boolean empty = true;

	private final Store store;

	@Autowired
	public Oven(Store store) {
		this.store = store;
		this.ovenID = this.ID++;

	}

	public int getId() {
		return this.ovenID;
	}

	public void resetTime() {
		myTimer.cancel();
	}

	public boolean isEmpty() {
		return empty;
	}

	public boolean notifyObservers(Pizza pizza) {
		if (pizza != null) {
			store.updatePizzaOrder(pizza);
			return true;
		} else {
			System.out.println("pizza ist null");
			return false;
		}
	}

	public boolean fill(Pizza pizza, BusinessTime businessTime) {
		if (this.isEmpty()) {
			currentPizza = pizza;
			empty = false;
			myTimer = new BakerTimer(businessTime);
			myTimer.scheduleAtFixedRate(new BakerTask(myTimer, pizza, this, businessTime), 0, 1000);
			return true;
		} else {
			return false;
		}
	}

	public void clear() {
		currentPizza = null;
		empty = true;
	}

	public Pizza getPizza() {
		return currentPizza;	
	}

	public String getPizzaName() {
		if (!this.isEmpty()) {
			return currentPizza.getName();
		} else {
			return null;
		}
	}
	
	public BakerTimer getBakerTimer(){
		return myTimer;
	}

}
