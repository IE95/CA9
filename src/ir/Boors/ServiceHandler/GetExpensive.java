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

public class GetExpensive extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String name;
		boolean hasError = false;
		String message = "" ;
		List<Order> orders = null;
		try{		
			orders = DAO.getExpensiveRequests();
		}catch(SQLException e){
			message = e.getMessage();
			hasError = true;	
		}
		PrintWriter out = resp.getWriter();
		if(hasError==true){
			out.println(message);
			return;
		}
		resp.setContentType("text/html");
    	
    	StringBuilder sb  = new StringBuilder();
    	sb.append("[");
    	boolean b = false;
		for(Order s : orders){
			sb.append(s.getJson());
			sb.append(",");
			b = true;
		} 
		if(b == true)
			sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		out.print(sb.toString());
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}

