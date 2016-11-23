package pizzaShop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DelivererController {

	public void DelivererController()
	{
		
	}
	
	@RequestMapping("/checkOut")
	public String checkIn()
	{
		return "redirect:index";
	}
	
	@RequestMapping("/checkIn")
	public String checkOut()
	{
		return "redirect:index";
	}
}
