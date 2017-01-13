package pizzaShop.model.ManagementSystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.money.MonetaryAmount;

import org.javamoney.moneta.Money;
import org.salespointframework.accountancy.Accountancy;
import org.salespointframework.accountancy.AccountancyEntry;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import pizzaShop.controller.ErrorClass;
import pizzaShop.model.AccountSystem.Customer;
import pizzaShop.model.AccountSystem.Deliverer;
import pizzaShop.model.AccountSystem.Person;
import pizzaShop.model.AccountSystem.StaffMember;
import pizzaShop.model.DataBaseSystem.AddressRepository;
import pizzaShop.model.DataBaseSystem.CatalogHelper;
import pizzaShop.model.DataBaseSystem.CustomerRepository;
import pizzaShop.model.DataBaseSystem.ItemCatalog;
import pizzaShop.model.DataBaseSystem.PizzaOrderRepository;
import pizzaShop.model.ManagementSystem.Tan_Management.TanManagement;
import pizzaShop.model.OrderSystem.Ingredient;
import pizzaShop.model.OrderSystem.Item;
import pizzaShop.model.OrderSystem.ItemType;
import pizzaShop.model.OrderSystem.Pizza;
import pizzaShop.model.OrderSystem.PizzaOrder;
import pizzaShop.model.ProductionSystem.Oven;

@Component
public class Store {

	private final UserAccountManager employeeAccountManager;
	private final ItemCatalog itemCatalog;
	private final PizzaOrderRepository pizzaOrderRepo;
	private final CustomerRepository customerRepository;
	private final Accountancy accountancy;
	private final BusinessTime businessTime;
	private final CatalogHelper catalogHelper;
	private final TanManagement tanManagement;
	private ArrayList<StaffMember> staffMemberList; // why List and Repository for
												// StaffMember?
	private ArrayList<Oven> ovenList;

	private ArrayList<String> eMailList;

	private MailSender mailSender;

	private Pizzaqueue pizzaQueue = Pizzaqueue.getInstance();
	private Map<ProductIdentifier, ArrayList<String>> pizzaMap = new HashMap<ProductIdentifier, ArrayList<String>>();

	@Autowired
	public Store(UserAccountManager employeeAccountManager, ItemCatalog itemCatalog,
			PizzaOrderRepository pizzaOrderRepo, CustomerRepository customerRepository, Accountancy accountancy,
			BusinessTime businessTime, CatalogHelper catalogHelper, TanManagement tanManagement,
			AddressRepository addressRepository, MailSender mailSender) {

		this.employeeAccountManager = employeeAccountManager;
		this.itemCatalog = itemCatalog;
		this.pizzaOrderRepo = pizzaOrderRepo;
		this.customerRepository = customerRepository;
		this.staffMemberList = new ArrayList<StaffMember>();
		this.ovenList = new ArrayList<Oven>();
		this.accountancy = accountancy;
		this.businessTime = businessTime;
		this.catalogHelper = catalogHelper;
		this.tanManagement = tanManagement;
		this.eMailList = new ArrayList<String>();
		this.mailSender = mailSender;
		new ErrorClass(false);
		ovenList.add(new Oven(this));
		ovenList.add(new Oven(this));
		ovenList.add(new Oven(this));

	}

	public ArrayList<String> getEMailList() {
		return this.eMailList;
	}

	public boolean addEmailToMailingList(String eMailAddress) {

		if (!this.eMailList.contains(eMailAddress)) {

			SimpleMailMessage simpleMessage = new SimpleMailMessage();

			simpleMessage.setTo(eMailAddress);
			simpleMessage.setSubject("Papa Pizza Newsletter");
			simpleMessage.setText("Willkommen zum Papa Pizza Newsletter");

			try {
				this.mailSender.send(simpleMessage);
			} catch (MailException ex) {
				System.err.println(ex.getMessage());
			}

			return this.eMailList.add(eMailAddress);
		}

		return false;

	}

	public boolean removeEmailFromMailingList(String eMailAddress) {
		
		if (this.eMailList.contains(eMailAddress)) {

			SimpleMailMessage simpleMessage = new SimpleMailMessage();

			simpleMessage.setTo(eMailAddress);
			simpleMessage.setSubject("Papa Pizza Newsletter");
			simpleMessage.setText("Wir bedauern, dass sie nicht mehr am Papa Pizza Newsletter interessiert sind und hoffen, "
					+ "dass sie uns dennoch als treuer Kunde erhalten bleiben. ");

			try {
				this.mailSender.send(simpleMessage);
			} catch (MailException ex) {
				System.err.println(ex.getMessage());
			}

			return this.eMailList.remove(eMailAddress);
		}

		return false;
	}

