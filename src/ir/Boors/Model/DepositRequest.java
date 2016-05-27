package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.LinkedList;
import java.util.List;
import java.sql.*;

public class DepositRequest {
	
	private int id;
	private int userId;
	private int amount;
	
	public DepositRequest(int userId,int amount) {
		this.id = -1 ;
		this.userId = userId;
		this.amount = amount;
	}

	public DepositRequest(int id,int userId,int amount) {
		this.id = id ;
		this.userId = userId;
		this.amount = amount;
	}
	
	public String getJson(){
		String json = "{" ;
		json+= "\"id\":" + id + "," ;
		json+= "\"userId\":" + userId + "," ;
		json+= "\"amount\":" + amount ;
		json+= "}";
		return json;
	}

	public int getUserId(){
		return userId;
	}

	public int getAmount(){
		return amount;
	}

	public int getId(){
		return id;
	}
}
