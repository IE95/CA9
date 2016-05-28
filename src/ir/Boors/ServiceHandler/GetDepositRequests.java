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


public class GetDepositRequests extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String json = "{\"result\":1,\"depositRequests\":[" ;
		try{
			for (DepositRequest dr : DAO.getDepositRequests()) {
				json+= dr.getJson() ;
				json+=",";				
			}
			if(json.charAt(json.length()-1) == ',')
				json = json.substring(0,json.length()-1);
			json+="]";		
			json+="}";
		}catch(SQLException e){
			e.printStackTrace();
			json = "{\"result\":0,\"errMsg\":\"error in connecting to DB\"}";			
		}
		resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println(json);
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
		
}