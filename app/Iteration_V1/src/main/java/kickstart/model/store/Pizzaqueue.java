package kickstart.model.store;


import java.util.*;

import kickstart.model.catalog_item.Pizza;

public class Pizzaqueue extends LinkedList<Pizza>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Pizzaqueue Queue = new Pizzaqueue();
	
	private Pizzaqueue(){}
	
	public static Pizzaqueue getInstance(){
		return Queue;
	}
	
}