	public void sendNewsletter(String newsletterText) {

		for (String eMailAddress : this.eMailList) {

			SimpleMailMessage simpleMessage = new SimpleMailMessage();

			simpleMessage.setTo(eMailAddress);
			simpleMessage.setSubject("Papa Pizza Newsletter");
			simpleMessage.setText(newsletterText);

			try {
				this.mailSender.send(simpleMessage);
			} catch (MailException ex) {
				System.err.println(ex.getMessage());
			}

		}

	}

	public Pizzaqueue getPizzaQueue() {

		return pizzaQueue;
	}

	public ArrayList<Oven> getOvens() {

		return ovenList;
	}
	
	/**
	 * finds an {@link Oven} through its id
	 * @param id of the {@link Oven}
	 * @return the {@link Oven} with OvenId = id OR null if not found
	 */
	public Oven findOvenById(int id){
		for(Oven o : getOvens()){
			if(o.getId() == id){
				return o;
			}
		}
		return null;
	}

	public ArrayList<StaffMember> getStaffMemberList() {
		return staffMemberList;
	}

	public void updateUserAccount(StaffMember member, String username, String password, Role role) {

		if (member.getUserAccount() == null) {
			member.setUsername(username);
			member.setPassword(password);
			member.setRole(role);

			member.setUserAccount(employeeAccountManager.create(username, password, role));

		} else {

		}

	}

	public StaffMember getStaffMemberByUsername(String name) {

		for (StaffMember staffMember : staffMemberList) {
			if (staffMember.getUsername().equals(name))
				return staffMember;
		}

		return null;
	}

	public StaffMember getStaffMemberByForename(String name) {

		for (StaffMember staffMember : staffMemberList) {
			if (staffMember.getPerson().getForename().equals(name)) {
				return staffMember;
			}
		}
		return null;
	}

	/**
	 * Method to fill the pizzaqueue, if order contains pizza
	 * 
	 * @param pOrder
	 *            PizzaOrder to be analyzed
	 * @param bakeTime  
	 * @return analyzed PizzaOrder
	 */
	public PizzaOrder analyzePizzaOrder(PizzaOrder pOrder, int bakeTime) {
		for (OrderLine l : pOrder.getOrder().getOrderLines()) {
			Item temp = itemCatalog.findOne(l.getProductIdentifier()).get();
			if (temp.getType().equals(ItemType.PIZZA)) {
				Pizza p = (Pizza) temp;
				for (int i = 0; i < l.getQuantity().getAmount().intValue(); i++) {
					addOrder(pOrder.getId(), p);
					pizzaQueue.add(p);
					pOrder.addAsUnbaked();
				}

			}
		}
		LocalDateTime estimatedTime = businessTime.getTime().plusSeconds(bakeTime);
		final int DRESDEN_TIME_CONSTANT = 25;
		estimatedTime = pOrder.getPickUp() ? estimatedTime : estimatedTime.plusMinutes(DRESDEN_TIME_CONSTANT); 
		pOrder.setEstimatedDelivery(estimatedTime);
		if (pOrder.getUnbakedPizzas() == 0) {
			pOrder.readyOrder();
		}
		pizzaOrderRepo.save(pOrder);
		return pOrder;

	}

	/**
	 * Update unbaked pizza in a {@link PizzaOrder}
	 * 
	 * @param pizza
	 */
	public void updatePizzaOrder(Pizza pizza) {

		if (pizza == null) {
			return;
		} else {

			Iterable<PizzaOrder> pizzaOrders = pizzaOrderRepo.findAll();

			for (PizzaOrder order : pizzaOrders) {

				if (order.getId().toString().equals(getFirstOrder(pizza))) {
					order.markAsBaked();

					try {
						removeFirstOrder(pizza);
					} catch (Exception e) {
						System.out.println("Fehler bei updatePizzaOrder");
						
					}

					pizzaOrderRepo.save(order);
					return;
				}
			}
		}
	}

	public void addOrder(OrderIdentifier o, Pizza p) {
		if (!pizzaMap.containsKey(p.getId())) {
			ArrayList<String> tmp = new ArrayList<String>();
			tmp.add(o.toString());
			pizzaMap.put(p.getId(), tmp);
		} else {
			pizzaMap.get(p.getId()).add(o.toString());
		}

	}

