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

public class FrontController extends HttpServlet {

	// URLs must have the form /polling/ControllerClass.action
	// the execute() method of the ControllerClass will be called
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = request.getRequestURL();
		try {
			// HttpSession session = request.getSession();
			// if (session != null && session.getAttribute("user") == null) {
				
			// }
			request.getRequestDispatcher(url).forward(request, response);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			response.setContentType("text/html");
			if (ex.getTargetException() instanceof SQLException)
				response.getOutputStream().println("Error in accessing database!<p>Contact system administrator");
			else
				response.getOutputStream().println("Internal system error!<p>Contact system administrator");
		} catch (Exception e) {
			e.printStackTrace();
			response.setContentType("text/html");
			response.getOutputStream().println("Internal system error!<p>Contact system administrator");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
