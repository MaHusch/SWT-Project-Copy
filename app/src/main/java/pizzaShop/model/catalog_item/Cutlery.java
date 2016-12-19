package pizzaShop.model.catalog_item;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import org.salespointframework.time.BusinessTime;

/**
 * Class for representing Cutlery
 * 
 * @author Floretin Dörre
 *
 */

@Entity
public class Cutlery extends Item{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3835581467490627776L;
	/**
	 * 
	 */
	
	/**
	 * Date till Cutlery has to be returned
	 */
	private LocalDateTime returnTill;
	public Integer loanPeriod;

	/**
	 * unused Constructor for Spring
	 */
	@SuppressWarnings("unused")
	public Cutlery() {
	}

	/**
	 * Cunstructor
	 * 
	 * @param name
	 * @param price
	 * @param time
	 */

	public Cutlery(MonetaryAmount price, LocalDateTime time) {
		super("Essgarnitur",price,ItemType.CUTLERY);
		this.loanPeriod = 28; // according to task --> 4 weeks
		this.setDate(time);
		
	}

	/**
	 * Constructor for expanding the loanPeriod
	 * 
	 * @param name
	 * @param price
	 * @param time
	 * @param loanPeriod
	 */

	public Cutlery(MonetaryAmount price, LocalDateTime time, Integer loanPeriod) {
		super("Essgarnitur",price,ItemType.CUTLERY);
		this.loanPeriod = loanPeriod;
		this.setDate(time);
	}

	/**
	 * getter for returnTill
	 * 
	 * @return returnTill
	 */

	public LocalDateTime getDate() {
		return returnTill;
	}
	// item --> customer?

	/**
	 * setter for returnTill
	 * 
	 * @param time
	 *            time when cutlery lent --> return till will be calculated
	 */
	public void setDate(LocalDateTime time) {
		this.returnTill = time.plusDays(loanPeriod);
	}
	
	/**
	 * 
	 * @return returns the just the Date
	 */
	public String getDateString()
	{
		LocalDateTime t1 = this.getDate();
		return t1.getDayOfMonth() + "." + t1.getMonthValue() + "." + t1.getYear();
	}

}
