package ir.Boors.ServiceHandler;
import ir.Boors.Model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;

@WebServlet("/dotrade")
public class DoTrade extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		int id,price,quantity;
		String instrument,type;
		String json = "{\"result\":" ;
		Class typeHandler ;
		List<String> messages = new LinkedList<String>(); 
		boolean hasError = false;
		resp.setContentType("text/html");
		String command;
		String notCheck;
		try{
			try{
			command = req.getParameter("tradeType");
			id = Integer.parseInt(req.getParameter("id"));
			instrument = req.getParameter("instrument");
			price = Integer.parseInt(req.getParameter("price"));
			quantity = Integer.parseInt(req.getParameter("quantity"));
			type = req.getParameter("opType");
			notCheck = req.getParameter("notCheck");

			}catch(NumberFormatException e){
				messages.add("Mismatched parameters");
				throw new Exception() ;
			}
			if(instrument==null || type==null || command==null){
				messages.add("Mismatched parameters");
				throw new Exception();
			}
			if(!(command.equals("sell") || command.equals("buy"))){
				messages.add("Unknown Command");
				throw new Exception();
			}
			User user = DAO.serachUser(id);
			Stock stock = DAO.serachStock(instrument);
			boolean isCorrect = true ;
			if(user==null){			
				messages.add("Unknown user id");
				throw new Exception();
			}
			if(stock == null && (user.getId() != 1 || !command.equals("sell"))){
				messages.add("Invalid symbol id");
				hasError = true;
			}else if (stock == null && user.getId() == 1 && command.equals("sell")){
				Stock newStock = new Stock(instrument);
				DAO.addStock(newStock);
				stock = newStock ;
			}
			if(type.equals("MPO") && price!=0){
				messages.add("in MPO type price should be 0");
				hasError = true ;
			}
			if(hasError){
				throw new Exception();				
			}
			if(command.equals("sell")){
				if(user.getShareAmount(instrument)<quantity && user.getId()!=1){
					messages.add("Not enough share");
					throw new Exception();				
				}	
			}else{
				if((user.getBalance()<quantity*price) ){
					messages.add("Not enough money");
					throw new Exception();				
				}							
			}
			try{
				Order order = new Order(-1,stock.getId(), user, quantity, price, command, type);
				if(notCheck!= null && notCheck.equals("yes")){
					typeHandler = Class.forName("ir.Boors.ServiceHandler.OpTypeHandler." + type + "Handler");
					Method handler = typeHandler.getMethod("handle",Order.class,List.class,List.class);
					messages.addAll((List<String>)handler.invoke(null,order,stock.getSellOrders(),stock.getBuyOrders()));
				}
				else{
					int limit = DAO.getLimit();
					if(limit!=-1 && order.getOrderValue() > limit){
						DAO.addExpensiveOrder(order);
						messages.add("request was expensive");
					}
					else{
						typeHandler = Class.forName("ir.Boors.ServiceHandler.OpTypeHandler." + type + "Handler");
						Method handler = typeHandler.getMethod("handle",Order.class,List.class,List.class);
						messages.addAll((List<String>)handler.invoke(null,order,stock.getSellOrders(),stock.getBuyOrders()));
					}
				}
				
				
			}catch(Exception ex){
				messages.add("Invalid type");
				ex.printStackTrace();
				throw new Exception();								
			}
		}catch(SQLException e){
			hasError = true;
			messages.add("error in connecting to db");
		}catch(Exception e){
			hasError = true;
		}
		if(hasError || messages.get(0).equals("Order is declined") || messages.get(0).equals("Not enough money") || messages.get(0).equals("error in connecting to db")){
			json+="0";
		}
		else{
			json+="1";	
		}
		json+=",\"messages\":[";
		for (String s : messages) {
			json+= "\"" + s + "\",";
		}
		if(json.charAt(json.length()-1) == ',')
			json = json.substring(0,json.length()-1);			
		json+="]}";
		resp.setContentType("text/html");
		resp.getWriter().println(json);
		return ;
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}

}