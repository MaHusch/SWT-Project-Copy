package pizzaShop.model.ManagementSystem;

import java.util.concurrent.LinkedBlockingQueue;

import pizzaShop.model.OrderSystem.Pizza;

public class Pizzaqueue extends LinkedBlockingQueue<Pizza> {

	private static final long serialVersionUID = 1L;
	private static final Pizzaqueue Queue = new Pizzaqueue();

	private Pizzaqueue() {
	}

	public static Pizzaqueue getInstance() {
		return Queue;
	}

}
