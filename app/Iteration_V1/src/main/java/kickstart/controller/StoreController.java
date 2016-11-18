package kickstart.controller;


import java.util.Optional;

import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import kickstart.model.store.Store;


@Controller
public class StoreController {
	
	//@Autowired 
	public StoreController() {
	}	
	
	
	@RequestMapping({"/", "/index"})
	public String index() {
		return "index";
	}
	

}