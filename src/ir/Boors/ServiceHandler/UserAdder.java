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


@WebServlet("/addUser")
public class UserAdder extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		int id ;
		boolean hasError = false;
		String message = "" ;
		String json = "{\"result\":" ;
		try{
			id = Integer.parseInt(req.getParameter("id"));
			User user = DAO.serachUser(id);
			String name = req.getParameter("name");
			String family = req.getParameter("family");
			String password = req.getParameter("password");
			String email = req.getParameter("email");
			if(name==null || family==null || password==null || email==null){
				throw new NumberFormatException();
			}
			if(user!=null){	
				message = "Repeated id";			
				hasError = true ;
			}else{				
				DAO.addUser(new User(id,name,family,email,password));
				DAO.addUserRole(id,"user");
				message = "New user is added" ;
			}
		}catch(NumberFormatException e){
			message = "Mismatched parameters";
			hasError = true;
		}catch(SQLException e){
			message = "db problem";
			hasError = true;			
		}
		if(hasError){
			json+="0,\"message\":\"" + message + "\"";
			json+="}";
		}else{
			json+="1";
			json+="}";			
		}
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(json);
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}