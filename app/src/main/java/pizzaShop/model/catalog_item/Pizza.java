package pizzaShop.model.catalog_item;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderIdentifier;


@Entity
public class Pizza extends Item {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4746830994439574139L;
	@ElementCollection private List<String> ingredients; 
	@ElementCollection private List<String> orderQueue = new ArrayList<String>();
	
	
	private boolean isFinished;
	
	@SuppressWarnings("unused")
	private Pizza(){}
	
	public Pizza(String name, javax.money.MonetaryAmount price)
	{
		super(name,price,ItemType.PIZZA);
		this.ingredients = new ArrayList<String>();
		//this.orderQueue = 
		this.setStatus(false);
	}
	
	public void setStatus(boolean status){
		
		isFinished = status;
		
	}
	
	public boolean addIngredient(Ingredient i) //change name ?
	{
		if(ingredients.contains(i.getName())) return false;
		
		ingredients.add(i.getName());
		this.setPrice(getPrice().add(i.getPrice()));
		return true;
	}
	
	public String removeIngredient(Ingredient i)
	{
		if(ingredients.contains(i.getName()))
		{
			ingredients.remove(i.getName());
			this.setPrice(getPrice().subtract(i.getPrice()));
			return i.getName();
		}
		
		return null;
	}
	
	public List<String> getIngredients()
	{
		return ingredients;
	}

	public boolean getStatus() {
		return isFinished;
	}

	public void addOrder(OrderIdentifier o){
		orderQueue.add(o.toString());
		
	}
	
	public String getFirstOrder(){
		return orderQueue.get(0);
	}


	public String removeFirstOrder() throws Exception{
		if(orderQueue.isEmpty()) throw new NullPointerException("Die Pizza hat keine Orders zugewiesen");
		return orderQueue.remove(0);
	}
	
	public String toString()  //TODO: nicerer String
	{
		String result = super.toString();
		java.util.Iterator<String> i =ingredients.iterator();
		
		if(i.hasNext()) 
		{	
			result += "(" + i.next();
			
			for(;i.hasNext();)
			{
				result += "," + i.next();
			}
		
			result += ")";
		}
		
		return result;
	}
}
