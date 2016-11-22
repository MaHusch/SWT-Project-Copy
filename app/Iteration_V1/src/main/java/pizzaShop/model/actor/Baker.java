package pizzaShop.model.actor;


import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;

import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.Oven;
import pizzaShop.model.store.Pizzaqueue;

@Entity
public class Baker extends StaffMember {
	
	private ArrayList<Oven> myOvens = new ArrayList<Oven>();
	private Pizzaqueue Queue = Pizzaqueue.getInstance();
	private Pizza nextPizza;
	
	
@Id @GeneratedValue private long employeeID;
	
	public Baker(String surname, String forename,String telephoneNumber, String username, String password, Role role){
		super(surname,forename,telephoneNumber);
		this.updateUserAccount(username, password, role);
	} 
	
	public Baker(String surname, String forename,String telephoneNumber){
		super(surname,forename,telephoneNumber);
	}
	
	public boolean addOven(Oven oven){
		myOvens.add(oven);
		oven.registerObserver(this);
		return true;
	}
	
	public boolean removeOven(Oven oven){
		myOvens.remove(oven);
		oven.unregisterObserver(this);
		return true;
	}
	
	public void getNextPizza(){
		nextPizza = Queue.poll();
	}
	
	public boolean putPizzaIntoOven(Oven oven){
		
		for(int i = 0; i < myOvens.size(); i++){
			if(myOvens.get(i).getId() == oven.getId() && myOvens.get(i).isEmpty()){					
				myOvens.get(i).fill(nextPizza);
				System.out.println(myOvens.get(i).getPizza());
				
				return true;
			}
		}
		return false;
	}
	
	public void update(Oven observable, boolean isFinished){
		for(int i = 0; i < myOvens.size(); i++){
			if(myOvens.get(i).getId() == observable.getId() && isFinished == true){
				myOvens.get(i).clear();
			}
		}
	}
	
	
	
	public ArrayList<Oven> getOvens(){
		return myOvens;
	}
	
	public Oven getOvenByID(int id){
		
		for(int i = 0; i < myOvens.size(); i++){
			if(myOvens.get(i).getId() == id){
				return myOvens.get(i);
			}
		}
		return null;
	}
	

}
