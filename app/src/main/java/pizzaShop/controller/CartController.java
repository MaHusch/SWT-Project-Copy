package pizzaShop.controller;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderIdentifier;
import org.salespointframework.order.OrderManager;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.time.BusinessTime;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import pizzaShop.model.actor.Customer;
import pizzaShop.model.actor.Deliverer;
import pizzaShop.model.actor.StaffMember;
import pizzaShop.model.catalog.Item;
import pizzaShop.model.catalog.ItemType;
import pizzaShop.model.store.Bill;
import pizzaShop.model.store.CustomerRepository;
import pizzaShop.model.store.ErrorClass;
import pizzaShop.model.store.ItemCatalog;
import pizzaShop.model.store.PizzaOrder;
import pizzaShop.model.store.PizzaOrderRepository;
import pizzaShop.model.store.PizzaOrderStatus;
import pizzaShop.model.store.StaffMemberRepository;
import pizzaShop.model.store.Store;
import pizzaShop.model.tan_management.Tan;
import pizzaShop.model.tan_management.TanManagement;

@Controller
@SessionAttributes("cart")
public class CartController {

	private final OrderManager<Order> orderManager;
	private final ItemCatalog itemCatalog;
	private final TanManagement tanManagement;
	private final CustomerRepository customerRepository;
	private final PizzaOrderRepository pizzaOrderRepository;
	private final StaffMemberRepository staffMemberRepository;
	private final BusinessTime businesstime;
	private Optional<Customer> customer = Optional.empty();
	private final Store store;
	private ErrorClass error;
	private boolean freeDrink = false;

	@Autowired
	public CartController(OrderManager<Order> orderManager, ItemCatalog itemCatalog, TanManagement tanManagement,
			CustomerRepository customerRepository, PizzaOrderRepository pizzaOrderRepository,
			StaffMemberRepository staffMemberRepository, Store store, BusinessTime businesstime) {
		this.orderManager = orderManager;
		this.itemCatalog = itemCatalog;
		this.tanManagement = tanManagement;
		this.customerRepository = customerRepository;
		this.pizzaOrderRepository = pizzaOrderRepository;
		this.staffMemberRepository = staffMemberRepository;
		this.store = store;
		this.businesstime = businesstime;
		error = new ErrorClass(false);
	}

	@ModelAttribute("cart")
	public Cart initializeCart() {
		return new Cart();
	}

	@RequestMapping("/cart")
	public String pizzaCart(Model model, @ModelAttribute Cart cart) {
		model.addAttribute("items", itemCatalog.findAll());
		
		ArrayList<Item> freeDrinks = new ArrayList<Item>();
		for(Item i : itemCatalog.findAll()){
			if(i.getType().equals(ItemType.FREEDRINK))
				freeDrinks.add(i);
		}
		model.addAttribute("freeDrinks", freeDrinks);
		model.addAttribute("error", error);
		model.addAttribute("customer", customer);
		model.addAttribute("freeDrink", freeDrink);
		return "cart";
		
	}

	@RequestMapping("/orders")
	public String pizzaOrder(Model model) {

		ArrayList<StaffMember> deliverers = new ArrayList<StaffMember>();

		for (StaffMember staff : store.getStaffMemberList()) {
			if (staff.getRole().getName().contains("DELIVERER")) {
				Deliverer deliverer = (Deliverer) staff;
				if (deliverer.getAvailable()) {
					deliverers.add(staff);
				}
			}
		}

		ArrayList<PizzaOrder> uncompletedOrders = new ArrayList<PizzaOrder>();
		ArrayList<PizzaOrder> completedOrders = new ArrayList<PizzaOrder>();

		for (PizzaOrder po : pizzaOrderRepository.findAll()) {
			if (po.getOrderStatus().equals(PizzaOrderStatus.COMPLETED)) {
				completedOrders.add(po);
			} else {
				uncompletedOrders.add(po);
			}
		}

		model.addAttribute("uncompletedOrders", uncompletedOrders);
		model.addAttribute("completedOrders", completedOrders);
		model.addAttribute("deliverers", deliverers);
		model.addAttribute("error", error);

		return "orders";
	}

	@RequestMapping(value = "/confirmCollection", method = RequestMethod.POST)
	public String cofirmLocalOrder(@RequestParam("orderID") OrderIdentifier id){
		PizzaOrder p = pizzaOrderRepository.findOne(id);
		if(p.getOrderStatus().equals(PizzaOrderStatus.READY)){
			error.setError(false);
			store.completeOrder(pizzaOrderRepository.findOne(id), "mitgenommen");
		}else{
			error.setError(true);
		}
		return "redirect:orders";
	}
	
