package pizzaShop.model.store;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

import org.salespointframework.time.BusinessTime;
import org.springframework.beans.factory.annotation.Autowired;

import pizzaShop.model.actor.Customer;

public class Bill {


	public Bill(Customer customer, PizzaOrder pizzaOrder, LocalDateTime ldt) {

		System.out.println("in bill class");

		FileWriter writer;
		File bill = new File("Bills/" + pizzaOrder.getId().getIdentifier() + ".txt");

		try {
			writer = new FileWriter(bill, true);
			
			writer.write("Pappa Pizza");
			writer.write(System.getProperty("line.separator"));
			writer.write("Nöthnitzer Straße 46");
			writer.write(System.getProperty("line.separator"));
			writer.write("01187 Dresden");
			writer.write(System.getProperty("line.separator"));
			writer.write(System.getProperty("line.separator"));
			writer.write("Rechnung vom: " + ldt.toString());
			writer.write(System.getProperty("line.separator"));
			writer.write("Kunde: " + customer.getPerson().getSurname() + ", " + customer.getPerson().getForename());
			writer.write(System.getProperty("line.separator"));
			writer.write("Addresse: " + customer.getPerson().getAddress().toString());
			writer.write(System.getProperty("line.separator"));
			writer.write("Bestellung: " + pizzaOrder.toString());
			writer.write(System.getProperty("line.separator"));
			writer.write("Neue TAN: " + pizzaOrder.getTan().getTanNumber());

			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
