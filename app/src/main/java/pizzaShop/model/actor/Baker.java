package pizzaShop.model.actor;

import java.util.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.salespointframework.useraccount.Role;

import pizzaShop.model.catalog_item.Pizza;
import pizzaShop.model.store.Oven;
import pizzaShop.model.store.Pizzaqueue;
import pizzaShop.model.store.Store;

@Entity
public class Baker extends StaffMember {

	private ArrayList<Oven> myOvens;
	private Pizzaqueue Queue = Store.getInstance().getPizzaQueue();
	private Pizza nextPizza;

	@Id
	@GeneratedValue
	private long employeeID;

	public Baker(String surname, String forename, String telephoneNumber, String username, String password) {
		super(surname, forename, telephoneNumber);
		this.updateUserAccount(username, password, Role.of("ROLE_BAKER"));
		myOvens = new ArrayList<Oven>();
	}

	public Baker(String surname, String forename, String telephoneNumber) {
		super(surname, forename, telephoneNumber);
		myOvens = new ArrayList<Oven>();
	}

	public void getNextPizza() throws Exception {
		
		if (!Queue.isEmpty()) {
			nextPizza = Queue.poll();
		} else {
			throw new NullPointerException("There is no Pizza in the PizzaQueue!");
		}
	}

	public boolean putPizzaIntoOven(Oven oven) {

		myOvens = Store.getInstance().getOvens();

		for (int i = 0; i < myOvens.size(); i++) {
			if (myOvens.get(i).getId() == oven.getId() && myOvens.get(i).isEmpty()) {
				myOvens.get(i).fill(nextPizza);
				System.out.println(myOvens.get(i).getPizza());

				return true;
			}
		}
		return false;
	}

}
