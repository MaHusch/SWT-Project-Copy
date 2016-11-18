package kickstart.controller;

import java.util.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import kickstart.model.catalog_item.Pizza;
import kickstart.model.store.*;

@Controller
public class BakerController {
	
	private ListIterator<Pizza> it;
	private ArrayList<Pizza> list;	
	
	public BakerController(){
		
	}
	
	@RequestMapping("/ovens")
	public String ovenView(Model model){

		return "ovens";
	}
	

}
