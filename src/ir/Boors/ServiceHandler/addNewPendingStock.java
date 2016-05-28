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

public class addNewPendingStock extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doPost(req,resp);
		
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String name;
		boolean hasError = false;
		String message = "" ;
		try{
			name = req.getParameter("name");
			int ownerId = Integer.parseInt(req.getUserPrincipal().getName());
			int quantity = Integer.parseInt(req.getParameter("quantity"));
			int price = Integer.parseInt(req.getParameter("price"));
			String request_type = req.getParameter("type").toString();

			if(name==null||name.equals("")){
				throw new NumberFormatException();
			}				
			Stock s = DAO.findPendingStock(name);
			if(s == null){
				DAO.addPendingStock(new Order(-1,name,new User(ownerId),quantity,price,"sell",request_type));	
			}
			else{
				message = "Error : stock was in the system";
				hasError = true;
			}
		}catch(NumberFormatException e){
			message = "Mismatched parameters";
			hasError = true;
		}catch(SQLException e){
			message = e.getMessage();
			hasError = true;			
		}
		resp.setContentType("text/html");
    	PrintWriter out = resp.getWriter();
		if(hasError){
			req.setAttribute("hasError", 1);
			out.println(message);
		}
		else{
			req.setAttribute("hasError", 0);
			out.println("successful");
		}
	}
}

