package ir.Boors.ServiceHandler.OpTypeHandler;
import ir.Boors.ServiceHandler.*;
import ir.Boors.Model.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.sql.*;

public class GTCHandler {
	private static List<String> refreshLists(List<Order> sellOrders,List<Order> buyOrders) throws SQLException{
		List<String> messsages = new LinkedList<String>();		
		while(true){
			if(sellOrders.isEmpty() || buyOrders.isEmpty())
				break ;
			if(Order.isPriceMatch(sellOrders.get(0),buyOrders.get(0))){
				messsages.add(Order.execute(sellOrders.get(0), buyOrders.get(0)));
				if(sellOrders.get(0).getQuantity() == 0){					
					DAO.deleteOrder(sellOrders.get(0));
					sellOrders.remove(0);
				}
				if(buyOrders.get(0).getQuantity() == 0){
					DAO.deleteOrder(buyOrders.get(0));
					buyOrders.remove(0);
				}
			}else{
				break;
			}
		}
		return messsages;
	}
	
	public static List<String> handle(Order order,List<Order> sellOrders,List<Order> buyOrders){
		List<String> messages = new LinkedList<String>();
		try{
			List<Order> orderList ;
			if(order.getOrderType().equals("sell")){
				orderList = sellOrders ;
				if(order.getUser().getId() !=1){
					order.getUser().decreaseShareAmount(order.getQuantity(), order.getStockId());
					DAO.updateShareAmount(order.getUser().getId(),order.getStockId(),order.getUser().getShareAmount(order.getStockId()));
				}
			}else{
				orderList = buyOrders ;
				order.getUser().withdraw(order.getPrice()*order.getQuantity());
				DAO.updateUserInfo(order.getUser());
			}
			orderList.add(order);
			Collections.sort(sellOrders);
			Collections.sort(buyOrders);
			Collections.reverse(buyOrders);
			messages = refreshLists(sellOrders,buyOrders);
			if(order.getQuantity() != 0)
				DAO.addOrder(order);
			if(messages.isEmpty())
				messages.add("Order is queued");
		}catch(SQLException e){
			e.printStackTrace();
			messages.clear();
			messages.add("error in connecting to db");			
		}	
		return messages;
	}
}
