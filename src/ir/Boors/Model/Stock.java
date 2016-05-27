package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Stock {
	private String id ;
	private int ownerId;
	private List<Order> sellOrders;
	private List<Order> buyOrders;

	public Stock(String id,List<Order> sellOrders,List<Order> buyOrders) {
		this.id = id;
		this.sellOrders = sellOrders;
		this.buyOrders = buyOrders;
	}


	public Stock(String id, int ownerId){
		this.id = id;
		this.ownerId = ownerId;
		this.sellOrders = new LinkedList<Order>();
		this.buyOrders = new LinkedList<Order>();
	}




	public Stock(String id) {
		this.id = id;
		this.sellOrders = new LinkedList<Order>();
		this.buyOrders = new LinkedList<Order>();
	}

	public String getJson(){
		String json = "{" ;
		json+= "\"symbol\":\"" + id + "\"," ;
		json+= "\"ownerId\": "+ownerId+",";
		json+= "\"sellList\":[" ;
		for(Order o : sellOrders){
			json+= o.getJson() ;
			json+=",";
		}
		if(json.charAt(json.length()-1) == ',')
			json = json.substring(0,json.length()-1);
		json+="],";
		json+= "\"buyList\":[" ;
		for(Order o : buyOrders){
			json+= o.getJson() ;
			json+=",";
		}
		if(json.charAt(json.length()-1) == ',')
			json = json.substring(0,json.length()-1);
		json+="]";		
		json+="}";		
		return json;
	}

	public String getId() {
		return id;
	}

	public int getOwnerId(){
		return this.ownerId;
	}

	public void setId(String id) {
		this.id = id;
	}
	public List<Order> getSellOrders() {
		return sellOrders;
	}
	public void setSellOrders(List<Order> sellOrders) {
		this.sellOrders = sellOrders;
	}
	public List<Order> getBuyOrders() {
		return buyOrders;
	}
	public void setBuyOrders(List<Order> buyOrders) {
		this.buyOrders = buyOrders;
	}
}
