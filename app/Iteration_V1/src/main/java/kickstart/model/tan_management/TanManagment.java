package kickstart.model.tan_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class TanManagment {
	
	public static final String EMPTY_STRING = "";
	public static final Tan NOT_FOUND_TAN = new Tan("00000", TanStatus.NOT_FOUND);
	
	private HashMap<Tan,String> tanHashMap = new HashMap<>();
	
	private HashMap<Tan,String> notAsignedTans = new HashMap<>();
	
	public TanManagment()//ArrayList<String> telephoneNumberList)
	{
		/*	
	 	Iterator<String> telephoneNumberListIterator = telephoneNumberList.iterator();

		
		while(telephoneNumberListIterator.hasNext())
		{
			String newTelephoneNumber = telephoneNumberListIterator.next();
			this.generateNewTan(newTelephoneNumber);
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
	
	public boolean checkTan(Tan tan, String telephoneNumber)
	{
		
		Iterator<Map.Entry<Tan, String>> hashMapIterator = tanHashMap.entrySet().iterator();
		
		while(hashMapIterator.hasNext())
			{
				Map.Entry<Tan, String> entry = (Map.Entry<Tan, String>)hashMapIterator.next();
				
				if(entry.getValue() == telephoneNumber && entry.getKey().getTanNumber().equals(tan.getTanNumber())) return true;
			
			}
		
		return false;
	}
	
	public Tan generateNewTan(String telephoneNumber)
	{
		
		Set<Tan> existingTans = tanHashMap.keySet();
		
		Iterator<Tan> existingTansIterator = existingTans.iterator();
		
		Set<Tan> existingNotAsignedTans = notAsignedTans.keySet();
		
		Iterator<Tan> existingNotAsignedTansIterator = existingNotAsignedTans.iterator();
		
		boolean foundNewTan = false;
		
		while(!foundNewTan)
		{
			Integer newTan = new Random().nextInt(99999) + 1;
			
			String newTanString = newTan.toString();
			
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
			
			while(existingNotAsignedTansIterator.hasNext() && !tanAlreadyExists)
			{
					
				if(newTanString.equals(((Tan) existingNotAsignedTansIterator.next()).getTanNumber())) tanAlreadyExists = true;
				
			}
			
			if(!tanAlreadyExists)
			{
				
				Tan newlyCreatedTan = new Tan(newTanString, TanStatus.VALID);
				
				notAsignedTans.put(newlyCreatedTan, telephoneNumber);
						
				return newlyCreatedTan;
			}
			
		}
		
		return NOT_FOUND_TAN;
			
	}
	
	public void invalidateTan(Tan tan)
	{
		
		tan.setStatus(TanStatus.USED);
		
		String telephoneNumber = this.getTelephoneNumber(tan);
		
		//System.out.println("replacing");
		
		tanHashMap.replace(tan, telephoneNumber, EMPTY_STRING);
		
		
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
	
	public void asignTan(Tan tan)
	{
		String telephoneNumber = this.notAsignedTans.get(tan);
		
		Tan oldTan = this.getTan(telephoneNumber);
		
		if(oldTan.getStatus() != TanStatus.NOT_FOUND)
		{
			
			this.invalidateTan(oldTan);
			
			tanHashMap.replace(oldTan, telephoneNumber, EMPTY_STRING);
					
		}

		//System.out.println("new tan added");
		
		tanHashMap.put(tan, telephoneNumber);
		
	}
	
	public boolean deleteNotAsignedTan(Tan tan)
	{
		String telephoneNumber = this.notAsignedTans.get(tan);
		
		return notAsignedTans.remove(tan, telephoneNumber);
		
	}



}
