package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Record {
	private int id;
	private String stockId;
	private User seller;
	private User buyer;
	private int sellerCurrBalance;
	private int buyerRemainBalance;
	private int quantity;
	private int price;	
	private String opType;
	private Date date;
	
	
	public Record(int id,String stockId, User seller,User buyer,int sellerCurrBalance,int buyerRemainBalance,int quantity,int price,String opType,Date date) {
		this.id = id;
		this.stockId = stockId;
		this.seller = seller;
		this.buyer = buyer;
		this.sellerCurrBalance = sellerCurrBalance;
		this.buyerRemainBalance = buyerRemainBalance;
		this.quantity = quantity;
		this.price = price;
		this.opType = opType;
		this.date = date;
	}

	public Record(int id,String stockId, User seller,User buyer,int sellerCurrBalance,int buyerRemainBalance,int quantity,int price,String opType) {
		this(id,stockId,seller,buyer,sellerCurrBalance,buyerRemainBalance,quantity,price,opType,new Date());
	}

	
	public String toString(){
		return buyer.getId()+", "+seller.getId()+", "+stockId+", "+opType+", "+quantity+", "+buyerRemainBalance+", "+sellerCurrBalance ;
	}	

	public String getJson(){
		DateFormat df= new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
		String json = "{" ;
		json+= "\"symbol\":\"" + stockId + "\"," ;
		json+= "\"seller\":{\"id\":" + seller.getId() + ",\"name\":\"" + seller.getName() + "\",\"family\":\"" + seller.getFamily() + "\"},";
		json+= "\"buyer\":{\"id\":" + buyer.getId() + ",\"name\":\"" + buyer.getName() + "\",\"family\":\"" + buyer.getFamily() + "\"},";
		json+= "\"quan\":" + quantity + "," ;
		json+= "\"price\":" + price + "," ;
		json+= "\"method\":\"" + opType + "\"," ;
		json+= "\"date\":\"" + df.format(date) + "\"";
		json+= "}";
		return json;
	}

	public int getId(){
		return id;
	}

	public String getStockId(){
		return stockId;
	}

	public User getSeller(){
		return seller;
	}

	public User getBuyer(){
		return buyer;
	}

	public int getSellerBalance(){
		return sellerCurrBalance;
	}	

	public int getBuyerBalance(){
		return buyerRemainBalance;
	}	

	public int getQuantity() {
		return quantity;
	}

	public int getPrice() {
		return price;
	}

	public String getOpType() {
		return opType;
	}

	public Date getDate(){
		return date;
	}

}
