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


@WebServlet("/confirmSymbol")
public class ConfirmSymbol extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		
		
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "" ;
		try{
			String name = req.getParameter("name");
			int ownerId = Integer.parseInt(req.getParameter("ownerId"));
			int price = Integer.parseInt(req.getParameter("price"));
			int quantity = Integer.parseInt(req.getParameter("quantity"));
			String type = req.getParameter("opType").toString();
			if(name==null){
				throw new MyException("invalid symbol");
			}
			Stock s = new Stock(name,ownerId);
			DAO.confirmSymbol(s);
			DAO.addOrder(new Order(-1,name,new User(ownerId),quantity,price,"sell",type));
		}catch(NumberFormatException | SQLException | MyException e){
			message = e.getMessage();
			hasError = true;			
		}
		resp.setContentType("text/html");
    	PrintWriter out = resp.getWriter();
		if(hasError){
			out.println(message);
		}
		else{
			out.println("successful");
		}
	}
}
