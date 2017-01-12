package pizzaShop.model.OrderSystem;

import java.time.LocalDateTime;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Class for representing Cutlery
 * 
 * @author Floretin Dörre
 *
 */

@Entity
public class Cutlery{


	@Id @GeneratedValue private long CutleryID;
	
	/**
	 * Date till Cutlery has to be returned
	 */
	private LocalDateTime returnTill;
	public Integer loanPeriod;
	private MonetaryAmount price;

	/**
	 * unused Constructor for Spring
	 */
	@Deprecated
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
		//super("Essgarnitur",price,ItemType.CUTLERY);
		this.setPrice(price);
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
		//super("Essgarnitur",price,ItemType.CUTLERY);
		this.setPrice(price);
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

	public MonetaryAmount getPrice() {
		return price;
	}

	public void setPrice(MonetaryAmount price) {
		this.price = price;
	}

}