	@RequestMapping(value = "/addCartItem", method = RequestMethod.POST)
	public String addItem(@RequestParam("pid") ProductIdentifier id, @RequestParam("number") int number,
			@ModelAttribute Cart cart) {
		// Assert.notNull(id, "ID must not be null!");
		// System.out.println(id + itemCatalog.findOne(id).toString());

		if (itemCatalog.findOne(id).isPresent()) {
			cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(number));
		}
		return "redirect:catalog";

	}

	@RequestMapping(value = "/removeCartItem", method = RequestMethod.POST)
	public String removeItem(@RequestParam("ciid") String cartId, @ModelAttribute Cart cart) {
		if(cart.getItem(cartId).get().getPrice().isZero())
			freeDrink = false;
		cart.removeItem(cartId);
		
	
		return "redirect:cart";

	}
	
	@RequestMapping(value = "/changeQuantity", method = RequestMethod.POST)
	public String changeQuantity(@RequestParam("ciid") String cartId, @RequestParam("amount") int amount, @RequestParam("quantity") int
			quantity, @RequestParam("pid") ProductIdentifier id, @ModelAttribute Cart cart){
		Optional<Item> item = itemCatalog.findOne(id); 
		assertTrue("Product must not be emtpy!", item.isPresent());
		if(quantity + amount == 0){
			return removeItem(cartId, cart);
		}else{
			cart.addOrUpdateItem(item.get(), Quantity.of(amount));
		}
		if(cart.getPrice().getNumber().intValue() < 30){
			Iterator<CartItem> ci = cart.iterator();
			while(ci.hasNext()){
				CartItem i = ci.next();
				if(((Item) i.getProduct()).getType().equals(ItemType.FREEDRINK)){
					cart.removeItem(i.getId());
					break;
				}
			}
			freeDrink=false;
		}
		return "redirect:cart";
	}
	
	@RequestMapping(value = "/addFreeDrink", method = RequestMethod.POST)
	public String addFreeDrink(@RequestParam("iid") ProductIdentifier id, @ModelAttribute Cart cart){
		cart.addOrUpdateItem(itemCatalog.findOne(id).get(), Quantity.of(1));
		freeDrink=true;
		return "redirect:cart";
	}

	@RequestMapping(value = "/checkTan", method = RequestMethod.POST)
	public String checkTan(@RequestParam("tnumber") String telephoneNumber, @RequestParam("tan") String tanValue) {

		Tan tan = tanManagement.getTan(telephoneNumber);
		if (tan.getTanNumber().equals(tanValue)) {
			error.setError(false);
			for (Customer c : customerRepository.findAll()) {
				System.out.println("test" + c.getPerson().getTelephoneNumber());
				if (telephoneNumber.equals(c.getPerson().getTelephoneNumber())) {
					customer = Optional.of(c);
					System.out.println("valid: " + tanValue + " tel: " + customer.get().getPerson().getTelephoneNumber());
				}
			}
		} else {
			error.setError(true);
			error.setMessage("Fehler bei der TAN-Überprüfung! Erneut eingeben!");
			System.out.println("fail");
		}

		return "redirect:cart";

	}
	
	@RequestMapping(value = "/logoutCustomer", method = RequestMethod.POST)
	public String logoutCustomer(){
		customer = Optional.empty();
		return "redirect:cart";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String buy(@ModelAttribute Cart cart, @RequestParam("onSite") String onSiteStr,
			@RequestParam("cutlery") String cutleryStr,@LoggedIn Optional<UserAccount> userAccount) {
		if (!userAccount.isPresent()) {
			return "redirect:login";
		}
		assertTrue("Checkbox liefert anderen Wert als 0 oder 1! nämlich" + onSiteStr,
				onSiteStr.equals("0,1") | onSiteStr.equals("0"));
		System.out.println("cutlery ist:" + cutleryStr);
		if (customer.isPresent()) {
			boolean onSite = false;
			boolean cutlery = true;
			System.out.println(onSiteStr + " onSite");
			if (onSiteStr.equals("0,1")) {
				onSite = true;
			}
			if(cutleryStr.equals("0")) cutlery = false;
			
			//TODO: check if customer already has a cutlery --> throw error
			if(cutlery) { 
				// if false --> return error
				store.lentCutlery(customer.get(), businesstime.getTime());
			}

			PizzaOrder pizzaOrder = new PizzaOrder(userAccount.get(), Cash.CASH,
					tanManagement.generateNewTan(customer.get().getPerson().getTelephoneNumber()), onSite, customer.get());// tanManagement.getTan(customer.getTelephoneNumber()));
			cart.addItemsTo(orderManager.save(pizzaOrder.getOrder()));
			store.analyzeOrder(pizzaOrderRepository.save(pizzaOrder));
			cart.clear();
			
			//Bill bill = new Bill(customer.get(), pizzaOrder, businesstime.getTime());
			// customer = Optional.empty(); disabled for testing purposes
		}
		return "redirect:cart";

	}

	@RequestMapping(value = "/assignDeliverer", method = RequestMethod.POST)
	public String assignDeliverer(Model model, @RequestParam("delivererName") String name,
			@RequestParam("orderID") OrderIdentifier orderID) {// @RequestParam
																// OrderIdentifier
		if (name == null || name.equals("")) {
			error.setError(true);
			error.setMessage("Keinen Lieferanten ausgewählt!");
		} else {
			Deliverer deliverer = (Deliverer) store.getStaffMemberByForename(name);

			deliverer.addOrder(orderID);
		}
		return "redirect:orders";
	}
}
