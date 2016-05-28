package ir.Boors.ServiceHandler;
import ir.Boors.Model.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;
import java.sql.*;


@WebServlet("/Logout")
public class Logout extends HttpServlet {
	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
			
		try{
			DAO.delCSRF(req.getSession().getId());
		}
		catch(SQLException e){

		}
		req.getSession(true).invalidate();
		resp.sendRedirect("home.html");
	}

	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}

}