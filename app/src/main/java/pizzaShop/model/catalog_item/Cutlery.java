package pizzaShop.model.catalog_item;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.time.BusinessTime;

@Entity
public class Cutlery extends Item {

	private LocalDateTime returnTill;
	public Integer loanPeriod;
	
	
	@SuppressWarnings("unused")
	public Cutlery() {}

	public Cutlery(String name, MonetaryAmount price, LocalDateTime time) {
		super(name, price, ItemType.CUTLERY);
		System.out.println("aktuelle Zeit" + time.toString()); 
		this.returnTill = time;
		this.loanPeriod = 28; // according to task --> 4 weeks 
		
	}
	
	public LocalDateTime getDate()
	{
		return returnTill;
	}
	// item --> customer?
	
	/**
	 * 
	 * @param time time when cutlery lent --> return till will be calculated
	 */
	public void setDate(LocalDateTime time)
	{
		this.returnTill = time.plusDays(loanPeriod);
	}

}
