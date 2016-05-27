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


@WebServlet("/confirmExpensive")
public class ConfirmExpensive extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		
		
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		boolean hasError = false;
		String message = "" ;
		try{

			int id = Integer.parseInt(req.getParameter("id"));
			if(id<0){
				throw new MyException("invalid id");
			}
			DAO.confirmExpensive(id);

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
