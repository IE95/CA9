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


@WebServlet("/getMyStockStatistic")
public class StockStatistic extends HttpServlet {
	protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {

		boolean hasError = false;
		String message = "" ;
		StringBuilder sb = new StringBuilder();
		try{
			int ownerId = Integer.parseInt(req.getUserPrincipal().getName());
			List<String> strings =  DAO.getStocksOfUser(ownerId);
			boolean a =false;
			sb.append("[");
			int total=0;
			for(String s : strings){
				a =true;
				sb.append("{");
				sb.append("\"stockName\":");

				sb.append("\""+s+"\",");


				sb.append("\"shares\":");
				sb.append("[");
				List<Share> shares = DAO.getShares(s);
				boolean b = false;
				for(Share sh : shares){
					sb.append(sh.getJson());
					sb.append(",");
					b = true;
					total += sh.count;
				}
				if(b == true)
            		sb.deleteCharAt(sb.length()-1);
				sb.append("]");
				sb.append(",\"total\":");
				sb.append(total);
				sb.append("}");
				sb.append(",");

			}
			if(a == true)
            	sb.deleteCharAt(sb.length()-1);

			sb.append("]");
		}catch(NumberFormatException e){
			message = "Mismatched parameters";
			hasError = true;
		}catch(SQLException e){
			message = e.getMessage();
			hasError = true;			
		}
		resp.setContentType("text/html");
    	PrintWriter out = resp.getWriter();
		out.println(sb.toString());
		
	}

	protected void doPost(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {
		
	}
}

