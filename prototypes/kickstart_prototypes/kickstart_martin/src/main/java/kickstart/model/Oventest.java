package kickstart.model;


public class Oventest {

	public static void main(String[] args) {
		
		Pizza p1 = new Pizza("p1");
		Pizza p2 = new Pizza("p2");
		Pizza p3 = new Pizza("p3");
		
		Pizzaqueue.getInstance().add(p1);
		Pizzaqueue.getInstance().add(p2);
		Pizzaqueue.getInstance().add(p3);
		
		Baker b1 = new Baker("Manfred");
		
		Oven o1 = new Oven(1);
		Oven o2 = new Oven(2);
		
		b1.addOven(o1);
		b1.addOven(o2);
		
		
		b1.getNextPizza();
		b1.putPizzaIntoOven(o1);
		
		
	}
}