	/**
	 * 
	 * @return returns the first order as a String or null if not present
	 */
	public String getFirstOrder(Pizza p) {
		return pizzaMap.get(p.getId()).get(0);
	}

	/**
	 * removes order from the list
	 * 
	 * @return returns the name of the first order in the list
	 * @throws Exception
	 *             when there is no order in the list
	 */
	public String removeFirstOrder(Pizza p) throws NullPointerException {
		if (pizzaMap.get(p.getId()).isEmpty())
			throw new NullPointerException("Die Pizza hat keine Orders zugewiesen");
		return pizzaMap.get(p.getId()).remove(0);
	}


	/**
	 * finish Order and create AccountancyEntry
	 * 
	 * @param p
	 * @param msg
	 * @param del
	 */
	public void completeOrder(PizzaOrder p, String msg, Deliverer del) {
		p.completeOrder();
		pizzaOrderRepo.save(p);
		if (del == null) {
			AccountancyEntry a = new AccountancyEntry(p.getTotalPrice(),
					"Order von " + p.getCustomer().getPerson().getForename() + p.getCustomer().getPerson().getSurname()
							+ " über Account " + p.getOrder().getUserAccount().getUsername() + " " + msg);
			accountancy.add(a);

		} else {

			/*
			 * Probleme mit dem totalPrice, deshalb erstmal ein Workaround
			 * ProductPaymentEntry a = ProductPaymentEntry.of(p.getOrder(),
			 * "Order über Account " +
			 * p.getOrder().getUserAccount().getUsername() + " "+msg);
			 */
			AccountancyEntry a = new AccountancyEntry(p.getTotalPrice(),
					"Order über Account " + del.getUsername() + " " + msg);
			accountancy.add(a);
		}
	}

	public Pizza getNextPizza() throws NullPointerException {

		if (!pizzaQueue.isEmpty()) {
			return pizzaQueue.poll();
		} else {
			throw new NullPointerException("There is no Pizza in the PizzaQueue!");
		}
	}

	public void configurePizza(Model model, String ids[], String pizzaName, String admin_flag, String pizzaID,
			Cart cart) {

		Pizza newPizza;

		newPizza = new Pizza("custom", Money.of(2, "EUR"));

		for (int i = 0; i < ids.length; i++) {

			Item foundItem = catalogHelper.findItemByIdentifier(ids[i], ItemType.INGREDIENT);

			if (foundItem != null) {
				MonetaryAmount itemPrice = foundItem.getPrice();
				String itemName = foundItem.getName();

				Ingredient newIngredient = new Ingredient(itemName, itemPrice);
				newPizza.addIngredient(newIngredient);

				if (pizzaName.equals("") || (pizzaName == null)) {
					newPizza.setName("DIY Pizza");
				} else {
					newPizza.setName(pizzaName);
				}
			}
		}

		if ((admin_flag != null && !admin_flag.equals("")) && admin_flag.equals("true")
				&& (pizzaID != null && !pizzaID.equals(""))) {
			Pizza pizza = (Pizza) (catalogHelper.findItemByIdentifier(pizzaID, null));
			itemCatalog.delete(pizza);
		}

		boolean exist = false;
		Item existingPizza = null;
		for (Item i : itemCatalog.findByType(ItemType.PIZZA)) {

			if (i.toString().equals(newPizza.toString())) {
				exist = true;
				existingPizza = i;
			}
		}

		if (!exist) {
			Pizza savedPizza = itemCatalog.save(newPizza);
			cart.addOrUpdateItem(savedPizza, Quantity.of(1));
		} else {
			model.addAttribute("existingPizza", existingPizza.getName());
			if (existingPizza != null)
				cart.addOrUpdateItem(existingPizza, Quantity.of(1));
		}
	}

	/**
	 * 
	 * @param t
	 *            input telephonnumber
	 * @param p
	 *            {@link Person} to be edited (if new Person --> null)
	 * @return errormessage (isEmpty if valid)
	 */
	public String validateTelephonenumber(String t, Person p) {
		for (char c : t.toCharArray()) {
			if (!Character.isDigit(c))
				return "Telefonnummer darf nur Ziffern enthalten!";

		}

		for (Customer cu : this.customerRepository.findAll()) {
			if (cu.getPerson().getTelephoneNumber().equals(t) && !cu.getPerson().equals(p))
				return "Telefonnummber ist bereits vergeben!";
		}
		return "";
	}
}
