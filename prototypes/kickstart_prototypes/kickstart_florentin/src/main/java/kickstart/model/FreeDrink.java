package kickstart.model;

import javax.persistence.Entity;

import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.*;

@Entity
public class FreeDrink extends Item {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4420429436951583727L;

	@SuppressWarnings("unused")
	private FreeDrink(){}
	
	public FreeDrink(String name) {
		super(name, Money.of(0.0, EURO));
		
		// TODO Auto-generated constructor stub
	}

	public String toString()
	{
		return "Freigetränk";
	}
}
