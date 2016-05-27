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

public class DAO {
	private static final String CONN_STR = "jdbc:hsqldb:hsql://localhost/bourse";
	static {
		try {
				Class.forName("org.hsqldb.jdbc.JDBCDriver");
			} catch (ClassNotFoundException ex) {
				System.err.println("Unable to load HSQLDB JDBC driver");
		}
	}

	public static User serachUser(int id) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from user where id = " + id);
		String name,family;
		int balance ;
		Map<String,Integer> sharesAmount = new HashMap<String, Integer>();
		if(!rs.next()){
			return null;
		}	
		name = rs.getString("name");
		family = rs.getString("family");
		balance = rs.getInt("balance");
		rs = st.executeQuery("select * from share where user_id = " + id);
		while(rs.next()){
			sharesAmount.put(rs.getString("stock_id"),rs.getInt("quantity"));
		}
		con.close();
		return new User(id,name,family,balance,sharesAmount);
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

	public static void addRecord(Record record) throws SQLException{
		DateFormat df= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("insert into record values ("+nextId("record") + ",'" 
																	+ record.getStockId() + "',"
																	+ record.getSeller().getId() + ","
																	+ record.getBuyer().getId() + ","
																	+ record.getQuantity() + "," 
																	+ record.getPrice() + ","
																	+ record.getSellerBalance() + ","
																	+ record.getBuyerBalance() + ",'" 
																	+ record.getOpType() + "','"
																	+ df.format(record.getDate()) + "')");	
		con.close();
	}

	public static Record createRecord(ResultSet rs) throws SQLException{
		User seller,buyer;
		String stockId,opType;
		int id,quantity,price,sellerId,buyerId,sellerBalance,buyerBalance;
		Date regDate;
		id = rs.getInt("id");
		stockId = rs.getString("stock_id");
		seller = serachUser(rs.getInt("seller_id"));
		buyer = serachUser(rs.getInt("buyer_id"));
		sellerBalance = rs.getInt("seller_balance");
		buyerBalance = rs.getInt("buyer_balance");
		quantity = rs.getInt("quantity");
		price = rs.getInt("price");
		opType = rs.getString("op_type");
		regDate = new Date(rs.getTimestamp("reg_date").getTime());
		return new Record(id,stockId,seller,buyer,sellerBalance,buyerBalance,quantity,price,opType,regDate);		
	}

	public static List<Record> getRecords() throws SQLException{
		List<Record> records = new LinkedList<Record>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from record");
		while(rs.next()){
			records.add(createRecord(rs));
		}		
		con.close();
		return records;
	}	


	public static Order createOrder(ResultSet rs,User user) throws SQLException{
		String stockId,orderType,opType;
		int id,quantity,price;
		id = rs.getInt("id");
		stockId = rs.getString("stock_id");
		quantity = rs.getInt("quantity");
		price = rs.getInt("price");
		orderType = rs.getString("request_type");
		opType = rs.getString("op_type");
		return new Order(id,stockId,user,quantity,price,orderType,opType);		
	}

	public static void loadUserOrders(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from request where user_id=" + user.getId());
		while(rs.next()){
			user.addOrder(createOrder(rs,user));
		}
		con.close();		
	}	

	public static void addOrder(Order order) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("insert into request values ("+nextId("request") + ",'" 
																	+ order.getStockId() + "',"
																	+ order.getUser().getId() + ","
																	+ order.getQuantity() + "," 
																	+ order.getPrice() + ",'" 
																	+ order.getOrderType() + "','" 
																	+ order.getOpType() + "')");
		con.close();

	}

	public static void updateOrder(Order order) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("update request set quantity=" + order.getQuantity() + " where id=" + order.getId());
		con.close();
	}

	public static void deleteOrder(Order order) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		st.executeUpdate("delete from request where id=" + order.getId());	
		con.close();	
	}
	
	public static void addUser(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into user values(" + user.getId() + ",'" + user.getName() + "','" + user.getFamily() + "'," + user.getBalance() + ")" );
		con.close();
	}

	public static void updateUserInfo(User user) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("update user set name='" + user.getName() + "',family='" + user.getFamily() + "',balance=" + user.getBalance() + " where id=" + user.getId());
		con.close();
	}

	public static void addShareAmount(int userId,String stockId,int amount) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into share values('" + stockId + "'," + userId + "," + amount + ")");		
		con.close();
	}

	public static Integer findShareAmount(int userId,String stockId) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		ResultSet rs = st.executeQuery("select quantity from share where user_id=" + userId + "and stock_id='" + stockId + "'");
		if(rs.next()){
			con.close();
			return rs.getInt("quantity");
		}else{
			con.close();
			return null;
		}			
	}


	public static void updateShareAmount(int userId,String stockId,int newAmount) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("update share set quantity=" + newAmount + " where user_id=" + userId + "and stock_id='" + stockId + "'");		
		con.close();		
	}

	public static void addStock(Stock stock) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into stock values('" + stock.getId() + "')" );
		con.close();
	}	

	public static List<Order> findStockOrders(String stockId,String orderType) throws SQLException{
		List<Order> orders = new LinkedList<Order>();
		User user;
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from request where stock_id='" +stockId + "' and request_type='" + orderType+ "'");		
		while(rs.next()){
			user = serachUser(rs.getInt("user_id"));
			orders.add(createOrder(rs,user));
		}
		con.close();
		return orders;
	}

	public static Stock serachStock(String id) throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from stock where id='" +id+"'");
		if(rs.next()){			
			con.close();
			return new Stock(id,findStockOrders(id,"sell"),findStockOrders(id,"buy"));
		}else{		
			con.close();
			return null ;
		}		
	}

	public static List<Stock> getStocks() throws SQLException{
		List<Stock> stocks = new LinkedList<Stock>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from stock");
		String stockId;
		while(rs.next()){			
			stockId = rs.getString("id");
			stocks.add(new Stock(stockId,findStockOrders(stockId,"sell"),findStockOrders(stockId,"buy")));
		}		
		con.close();
		return stocks;
	}


	public static void addPendingStock(Stock s)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();		
		st.executeUpdate("insert into pending_stock values('"+ s.getId()+ "',"+s.getOwnerId()+")");
		con.close();
	}


	public static Stock findPendingStock(String stockName)throws SQLException{
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();	
		ResultSet rs = st.executeQuery("select * from pending_stock where id='" + stockName + "'");
		con.close();
		if(rs.next()){
			return new Stock(rs.getString("id"),rs.getInt("owner_id"));
		}else{
			Stock s = serachStock(stockName);
			if(s == null){
				return null;
			}
			return s;
		}		
	}

	public static List<Stock> getPendingStocks() throws SQLException{
		List<Stock> stocks = new LinkedList<Stock>();
		Connection con = DriverManager.getConnection(CONN_STR);
		Statement st = con.createStatement();				
		ResultSet rs = st.executeQuery("select * from pending_stock");
		while(rs.next()){			
			String stockId = rs.getString("id");
			int ownerId = rs.getInt("owner_id");
			stocks.add(new Stock(stockId,ownerId));
		}		
		con.close();
		return stocks;
	}



}
