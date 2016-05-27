package ir.Boors.Model;
import ir.Boors.ServiceHandler.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class myDAO {
	private static final String CONN_STR = "jdbc:hsqldb:hsql://localhost/bourse";
	static {
		try {
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load HSQLDB JDBC driver");
		}
	}

	public static int nextId(String table) throws SQLException {
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select max(id) as max_id from " + table);
		int maxId = 0;
		if (rs.next()) {
			maxId = rs.getInt("max_id");
		}
		con.close();
		return maxId + 1;
	}

	public static void addDepositRequest(DepositRequest dr) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		st.executeUpdate("insert into deposit_req values(" + nextId("deposit_req") + "," + dr.getUserId() + "," + dr.getAmount() + ")" );
		con.close();
	}

	public static void loadUserDepositRequests(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from deposit_req where user_id=" + user.getId());
		user.clearDepositRequests();
		while(rs.next()){
			user.addDepositRequest(new DepositRequest(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("amount")));
		}
		con.close();		
	}	

	public static DepositRequest findDepositRequestById(int requestId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from deposit_req where id=" + requestId);
		if(rs.next()){
			con.close();
			return new DepositRequest(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("amount"));
		}else{
			con.close();
			return null ;
		}
	}

	public static List<DepositRequest> getDepositRequests() throws SQLException{
		List<DepositRequest> requests = new LinkedList<DepositRequest>(); 
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from deposit_req");
		while(rs.next()){
			requests.add(new DepositRequest(rs.getInt("id"),rs.getInt("user_id"),rs.getInt("amount")));
		}
		con.close();
		return requests;
	}

	public static void deleteDepositRequest(int requestId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeQuery("delete from deposit_req where id=" + requestId);
		con.close();
	}

	public static boolean hasUserRole(int userId,String role) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs =st.executeQuery("select * from user_role where id=" + userId + " and role='" + role + "'");
		con.close();
		if(rs.next())
			return true;
		else
			return false;
	}

	public static void addUserRole(int userId,String role) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeQuery("insert into user_role values(" + userId + ",'" + role + "')");
		con.close();
	}

}
