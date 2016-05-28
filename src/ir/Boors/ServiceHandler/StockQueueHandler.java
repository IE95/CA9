package ir.Boors.ServiceHandler;
import ir.Boors.Model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;


public class StockQueueHandler extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String stockId ;
		String json = "{\"result\":" ;
		Stock stock = null;
		boolean hasError = false;
		stockId = req.getParameter("instrument");
		if(stockId == null)
			stockId="all";
		try{
			if(stockId.equals("all")){			
				json+="1,\"stocks\":[" ;
				for (Stock s : DAO.getStocks()) {
					json+= s.getJson() ;
					json+=",";				
				}
				if(json.charAt(json.length()-1) == ',')
					json = json.substring(0,json.length()-1);
				json+="]";		
				json+="}";
			}
			else{
				stock = DAO.serachStock(stockId);
				if(stock==null){
					json+="0,\"errMsg\":\"there is no stock with symbol " + stockId + "\"}";
				}
				else{
					json+="1,\"stocks\":" + stock.getJson() + "}" ;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
			json = "{\"result\":0,\"errMsg\":\"error in connecting to DB\"}" ;
		}
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(json);
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
		
}