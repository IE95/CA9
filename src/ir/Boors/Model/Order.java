package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

public class Order implements Comparable<Order>{
	private String stockId;
	private int id;
	private User user;
	private int quantity;
	private int price;
	private String orderType;
	private String opType;
	
	
	
	public Order(int id,String stockId, User user, int quantity, int price,String orderType, String opType) {
		this.id = id;
		this.stockId = stockId;
		this.user = user;
		this.quantity = quantity;
		this.price = price;
		this.orderType = orderType;
		this.opType = opType;
	}

	public int compareTo(Order o2){
		return this.price - o2.price ;
	}
	
	public static String execute(Order o1,Order o2) throws SQLException{
		Order sell,buy;
		String type;
		if(o1.getOrderType().equals("sell")){
			sell = o1;
			buy = o2;
		}else{
			sell =o2;
			buy=o1;
		}		
		if(sell.opType.equals("MPO") || buy.opType.equals("MPO"))
			type = "MPO" ;
		else if(sell.opType.equals("IOC") || buy.opType.equals("IOC"))
			type = "IOC" ;
		else 
			type = "GTC" ;
		int price ,quantity;
		if(sell.opType.equals("MPO"))
			price = buy.price ;
		else if(buy.opType.equals("MPO"))
			price = sell.price ;
		else
			price = buy.price ;
		if(sell.quantity < buy.quantity)
			quantity = sell.quantity ;
		else
			quantity = buy.quantity ;
		sell.user.deposit(quantity*price);
		DAO.updateUserInfo(sell.user);
		if(!buy.opType.equals("GTC")){
			buy.user.withdraw(quantity*price);
			DAO.updateUserInfo(buy.user);
		}
		sell.quantity-=quantity ;
		buy.quantity-=quantity ;
		DAO.updateOrder(sell);
		DAO.updateOrder(buy);
		if(sell.user.getId()!=1 && !sell.opType.equals("GTC")){
			sell.user.decreaseShareAmount(quantity, sell.stockId);
			DAO.updateShareAmount(sell.user.getId(),sell.stockId,sell.user.getShareAmount(sell.stockId));
		}
		buy.user.increaseShareAmount(quantity, buy.stockId);
		if(DAO.findShareAmount(buy.user.getId(),buy.stockId) == null)
			DAO.addShareAmount(buy.user.getId(),buy.stockId,buy.user.getShareAmount(buy.stockId));
		else
			DAO.updateShareAmount(buy.user.getId(),buy.stockId,buy.user.getShareAmount(buy.stockId));
		DAO.addRecord(new Record(-1,sell.stockId,sell.user,buy.user,sell.user.getBalance(),buy.user.getBalance(),quantity,price,type));
		return sell.user.getId() + " sold " + quantity + " shares of " + sell.stockId +" @"+price+" to " + buy.user.getId() ; 
	}
	
	public static boolean isPriceMatch(Order o1 , Order o2){
		Order sell,buy;
		if(o1.getOrderType().equals("sell")){
			sell = o1;
			buy = o2;
		}else{
			sell =o2;
			buy=o1;
		}
		return sell.getPrice()<=buy.getPrice() ;
	}	
	
	public String getJson(){
		String json = "{" ;
		json+= "\"symbol\":\"" + stockId + "\"," ;
		json+= "\"userId\":" + user.getId() + "," ;
		json+= "\"quan\":" + quantity + "," ;
		json+= "\"price\":" + price + "," ;
		json+= "\"type\":\"" + orderType + "\"," ;
		json+= "\"opType\":\"" + opType +"\"";		
		json+= "}";
		return json;
	}

	public int getId(){
		return id;
	}

	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	public int getOrderValue(){
		return quantity*price;
	}
}
