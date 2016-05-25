package ir.Boors.ServiceHandler.OpTypeHandler;
import ir.Boors.ServiceHandler.*;
import ir.Boors.Model.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.sql.*;

public class IOCHandler {
	
	public static List<String> handle(Order order,List<Order> sellOrders,List<Order> buyOrders){
		List<Order> opositOrderList ;
		if(order.getOrderType().equals("sell"))
			opositOrderList = buyOrders ;
		else
			opositOrderList = sellOrders ;
		List<String> messages = new LinkedList<String>();
		int remain = order.getQuantity() ;
		for(Order currOrder : opositOrderList){
			if(Order.isPriceMatch(order,currOrder)){
				int minq;
				if(order.getQuantity() < currOrder.getQuantity())
					minq = order.getQuantity();
				else
					minq = currOrder.getQuantity();
				remain-=minq;
			}else{
				break;
			}
		}
		if(remain > 0){
			messages.add("Order is declined");
			return messages ;
		}
		try{
			while(order.getQuantity() > 0){
				Order currOrder = opositOrderList.get(0); 
				messages.add(Order.execute(order, currOrder));
				if(currOrder.getQuantity()<=0)
					DAO.deleteOrder(opositOrderList.get(0));
			}	
		}catch(SQLException e){
			e.printStackTrace();
			messages.clear();
			messages.add("error in connecting to db");
		}
		return messages;		
	}
}












