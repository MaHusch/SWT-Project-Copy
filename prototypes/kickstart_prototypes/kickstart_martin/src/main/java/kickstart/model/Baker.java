package kickstart.model;


import java.util.*;

public class Baker {
	
	private ArrayList<Oven> myOvens = new ArrayList<Oven>();
	private Pizzaqueue Queue = Pizzaqueue.getInstance();
	private String name;
	private Pizza nextPizza;
	
	public Baker(String name){
		this.name = name;
	}
	
	public boolean addOven(Oven oven){
		myOvens.add(oven);
		oven.registerObserver(this);
		System.out.println("added oven");
		return true;
	}
	
	public boolean removeOven(Oven oven){
		myOvens.remove(oven);
		oven.unregisterObserver(this);
		return true;
	}
	
	public void getNextPizza(){
		nextPizza = Queue.pollFirst();
	}
	
	public boolean putPizzaIntoOven(Oven oven){
		
		for(int i = 0; i < myOvens.size(); i++){
			if(myOvens.get(i).getId() == oven.getId() && myOvens.get(i).isEmpty()){					
				myOvens.get(i).fill(nextPizza);
				
				return true;
			}
		}
		return false;
	}
	
	public void update(Oven observable, boolean isFinished){
		for(int i = 0; i < myOvens.size(); i++){
			if(myOvens.get(i).getId() == observable.getId()){
				myOvens.get(i).clear();
			}
		}
	}
	
	
	public String getName(){
		return name;
	}
	

}
