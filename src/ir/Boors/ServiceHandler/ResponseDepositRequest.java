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

@WebServlet("/responseDepositRequest")
public class ResponseDepositRequest extends HttpServlet {

	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {		
	int requestId = 0 ,userId=0;				
	String message ="", command = "";
	String json = "{\"result\":" ;
	boolean hasError = false;
	try {
		userId = Integer.parseInt(req.getUserPrincipal().getName());
		requestId = Integer.parseInt(req.getParameter("requestId"));	
		command = req.getParameter("command");
		if( (command == null) || !(command.equals("accept") || command.equals("reject")) ){
			message = "unknown command";
			throw new NumberFormatException();
		}
		DepositRequest dr = DAO.findDepositRequestById(requestId);
		if(dr == null){
			message = "request not found";
			throw new NumberFormatException();
		}
		if(command.equals("accept")){
			User user = DAO.serachUser(userId);
			if(user==null){
				message = "user not found" ;
				throw new NumberFormatException();
			}
			user.deposit(dr.getAmount());
			DAO.updateUserInfo(user);
		}
		DAO.deleteDepositRequest(requestId);
	}catch (NumberFormatException e) {
		e.printStackTrace();
		if(message.equals(""))
			message = "wrong format input" ;
		hasError = true ;
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