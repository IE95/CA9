package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.HashMap;
import java.util.Map;
import java.util.*;


public class User {
	private String name ;
	private String family ;
	private int id ;
	private int balance ;
	private Map<String,Integer> sharesAmount ;
	private List<Order> orders;	
	private List<DepositRequest> depositRequests;

	
	public User(int id,String name ,String family,int balance,Map<String,Integer> sharesAmount) {
		this.id = id;
		this.name = name;
		this.family = family;
		this.balance = balance;
		this.sharesAmount = sharesAmount;
		this.orders = new LinkedList<Order>();
		this.depositRequests = new LinkedList<DepositRequest>();
	}

	public User(int id,String name ,String family) {
		this.id = id;
		this.name = name;
		this.family = family;
		this.balance = 0;
		this.sharesAmount = new HashMap<String, Integer>();
		this.orders = new LinkedList<Order>();
		this.depositRequests = new LinkedList<DepositRequest>();
	}

	public User(int id){
		this.id = id;
	}
	
	public void deposit(int amount){
		balance+=amount ;
	}
	
	public boolean withdraw(int amount){
		if(balance<amount)
			return false ;
		balance-=amount;
		return true ;
	}	
	
	public int getShareAmount(String id){
		Integer amount = sharesAmount.get(id); 
		if(amount==null)
			return 0;
		return amount;
	}
	
	public void decreaseShareAmount(int Amount,String id){
		Integer currAmount = sharesAmount.get(id);
		if(currAmount == null)
			return;
		sharesAmount.replace(id, currAmount-Amount);
	}

	public void increaseShareAmount(int Amount,String id){
		Integer currAmount = sharesAmount.get(id);
		if(currAmount == null)
			sharesAmount.put(id, Amount);
		else
			sharesAmount.replace(id, currAmount+Amount);		
	}
	
	public void addOrder(Order order){
		orders.add(order);
	}

	public void delOrder(Order order){
		for (Iterator<Order> it = orders.listIterator();it.hasNext();) {
			Order curr = it.next();
			if(curr.getId() == order.getId()){
				it.remove();
			}
		}
	}

	public void addDepositRequest(DepositRequest dr){
		depositRequests.add(dr);
	}

	public void clearDepositRequests(){
		depositRequests.clear();
	}

	public int getId() {
		return id;
	}

	public String getName(){
		return name ;
	}

	public String getFamily(){
		return family ;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getJson(){
		String json = "{" ;
		json+= "\"id\":" + id + "," ;
		json+= "\"name\":\"" + name + "\"," ;
		json+= "\"family\":\"" + family + "\"," ;
		json+= "\"balance\":" + balance + "," ;
		json+= "\"shares\":[" ;
		for(String key : sharesAmount.keySet()){
			json+= "{\"name\":\"" + key + "\"," ;
			json+= "\"quan\":" + sharesAmount.get(key) + "}";
			json+=",";
		}
		if(json.charAt(json.length()-1) == ',')
			json = json.substring(0,json.length()-1);
		json+="],";
		json+= "\"depositRequests\":[" ;
		for(DepositRequest dr : depositRequests){
			json+= dr.getJson() ;
			json+=",";
		}
		if(json.charAt(json.length()-1) == ',')
			json = json.substring(0,json.length()-1);
		json+="],";
		json+= "\"reqs\":[" ;
		for(Order o : orders){
			json+= o.getJson() ;
			json+=",";
		}
		if(json.charAt(json.length()-1) == ',')
			json = json.substring(0,json.length()-1);
		json+="]";		
		json+="}";		
		return json;
	}

}
