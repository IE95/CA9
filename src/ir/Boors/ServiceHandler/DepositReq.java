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

@WebServlet("/depositReq")
public class DepositReq extends HttpServlet {

	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		int id = 0;
		int amount = 0 ;
		String message ="";
		boolean hasError = false;
		try {
			id = Integer.parseInt(req.getParameter("id"));
			amount = Integer.parseInt(req.getParameter("amount"));	
			User user = DAO.serachUser(id);
			if(user==null){
				hasError = true ;
				message = "کاربری با این شناسه پیدا نشد ." ;
			}else{
				user.deposit(amount);
				DAO.updateUserInfo(user);
				message = "درخواست شما با موفقیت انجام شد" ;				
			}
		}catch (NumberFormatException e) {
			e.printStackTrace();
			hasError = true ;
			message = "ورودی ها غلط است." ;
		}catch(SQLException e){
			e.printStackTrace();
			hasError = true ;
			message = "مشکل در اتصال به پایگاه داده" ;
		}	
		if(hasError)
			req.setAttribute("hasError", 1);
		else
			req.setAttribute("hasError", 0);
		req.setAttribute("message", message);
		req.getRequestDispatcher("depositReq.jsp").forward(req, resp);
		return ;
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}