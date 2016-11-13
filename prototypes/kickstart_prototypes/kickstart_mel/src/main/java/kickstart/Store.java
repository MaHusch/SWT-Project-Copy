package kickstart;

import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Component;

import kickstart.model.Admin;

@Component
public class Store {
	
	private static Store store = null;  
	
	public static UserAccountManager employeeAccountManager;
	public static Admin admin;
	
	
	public Store(){
	}
	
	@Autowired
	public Store(UserAccountManager employeeAccountManager){
		this.employeeAccountManager = employeeAccountManager;
		
		this.admin = new Admin("Mustermann","Max","123456789");
		
		store = this;
	}
	
	// Store is a singleton 
	public static Store getInstance(){
		/*
		if (store == null) {
			store = new Store();
		}*/
		
		return store;
	}
}
