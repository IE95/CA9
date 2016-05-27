package ir.Boors.ServiceHandler;
import ir.Boors.Model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.io.PrintWriter;
import java.util.List;
import java.util.LinkedList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

@WebServlet("/addUserRole")
public class AddUserRole extends HttpServlet {

	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		int userId=0;
		String message ="",role="";
		String json = "{\"result\":" ;
		boolean hasError = false;
		try {
			userId = Integer.parseInt(req.getParameter("userId"));
			role = req.getParameter("role");	
			if(!(role.equals("user") || role.equals("finance_user") || role.equals("company_owner") || role.equals("admin"))){
				hasError = true ;
				message = "wrong role" ;
			}
			if(hasError == false){
				User user = DAO.serachUser(userId);
				if(user==null){
					hasError = true ;
					message = "user not found" ;
				}else{
					if(myDAO.hasUserRole(userId,role)){
						hasError = true ;
						message = "user already have role " + role ;	
					}else{
						myDAO.addUserRole(userId,role);
					}
				}
			}
		}catch (NumberFormatException e) {
			e.printStackTrace();
			hasError = true ;
			message = "wrong format input" ;
		}catch(SQLException e){
			e.printStackTrace();
			hasError = true ;
			message = "db problem" ;
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