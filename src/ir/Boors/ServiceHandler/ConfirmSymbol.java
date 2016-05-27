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
		String name;
		boolean hasError = false;
		String message = "" ;
		try{
			name = req.getParameter("name");
			int ownerId = Integer.parseInt(req.getParameter("ownerId"));
			if(name==null){
				throw new MyException("invalide symbol");
			}
			Stock s = new Stock(name,ownerId);
			DAO.confirmSymbol(s);
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
