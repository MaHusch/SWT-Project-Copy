package pizzaShop.model.store;

import java.time.LocalDateTime;
import java.util.Timer;

import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;

public class BakerTimer extends Timer {

	private int counter;
	private LocalDateTime endDate;
	
	//private BusinessTime date;

	public BakerTimer(BusinessTime date) {
		endDate = date.getTime().plusMinutes(1);
		counter = (endDate.getMinute() - date.getTime().getMinute()) * 60;
		System.out.println(date.getTime());
		System.out.println(counter);
		System.out.println(endDate);
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
