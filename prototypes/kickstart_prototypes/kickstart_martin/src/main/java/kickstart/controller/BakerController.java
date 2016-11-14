package kickstart.controller;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kickstart.model.*;

@Controller
public class BakerController {
	
	private Baker b1;
	private Oven o1;
	private Oven o2;
	private Pizza p1;
	private Pizza p2;
	private Pizza p3;
	private Pizza p4;
	private ListIterator<Pizza> it;
	private ArrayList<Pizza> list;	
	
	public BakerController(){
		
		this.b1 = new Baker("Fred");
		this.o1 = new Oven(1);
		this.o2 = new Oven(2);
		this.p1 = new Pizza("Salami");
		this.p2 = new Pizza("Funghi");
		this.p3 = new Pizza("Magaritta");
		this.p4 = new Pizza("Hawaii");
		
		
		Pizzaqueue.getInstance().add(p1);
		Pizzaqueue.getInstance().add(p2);
		Pizzaqueue.getInstance().add(p3);
		Pizzaqueue.getInstance().add(p4);
		
		it = Pizzaqueue.getInstance().listIterator();
		list = new ArrayList<Pizza>();
		while(it.hasNext()){
			list.add(it.next());
		}
		/*
		 * Ich mache das, weil aus irgendeinem Grund immer nur das erste Objekt der Pizzaqueue.getInstance()
		 * angezeigt wird auf der Website, nicht der gesamte Inhalt.
		 * Daher erzeuge ich eine ArrayList und fülle sie mit dem Inhalt der Pizzaqueue und übergebe diese dem
		 * Model.
		 * 
		 */
		
		b1.addOven(o1);
		b1.addOven(o2); 
		
		o1.fill(p1);
		o2.fill(p2);		
		
	}
	
	@RequestMapping("/baker")
	public String foo(Model model){
		
		model.addAttribute("baker", b1.getSelf());
		model.addAttribute("queue", list);
		
		return "baker";
	}
	

}
