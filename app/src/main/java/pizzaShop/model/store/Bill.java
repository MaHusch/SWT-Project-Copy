package pizzaShop.model.store;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.model.actor.Customer;

public class Bill {
	

	private String path = "ressources/Bills/";
	
	private BusinessTime businessTime;
	private int id = 1;
	
	public Bill(){}
	
	@Autowired
	public Bill(Customer customer, PizzaOrder pizzaOrder){
		
		FileWriter writer;
		File bill = new File("bills/das.txt");
		
		try {
			writer = new FileWriter(bill, true);
			writer.write("Rechnung vom: xxxx");
			writer.write(System.getProperty("line.separator"));
			writer.write("Kunde: " + customer.getSurname() + ", " + customer.getForename());
			writer.write(System.getProperty("line.separator"));
			writer.write("Addresse: " + customer.getAddress().toString());
			writer.write(System.getProperty("line.separator"));
			writer.write("Bestellung: " + pizzaOrder.toString());

			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
