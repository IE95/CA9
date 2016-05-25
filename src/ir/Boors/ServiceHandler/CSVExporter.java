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

@WebServlet("/export")
public class CSVExporter extends HttpServlet {

	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
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
			req.setAttribute("message", "فایل backup.csv با موفقیت در webapps/boors ساخته شد");
		}catch(IOException e){
			e.printStackTrace();
			req.setAttribute("message", "اشکال در ساخت فایل backup.csv");
		}catch(SQLException e){
			e.printStackTrace();
			req.setAttribute("message", "اشکال در ارتباط با پایگاه داده");
		}
		req.getRequestDispatcher("exportResult.jsp").forward(req, resp);
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
}