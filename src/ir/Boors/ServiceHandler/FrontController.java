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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.*;



public class FrontController extends HttpServlet {

	// URLs must have the form /polling/ControllerClass.action
	// the execute() method of the ControllerClass will be called
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String className = request.getServletPath().substring(1);
		try {
			//check csrf
			int serverSide = DAO.getCSRF(request.getSession().getId());
			int userSide = Integer.parseInt(request.getParameter("csrf").trim());
			if(serverSide != userSide){
				response.getWriter().println("CSRF ATTACK");
				response.setStatus(400);
				return;
			}
			Class ctrlClass = Class.forName("ir.Boors.ServiceHandler." + className);
			Method m = ctrlClass.getMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
			m.invoke(ctrlClass.newInstance(), request, response);
		} catch (Exception e) {
			e.printStackTrace();

			response.setContentType("text/html");
			response.setStatus(400);
			response.getWriter().println("CSRF ATTACK");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String className = request.getServletPath().substring(1);
		try {
			//check csrf
			int serverSide = DAO.getCSRF(request.getSession().getId());
			int userSide = Integer.parseInt(request.getParameter("csrf").trim());
			if(serverSide != userSide){
				response.getWriter().println("CSRF ATTACK");
				response.setStatus(400);
				return;
			}
			Class ctrlClass = Class.forName("ir.Boors.ServiceHandler." + className);
			Method m = ctrlClass.getMethod("doPost", HttpServletRequest.class, HttpServletResponse.class);
			m.invoke(ctrlClass.newInstance(), request, response);
		} catch (Exception e) {
	
			response.setContentType("text/html");
			response.setStatus(400);
			response.getWriter().println("CSRF ATTACK");
		}
	}

}
