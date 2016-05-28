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


public class TradeQueueCtrl extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String stockId ;
		String message = "";
		Stock stock = null;
		boolean hasError = false;
		stockId = req.getParameter("instrument");
		if(stockId == null){
			message = "Mismatched parameters";
			hasError = true ;
		}else{
			try{
				stock = DAO.serachStock(stockId);
				if(stock==null){
					message = "سهامی با این شناسه پیدا نشد";
					hasError = true ;				
				}
			}catch(SQLException e){
				e.printStackTrace();
				hasError = true ;
				message = "مشکل اتصال به پایگاه داده" ;
			}
		}
		if(hasError){
			req.setAttribute("hasError", 1);
			req.setAttribute("message", message);
			req.getRequestDispatcher("tradeQueue.jsp").forward(req, resp);
		}else{
			req.setAttribute("hasError", 0);
			req.setAttribute("sellQ", stock.getSellOrders());
			req.setAttribute("buyQ", stock.getBuyOrders());
			req.getRequestDispatcher("tradeQueue.jsp").forward(req, resp);
		}
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
		
}