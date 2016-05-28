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


public class AddDepositRequest extends HttpServlet {

	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		int amount = 0 ,userId=0;
		String message ="";
		String json = "{\"result\":" ;
		boolean hasError = false;
		try {
			userId = Integer.parseInt(req.getUserPrincipal().getName());
			amount = Integer.parseInt(req.getParameter("amount"));	
			User user = DAO.serachUser(userId);
			if(user==null){
				hasError = true ;
				message = "user not found" ;
			}else{
				DAO.addDepositRequest(new DepositRequest(userId,amount));
				message = "request registered successfully" ;				
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
			json+="1,\"message\":\"" + message + "\"";
			json+="}";			
		}
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(json);
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}