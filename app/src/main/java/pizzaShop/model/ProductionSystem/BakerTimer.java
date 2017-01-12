package pizzaShop.model.ProductionSystem;

import java.time.LocalDateTime;
import java.util.Timer;

import org.salespointframework.time.BusinessTime;

public class BakerTimer extends Timer {

	private int counter;
	private LocalDateTime endDate;
	
	//private BusinessTime date;

	public BakerTimer(BusinessTime date) {
		endDate = date.getTime().plusMinutes(5);
		counter = (endDate.getMinute() - date.getTime().getMinute()) * 60;
		System.out.println("BakerTimer Startzeit: " + date.getTime());
		System.out.println("BakerTimer Endzeit: " + endDate);
		System.out.println("Bakertimer Counter: " + counter);
	}
	
	public int getCounter(){
		return counter;
	}

	public void setCounter(int counter){
		this.counter = counter;
	}
	
	public LocalDateTime getEndDate(){
		return endDate;
	}
}
