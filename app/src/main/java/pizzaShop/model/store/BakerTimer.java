package pizzaShop.model.store;

import java.util.Timer;

public class BakerTimer extends Timer {
	
	private int counter;
	
	public BakerTimer(){
		counter = 10;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

}
