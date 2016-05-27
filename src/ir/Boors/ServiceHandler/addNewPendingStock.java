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


@WebServlet("/addNewPendingStock")
public class addNewPendingStock extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String name;
		boolean hasError = false;
		String message = "" ;
		try{
			name = req.getParameter("name");
			int ownerId = Integer.parseInt(req.getUserPrincipal().getName());
			if(name==null){
				throw new NumberFormatException();
			}
			Stock s = DAO.findPendingStock(name);
			
			
			if(s!=null){	
				message = "Repeated id";			
				hasError = true ;
			}else{				
				DAO.addPendingStock(new Stock(name,ownerId));
				message = "New user is added" ;
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

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}

