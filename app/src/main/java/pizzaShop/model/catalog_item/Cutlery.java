package pizzaShop.model.catalog_item;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.time.BusinessTime;

@Entity
public class Cutlery extends Item {

	private LocalDateTime returnTill;
	
	@SuppressWarnings("unused")
	public Cutlery() {}

	public Cutlery(String name, MonetaryAmount price, LocalDateTime time) {
		super(name, price, ItemType.CUTLERY);
		System.out.println("aktuelle Zeit" + time.toString()); // no businesstime?
		this.returnTill = time;
		// TODO Auto-generated constructor stub
	}
	
	public LocalDateTime getDate()
	{
		return returnTill;
	}

}
