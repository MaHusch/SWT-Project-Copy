package kickstart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import kickstart.model.Baker;

@Controller
public class BakerController {
	
	//Baker b1 = new Baker("Fred");
	
	@RequestMapping("/baker")
	public String foo(){
		return "baker";
	}
	

}
