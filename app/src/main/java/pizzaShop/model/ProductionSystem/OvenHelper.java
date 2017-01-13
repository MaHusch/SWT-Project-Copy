package pizzaShop.model.ProductionSystem;

import static org.salespointframework.core.Currencies.EURO;

import java.time.Duration;
import java.util.ArrayList;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pizzaShop.model.ManagementSystem.Store;
import pizzaShop.model.OrderSystem.Pizza;

@Component
public class OvenHelper {

	private final Store store;
	private final BusinessTime businessTime;
	private final Accountancy accountancy;
	private final ArrayList<Oven> ovenList;

	@Autowired
	public OvenHelper(Store store, BusinessTime businessTime, Accountancy accountancy) {
		this.store = store;
		this.businessTime = businessTime;
		this.accountancy = accountancy;
		ovenList = this.store.getOvens();
	}

	public void deleteOven(int id) throws IllegalArgumentException {
		Oven o = store.findOvenById(id);
		if (o == null)
			throw new IllegalArgumentException("Ofen ist nicht vorhanden!");

		if (!o.isEmpty())
			throw new IllegalArgumentException("Ofen ist nicht leer!");

		ovenList.remove(store.findOvenById(id));

	}

	public void addOven() {
		ovenList.add(new Oven(store));
		accountancy.add(new AccountancyEntry(Money.of(-1000, EURO), "Neuer Ofen gekauft"));
	}

	public void putPizzaIntoOven(int id) throws Exception {
		Oven o = store.findOvenById(id);
		if (o == null) {
			throw new IllegalArgumentException("Ofen existiert nicht!");
		}

		if (o.isEmpty()) {
			Pizza nextPizza = store.getNextPizza();
			o.fill(nextPizza, businessTime);
		}else{
			throw new IllegalArgumentException("Ofen nicht leer!");
		}

	}

	public void forward(Integer minutes) throws Exception {
		if (minutes == null)
			throw new IllegalArgumentException("Feld darf nicht leer sein!");

		businessTime.forward(Duration.ofMinutes(minutes));
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
