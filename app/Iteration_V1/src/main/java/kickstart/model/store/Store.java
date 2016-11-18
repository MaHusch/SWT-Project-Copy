package kickstart.model.store;

import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Component;

import kickstart.model.actor.Admin;

@Component
public class Store {
	
	private static Store store = null;  
	
	public static UserAccountManager employeeAccountManager;
	public static ItemCatalog itemCatalog;
	
	public static Admin admin;
	
	
	public Store(){
	}
	
	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog){
		
		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
				
		this.admin = new Admin("Mustermann","Max","123456789");
		this.admin.updateUserAccount("admin", "123", Role.of("ROLE_ADMIN"));
		
		
		
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
