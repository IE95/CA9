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




public class GetUserProfile extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		int id ;
		String json = "{\"result\":" ;
		String errMsg="";
		boolean hasError = false ;
		User user = null;
		try{
			id = Integer.parseInt(req.getParameter("userId"));
			user = DAO.serachUser(id);
			if(user==null){
				hasError = true;
				errMsg = "wrong id";	
			}else{
				DAO.loadUserOrders(user);
				DAO.loadUserDepositRequests(user);
			}
		}catch(NumberFormatException e){
			hasError = true;
			errMsg = "wrong format input";
		}catch(SQLException e){
			e.printStackTrace();
			hasError = true ;
			errMsg = "db problem";			
		}
		if(hasError){
			json+="0,\"errMsg\":\"" + errMsg + "\"";
			json+="}";
		}else{
			json+="1,\"userProfile\":" + user.getJson();
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