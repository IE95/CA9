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


public class CSVExporter extends HttpServlet {

	public void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		String json = "{\"result\":" ;
		String message = "" ;
		boolean hasError = false;
		try {
			List<Record> records = DAO.getRecords();
			File file = new File(getServletContext().getRealPath("/")+"backup.csv");
			if (!file.exists()) {
				file.createNewFile();
			}
			PrintWriter pw = new PrintWriter(file);
			for (Record record: records) {
				pw.println(record.toString());	
			}
			pw.close();
			message = "backup.csv successfully created in webapps/boors";
		}catch(IOException e){
			e.printStackTrace();
			hasError = true;
			message = "error in creating backup.csv file";
		}catch(SQLException e){
			e.printStackTrace();
			hasError = true;
			message =  "db problem";
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