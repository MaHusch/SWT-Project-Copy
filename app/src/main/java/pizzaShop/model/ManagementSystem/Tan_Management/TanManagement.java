package pizzaShop.model.ManagementSystem.Tan_Management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class TanManagement {
	
	public static final String EMPTY_STRING = "";
	public static final Tan NOT_FOUND_TAN = new Tan("00000", TanStatus.NOT_FOUND);
	
	private HashMap<Tan,String> tanHashMap = new HashMap<>();
	
	private HashMap<Tan,String> notConfirmedTans = new HashMap<>();
	
	public TanManagement()//ArrayList<String> telephoneNumberList)
	{
		/*
	 	Iterator<String> telephoneNumberListIterator = telephoneNumberList.iterator();

		
		while(telephoneNumberListIterator.hasNext())
		{
			String newTelephoneNumber = telephoneNumberListIterator.next();
			Tan newTan = this.generateNewTan(newTelephoneNumber);
			//System.out.println(newTelephoneNumber);
			//System.out.println(newTan.getTanNumber());
			this.confirmTan(newTan);
		}
		*/
	}
	
	public Tan getTan(String telephoneNumber)
	{

		Iterator<Map.Entry<Tan, String>> hashMapIterator = tanHashMap.entrySet().iterator();
		
		while(hashMapIterator.hasNext())
			{
				Map.Entry<Tan, String> entry = (Map.Entry<Tan, String>)hashMapIterator.next();
				
				if(entry.getValue().equals(telephoneNumber)) return entry.getKey();

			
			}
		
		//System.out.println("not found");
		
		return NOT_FOUND_TAN;
		
	}
	
	public String getTelephoneNumber(Tan tan)
	{
		if(tanHashMap.containsKey(tan))
		{	
			return tanHashMap.get(tan);
		}
		else return EMPTY_STRING;
				
	}
	
	/**
	 * This method checks whether the given TelephoneNumber is the same as the TelephoneNumber that is saved 
	 * with the given TAN as the key in the TanHashMap.
	 * 
	 * @param tan				the TAN Object to be checked
	 * @param telephoneNumber	the TelephoneNumber to be checked
	 * @return
	 */
	
	public boolean checkTan(Tan tan, String telephoneNumber)
	{
		
		String receivedTelephoneNumber = this.getTelephoneNumber(tan);
		
		if(telephoneNumber.equals(receivedTelephoneNumber) && tan.getStatus() == TanStatus.VALID) return true;
		
		else return false;
		
	}
	
	/**
	 * This method checks whether the given TelephoneNumber is the same as the TelephoneNumber that is saved 
	 * with the given TAN as the key in the TanHashMap.
	 * 
	 * @param tanString			the TanNumber to be checked
	 * @param telephoneNumber	the TelephoneNumber to be checked
	 * @return
	 */
	
	public boolean checkTan(String tanString, String telephoneNumber)
	{
		
		Iterator<Map.Entry<Tan, String>> hashMapIterator = tanHashMap.entrySet().iterator();
		
		while(hashMapIterator.hasNext())
			{
				Map.Entry<Tan, String> entry = (Map.Entry<Tan, String>)hashMapIterator.next();
				
				if(entry.getKey().getTanNumber().equals(tanString) && entry.getValue().equals(telephoneNumber) && entry.getKey().getStatus() == TanStatus.VALID) return true;

			
			}
		
		return false;
		
	}
	
	public Tan getTanByTanNumber(String tanNumber)
	{
		Iterator<Map.Entry<Tan, String>> hashMapIterator = tanHashMap.entrySet().iterator();
		
		while(hashMapIterator.hasNext())
			{
				Map.Entry<Tan, String> entry = (Map.Entry<Tan, String>)hashMapIterator.next();
				
				Tan tanEntry = entry.getKey();
				
				if(tanEntry.getTanNumber().equals(tanNumber)) return tanEntry;

			
			}
		
		return NOT_FOUND_TAN;
		
	}
	
	public Tan getNotConfirmedTanByTanNumber(String tanNumber)
	{
		Iterator<Map.Entry<Tan, String>> hashMapIterator = notConfirmedTans.entrySet().iterator();
		
		while(hashMapIterator.hasNext())
			{
				Map.Entry<Tan, String> entry = (Map.Entry<Tan, String>)hashMapIterator.next();
				
				Tan tanEntry = entry.getKey();
				
				if(tanEntry.getTanNumber().equals(tanNumber)) return tanEntry;

			
			}
		
		return NOT_FOUND_TAN;
		
	}
	
	/**
	 * Generates a new TAN for the given TelephoneNumber. 
	 * To confirm the newly generated TAN after the order has been complete use confirmTan(tan)
	 * If generating a new TAN fails a TAN with the status NOT_FOUND is returned.
	 * 
	 * @param telephoneNumber	the TelephoneNumber 
	 * @return
	 */
	
	public Tan generateNewTan(String telephoneNumber)
	{
		
		Set<Tan> existingTans = tanHashMap.keySet();
		
		Iterator<Tan> existingTansIterator = existingTans.iterator();
		
		Set<Tan> existingNotConfirmedTans = notConfirmedTans.keySet();
		
		Iterator<Tan> existingNotConfirmedTansIterator = existingNotConfirmedTans.iterator();
		
		boolean foundNewTan = false;
		
		while(!foundNewTan)
		{
			
			Integer newTan = new Random().nextInt(99999) + 1;
			
			String newTanString = newTan.toString();
			
			// This is to ensure that all TANs have 5 digits and that leading zeros are possible.
			
			if(newTanString.length() == 4)			
			{				
				newTanString = "0" + newTanString;
			}
			if(newTanString.length() == 3)
			{
				newTanString = "00" + newTanString;
			}
			
			boolean tanAlreadyExists = false;
			
			while(existingTansIterator.hasNext() && !tanAlreadyExists)
			{
					
				if(newTanString.equals(((Tan) existingTansIterator.next()).getTanNumber())) tanAlreadyExists = true;
				
			}
			
			while(existingNotConfirmedTansIterator.hasNext() && !tanAlreadyExists)
			{
					
				if(newTanString.equals(((Tan) existingNotConfirmedTansIterator.next()).getTanNumber())) tanAlreadyExists = true;
				
			}
			
			if(!tanAlreadyExists)
			{
				
				Tan newlyCreatedTan = new Tan(newTanString, TanStatus.NOT_CONFIRMED);
				
				notConfirmedTans.put(newlyCreatedTan, telephoneNumber);
				
				//System.out.println("new Tan added");
				//System.out.println(newlyCreatedTan.getTanNumber());
				
				return newlyCreatedTan;
				
			}
			
		}
		
		return NOT_FOUND_TAN;
			
	}
	
	/**
	 * This method confirms a Tan by setting its status to VALID and putting it into the tanHashMap.
	 * If there already exists an entry with the same TelephoneNumber the status of the tan in this entry is set to USED.
	 * The entry of this Tan in the notConfirmedTans HashMap gets deleted.
	 * 
	 * @param tan	The Tan you want to confirm
	 */
	
	public void confirmTan(Tan tan)
	{
		String telephoneNumber = this.notConfirmedTans.get(tan);
		
		Tan oldTan = this.getTan(telephoneNumber);
		
		if(oldTan.getStatus() != TanStatus.NOT_FOUND)
		{
			//System.out.println("found old tan");
			this.invalidateTan(oldTan);
					
		}
		
		deleteNotConfirmedTan(tan);
		
		tan.setStatus(TanStatus.VALID);
		
		tanHashMap.put(tan, telephoneNumber);
				
	}
	
	/**
	 * This method invalidates a Tan by setting its status to USED and replacing the TelephoneNumber with an empty String.
	 * 
	 * @param tan	The Tan you want to invalidate
	 */
	
	public void invalidateTan(Tan tan)
	{
		
		tan.setStatus(TanStatus.USED);
		
		String telephoneNumber = this.getTelephoneNumber(tan);
		
		//System.out.println("replacing");
		
		tanHashMap.replace(tan, telephoneNumber, EMPTY_STRING);
		
		
	}
		
	
	public boolean deleteNotConfirmedTan(Tan tan)
	{
		String telephoneNumber = this.notConfirmedTans.get(tan);
		
		return notConfirmedTans.remove(tan, telephoneNumber);
		
	}
	
	
	
	public Iterable<Map.Entry<Tan, String>> getAllTans()
	{
		
		Iterator<Map.Entry<Tan, String>> tanHashIterator = tanHashMap.entrySet().iterator();
		
		ArrayList<Map.Entry<Tan, String>> allEntrys = new ArrayList<Map.Entry<Tan, String>>();
		
		while(tanHashIterator.hasNext())
		{
			allEntrys.add(tanHashIterator.next());
			
		}
		
		return allEntrys;
	}
	
	public Iterable<Map.Entry<Tan, String>> getAllNotConfirmedTans()
	{
		
		Iterator<Map.Entry<Tan, String>> tanHashIterator = notConfirmedTans.entrySet().iterator();
		
		ArrayList<Map.Entry<Tan, String>> allEntrys = new ArrayList<Map.Entry<Tan, String>>();
		
		while(tanHashIterator.hasNext())
		{
			allEntrys.add(tanHashIterator.next());
			
		}
		
		return allEntrys;
	}
	
	public void updateTelephoneNumber(String oldTelephoneNumber, String newTelephoneNumber)
	{
		Iterable<Map.Entry<Tan, String>> allTans = getAllTans();
		
		Iterable<Map.Entry<Tan, String>> allNotConfirmedTans = getAllNotConfirmedTans();
		
		for(Entry<Tan, String> entry : allTans)
		{
			if(entry.getValue().equals(oldTelephoneNumber))
			{
				tanHashMap.replace(entry.getKey(), oldTelephoneNumber, newTelephoneNumber);
			}
		}
		
		for(Entry<Tan, String> entry : allNotConfirmedTans)
		{
			if(entry.getValue().equals(oldTelephoneNumber))
			{
				tanHashMap.replace(entry.getKey(), oldTelephoneNumber, newTelephoneNumber);
			}
		}
		
	}
	
	public void deleteUsedTans()
	{
		Iterable<Map.Entry<Tan, String>> allTans = getAllTans();	
		
		for(Entry<Tan, String> entry : allTans)
		{
			if(entry.getKey().getStatus().equals(TanStatus.USED))
			{
				tanHashMap.remove(entry.getKey());
			}
		}
		

		
	}

}
