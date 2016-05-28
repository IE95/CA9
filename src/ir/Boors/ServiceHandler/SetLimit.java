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



public class SetLimit extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		
		
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String name;
		boolean hasError = false;
		String message = "" ;
		try{
			int limit = Integer.parseInt(req.getParameter("limit"));
			if(limit>0)
				DAO.setLimit(limit);
			else{
				message  = "less than zero limit";
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