package pizzaShop.model.store;

import java.util.ArrayList;

import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.*;
import org.springframework.stereotype.Component;

import pizzaShop.model.actor.Admin;
import pizzaShop.model.actor.StaffMember;

@Component
public class Store {
	
	private static Store store = null;  
	
	public static UserAccountManager employeeAccountManager;
	public static ItemCatalog itemCatalog;
	
	public static ArrayList<StaffMember> staffMemberList;
	public static Admin admin;
	
	private ArrayList<Oven> ovenList;
	
	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();
	
	
	public Store(){
	}
	
	@Autowired
	public Store(UserAccountManager employeeAccountManager,ItemCatalog itemCatalog){
		
		this.employeeAccountManager = employeeAccountManager;
		this.staffMemberList = new ArrayList<StaffMember>();
		this.itemCatalog = itemCatalog;
		this.ovenList = new ArrayList<Oven>();		
		this.admin = new Admin("Mustermann","Max","123456789");
		this.admin.updateUserAccount("admin", "123", Role.of("ROLE_ADMIN"));
		
		Oven oven1 = new Oven(this); 
		Oven oven2 = new Oven(this); 
		Oven oven3 = new Oven(this); 
		Oven oven4 = new Oven(this);
		System.out.println(oven3.getId());
		
		this.store = this;
	}
	
	// Store is a singleton 
	public static Store getInstance(){
		/*
		if (store == null) {
			store = new Store();
		}*/
		
		return store;
	}
	
	public Pizzaqueue getPizzaQueue(){
		
		return pizzaQueue;
	}
	
	public ArrayList<Oven> getOvens(){
		
		return ovenList;
	}
	
	public StaffMember getStaffMemberByName(String name){
		
		for(StaffMember staffMember : staffMemberList)
		{
			if(staffMember.getUsername().equals(name)) return staffMember;
		}
		
		return null;
	}
}
